package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.AuthUseCase;
import com.ticketera.application.usecase.GetUserTicketsUseCase;
import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.entity.User;
import com.ticketera.infrastructure.security.JwtService;
import com.ticketera.infrastructure.web.dto.AuthResponseDto;
import com.ticketera.infrastructure.web.dto.TicketResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints de perfil y tickets del usuario")
public class UserController {

    private final AuthUseCase authUseCase;
    private final GetUserTicketsUseCase getUserTicketsUseCase;
    private final JwtService jwtService;

    public UserController(AuthUseCase authUseCase, GetUserTicketsUseCase getUserTicketsUseCase, JwtService jwtService) {
        this.authUseCase = authUseCase;
        this.getUserTicketsUseCase = getUserTicketsUseCase;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Obtener perfil del usuario actual", description = "Devuelve los datos del usuario autenticado usando el token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil recuperado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = authUseCase.findByEmail(email);
        return ResponseEntity.ok(new AuthResponseDto(
            jwtService.generateToken(user.getEmail().value(), Map.of(
                "id", user.getId(),
                "fullName", user.getFullName(),
                "role", user.getRole().name())),
            user.getId(),
            user.getEmail().value(),
            user.getFullName(),
            user.getRole().name()));
    }

    @Operation(summary = "Obtener tickets del usuario actual", description = "Lista tickets del usuario logueado por su email de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tickets recuperados"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/me/tickets")
    public ResponseEntity<List<TicketResponseDto>> meTickets(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        String email = userDetails.getUsername();
        List<Ticket> tickets = getUserTicketsUseCase.execute(email);
        List<TicketResponseDto> dtos = tickets.stream()
            .map(TicketResponseDto::fromDomain)
            .toList();
        return ResponseEntity.ok(dtos);
    }
}