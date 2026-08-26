package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Evento de la cartelera con su inventario")
public record EventResponse(
    @Schema(description = "ID de la base de datos", example = "1") Long id,
    @Schema(description = "Codigo del evento", example = "evt-jazz-001") String code,
    @Schema(description = "ID de la ciudad", example = "1") Long cityId,
    @Schema(description = "Nombre del evento", example = "Jazz Night") String name,
    @Schema(description = "Lugar", example = "Gran Teatro Lima") String venue,
    @Schema(description = "Capacidad total", example = "500") int capacity,
    @Schema(description = "Entradas disponibles", example = "498") int availableTickets,
    @Schema(description = "Entradas vendidas", example = "2") int ticketsSold,
    @Schema(description = "Artista o banda", example = "Miles Davis") String artist,
    @Schema(description = "Fecha del evento") LocalDateTime eventDate,
    @Schema(description = "Hora del evento", example = "20:00") String eventTime,
    @Schema(description = "Precio de la entrada", example = "25000.0") double price,
    @Schema(description = "URL de la imagen", example = "/images/jazz.webp") String imageUrl,
    @Schema(description = "Evento destacado", example = "false") boolean featured,
    @Schema(description = "Estado del evento", example = "ON_SALE") String status
) {

    public static EventResponse fromDomain(Event event) {
        return new EventResponse(
            event.getDbId(),
            event.getCode().value(),
            event.getCityId().value(),
            event.getName(),
            event.getVenue(),
            event.getCapacity(),
            event.getAvailableTickets(),
            event.getTicketSold(),
            event.getArtist(),
            event.getEventDate(),
            event.getEventTime(),
            event.getPrice().value(),
            event.getImageUrl(),
            event.isFeatured(),
            event.getStatus());
    }
}