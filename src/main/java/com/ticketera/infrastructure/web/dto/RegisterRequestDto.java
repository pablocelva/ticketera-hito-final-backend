package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de registro de un nuevo usuario")
public record RegisterRequestDto(

    @Schema(description = "Email del usuario", example = "user@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @Schema(description = "Nombre completo del usuario", example = "Jane Doe")
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 160, message = "Full name must be between 2 and 160 characters")
    String fullName,

    @Schema(description = "Contrasena del usuario", example = "secret")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password
) {
}
