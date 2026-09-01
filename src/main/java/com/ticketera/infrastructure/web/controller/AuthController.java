package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.AuthUseCase;
import com.ticketera.domain.entity.User;
import com.ticketera.infrastructure.security.JwtService;
import com.ticketera.infrastructure.web.dto.AuthRequestDto;
import com.ticketera.infrastructure.web.dto.AuthResponseDto;
import com.ticketera.infrastructure.web.dto.RegisterRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Auth", description = "Registro, login y emision de tokens JWT")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthUseCase authUseCase,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.authUseCase = authUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Registrar usuario", description = "Crea un usuario nuevo y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado"),
        @ApiResponse(responseCode = "400", description = "Datos de registro invalidos"),
        @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        User user = authUseCase.register(request.email(), request.fullName(), request.password());
        String token = buildToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(token, user));
    }

    @Operation(summary = "Iniciar sesion", description = "Valida credenciales y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticado correctamente"),
        @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        String email = request.email().toLowerCase().trim();
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, request.password()));
        User user = authUseCase.findByEmail(email);
        String token = buildToken(user);
        return ResponseEntity.ok(toResponse(token, user));
    }

    private String buildToken(User user) {
        return jwtService.generateToken(user.getEmail().value(), Map.of(
            "id", user.getId(),
            "fullName", user.getFullName(),
            "role", user.getRole().name()));
    }

    private AuthResponseDto toResponse(String token, User user) {
        return new AuthResponseDto(token, user.getId(), user.getEmail().value(),
            user.getFullName(), user.getRole().name());
    }
}
