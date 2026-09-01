package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticacion con el token JWT y datos del usuario")
public record AuthResponseDto(

    @Schema(description = "Token JWT para las peticiones autenticadas", example = "eyJhbGciOiJIUzI1NiJ9...")
    String token,

    @Schema(description = "Identificador del usuario", example = "1")
    Long id,

    @Schema(description = "Email del usuario", example = "user@example.com")
    String email,

    @Schema(description = "Nombre completo del usuario", example = "Jane Doe")
    String fullName,

    @Schema(description = "Rol del usuario", example = "ROLE_USER")
    String role
) {
}
