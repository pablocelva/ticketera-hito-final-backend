package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.valueobject.Email;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.Money;
import com.ticketera.domain.valueobject.OrderStatus;
import com.ticketera.domain.valueobject.TicketId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "order_id", length = 36)
    private String orderId;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected TicketEntity() {
    }

    private TicketEntity(String id, Long eventId, String customerName, String customerEmail,
                         String orderId, Double unitPrice, Double totalAmount,
                         String status, LocalDateTime createdAt) {
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

    public static TicketEntity fromDomain(Ticket ticket) {
        return new TicketEntity(
            ticket.getId().value(),
            ticket.getEventId() != null ? Long.parseLong(ticket.getEventId().value()) : null,
            ticket.getCustomerName(),
            ticket.getCustomerEmail() != null ? ticket.getCustomerEmail().value() : null,
            ticket.getOrderId(),
            ticket.getUnitPrice() != null ? ticket.getUnitPrice().value() : null,
            ticket.getTotalAmount() != null ? ticket.getTotalAmount().value() : null,
            ticket.getStatus() != null ? ticket.getStatus().name() : null,
            ticket.getCreatedAt());
    }

    public Ticket toDomain() {
        return new Ticket(
            new TicketId(id),
            new EventId(String.valueOf(eventId)),
            customerName,
            customerEmail != null ? new Email(customerEmail) : null,
            orderId,
            unitPrice != null ? new Money(unitPrice) : null,
            totalAmount != null ? new Money(totalAmount) : null,
            status != null ? OrderStatus.valueOf(status) : null,
            createdAt);
    }

    public String getId() {
        return id;
    }
}