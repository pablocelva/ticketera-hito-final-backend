package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import com.ticketera.domain.valueobject.EventStatus;

@Schema(description = "Peticion de actualizacion de evento")
public record UpdateEventRequest(

    @Schema(description = "Nombre del evento", example = "Jazz Night Updated")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Lugar del evento", example = "Gran Teatro Lima")
    @NotBlank(message = "Venue is required")
    String venue,

    @Schema(description = "Capacidad total de entradas", example = "600")
    @Positive(message = "Capacity must be positive")
    int capacity,

    @Schema(description = "Nombre del artista o banda", example = "Miles Davis")
    String artist,

    @Schema(description = "Fecha y hora del evento", example = "2026-12-01T20:00:00")
    @NotNull(message = "Event date is required")
    LocalDateTime eventDate,

    @Schema(description = "Hora del evento", example = "20:00")
    String eventTime,

    @Schema(description = "Precio de la entrada", example = "25000.0")
    @Positive(message = "Price must be positive")
    double price,

    @Schema(description = "URL de la imagen del evento", example = "/images/jazz.webp")
    String imageUrl,

    @Schema(description = "Evento destacado en la cartelera", example = "false")
    boolean featured,

    @Schema(description = "Estado del evento (publicar, cancelar)", example = "ON_SALE",
    allowableValues = {"SCHEDULED", "ON_SALE", "SOLD_OUT", "LIVE", "FINISHED", "CANCELED"})
    EventStatus status
) {
}