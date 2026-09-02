package com.ticketera.infrastructure.web.dto;

import com.ticketera.application.usecase.OrderResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resultado de una orden de compra procesada")
public record OrderResponse(
    @Schema(description = "Identificador de la orden", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,
    @Schema(description = "Codigo del evento", example = "evt-jazz-001")
    String eventId,
    @Schema(description = "Nombre del evento", example = "Jazz Night")
    String eventName,
    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    String customerName,
    @Schema(description = "Email del cliente", example = "customer@email.com")
    String customerEmail,
    @Schema(description = "Entradas adquiridas", example = "2")
    int ticketsPurchased,
    @Schema(description = "Entradas restantes en inventario", example = "498")
    int remainingTickets,
    @Schema(description = "Precio unitario", example = "25000.0")
    double unitPrice,
    @Schema(description = "Precio total", example = "50000.0")
    double totalPrice,
    @Schema(description = "Estado de la orden", example = "CONFIRMED")
    String status,
    @Schema(description = "Fecha de creacion", example = "2026-08-25T12:00:00")
    String createdAt,
    @Schema(description = "Lista de entradas emitidas individualmente")
    List<TicketResponseDto> tickets
) {

    public static OrderResponse fromDomain(OrderResult result) {
        List<TicketResponseDto> ticketDtos = result.tickets() != null
            ? result.tickets().stream().map(TicketResponseDto::fromDomain).toList()
            : List.of();

        return new OrderResponse(
            result.id(),
            result.eventId(),
            result.eventName(),
            result.customerName(),
            result.customerEmail(),
            result.ticketsPurchased(),
            result.remainingTickets(),
            result.unitPrice(),
            result.totalPrice(),
            result.status(),
            result.createdAt(),
            ticketDtos);
    }
}