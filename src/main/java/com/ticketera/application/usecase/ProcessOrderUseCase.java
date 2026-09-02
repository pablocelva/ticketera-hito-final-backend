package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.TicketRepository;
import com.ticketera.domain.repository.UserRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.Money;
import com.ticketera.domain.valueobject.OrderStatus;
import com.ticketera.domain.valueobject.TicketId;
import com.ticketera.domain.valueobject.TicketQuantity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProcessOrderUseCase {

    private static final String ADMIN_EMAIL = "admin@ticketera.com";

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final MessageNotifier notifier;
    private final UserRepository userRepository;

    public ProcessOrderUseCase(EventRepository eventRepository, TicketRepository ticketRepository,
                               MessageNotifier notifier, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.notifier = notifier;
        this.userRepository = userRepository;
    }

    public OrderResult execute(Long eventId, int quantity) {
        return execute(eventId, quantity, null, null);
    }

    public OrderResult execute(Long eventId, int quantity, String customerName, String customerEmail) {
        TicketQuantity tickets = new TicketQuantity(quantity);

        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        event.reserveTickets(tickets);
        eventRepository.save(event);

        double unitPrice = event.getPrice().value();
        double totalPrice = unitPrice * quantity;
        String orderId = UUID.randomUUID().toString();
        String resolvedName = customerName != null ? customerName : "anonymous";
        String resolvedEmail = (customerEmail != null && !customerEmail.isBlank()) ? customerEmail : null;
        Long userId = resolveUserId(resolvedEmail);

        java.util.List<Ticket> createdTickets = new java.util.ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Ticket ticket = new Ticket(
                new TicketId(UUID.randomUUID().toString()),
                new EventId(String.valueOf(event.getDbId())),
                resolvedName,
                resolvedEmail != null ? new com.ticketera.domain.valueobject.Email(resolvedEmail) : null,
                orderId,
                new Money(unitPrice),
                new Money(totalPrice),
                OrderStatus.CONFIRMED,
                userId,
                LocalDateTime.now());
            ticketRepository.save(ticket);
            createdTickets.add(ticket);
        }

        notifier.send(ADMIN_EMAIL,
            "Order processed for: " + event.getName()
                + " (" + tickets.value() + " tickets), with ID: " + event.getCode().value());

        return new OrderResult(
            orderId,
            event.getCode().value(),
            event.getName(),
            resolvedName,
            resolvedEmail,
            tickets.value(),
            event.getAvailableTickets(),
            unitPrice,
            totalPrice,
            OrderStatus.CONFIRMED.name(),
            LocalDateTime.now().toString(),
            createdTickets);
    }

    private Long resolveUserId(String customerEmail) {
        if (customerEmail == null) {
            return null;
        }
        return userRepository.findByEmail(customerEmail)
            .map(com.ticketera.domain.entity.User::getId)
            .orElse(null);
    }
}