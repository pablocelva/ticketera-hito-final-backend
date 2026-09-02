package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entrada vendida")
public record TicketResponseDto(
    @Schema(description = "Identificador de la entrada", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,
    @Schema(description = "Código único de entrada", example = "550e8400")
    String code,
    @Schema(description = "ID del evento", example = "1")
    Long eventId,
    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    String customerName,
    @Schema(description = "Email del cliente", example = "customer@email.com")
    String customerEmail,
    @Schema(description = "Precio unitario", example = "15000.0")
    Double unitPrice,
    @Schema(description = "Fecha de compra")
    String purchaseDate
) {

    public static TicketResponseDto fromDomain(Ticket ticket) {
        return new TicketResponseDto(
            ticket.getId().value(),
            ticket.getId().value(),
            ticket.getEventId() != null ? Long.parseLong(ticket.getEventId().value()) : null,
            ticket.getCustomerName(),
            ticket.getCustomerEmail() != null ? ticket.getCustomerEmail().value() : null,
            ticket.getUnitPrice() != null ? ticket.getUnitPrice().value() : null,
            ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : null);
    }
}
