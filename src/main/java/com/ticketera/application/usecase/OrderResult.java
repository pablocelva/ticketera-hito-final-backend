package com.ticketera.application.usecase;

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
    String createdAt
) {}