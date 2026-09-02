package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Ticket;
import java.util.List;

public record OrderResult(
    String id,
    String eventId,
    String eventName,
    String customerName,
    String customerEmail,
    int ticketsPurchased,
    int remainingTickets,
    double unitPrice,
    double totalPrice,
    String status,
    String createdAt,
    List<Ticket> tickets
) {
    public OrderResult(String id, String eventId, String eventName, String customerName, String customerEmail,
                       int ticketsPurchased, int remainingTickets, double unitPrice, double totalPrice,
                       String status, String createdAt) {
        this(id, eventId, eventName, customerName, customerEmail, ticketsPurchased, remainingTickets,
             unitPrice, totalPrice, status, createdAt, List.of());
    }
}