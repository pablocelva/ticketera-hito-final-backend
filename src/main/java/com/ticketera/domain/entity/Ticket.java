package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.Money;
import com.ticketera.domain.valueobject.OrderStatus;
import com.ticketera.domain.valueobject.TicketId;

import java.time.LocalDateTime;

public class Ticket {

    private final TicketId id;
    private final EventId eventId;
    private final String customerName;
    private final Email customerEmail;
    private final String orderId;
    private final Money unitPrice;
    private final Money totalAmount;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

    public Ticket(TicketId id, Long eventId, String customerName, String customerEmail) {
        this.id = id;
        this.eventId = new EventId(String.valueOf(eventId));
        this.customerName = customerName;
        this.customerEmail = (customerEmail == null || customerEmail.isBlank()) ? null : new Email(customerEmail);
        this.orderId = null;
        this.unitPrice = null;
        this.totalAmount = null;
        this.status = null;
        this.createdAt = null;
    }

    public Ticket(TicketId id, EventId eventId, String customerName, Email customerEmail,
                  String orderId, Money unitPrice, Money totalAmount,
                  OrderStatus status, LocalDateTime createdAt) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderId = orderId;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TicketId getId() {
        return id;
    }

    public EventId getEventId() {
        return eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Email getCustomerEmail() {
        return customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}