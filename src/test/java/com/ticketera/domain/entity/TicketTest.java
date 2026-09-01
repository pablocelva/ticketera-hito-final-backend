
package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.Money;
import com.ticketera.domain.valueobject.OrderStatus;
import com.ticketera.domain.valueobject.TicketId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Ticket")
class TicketTest {

    @Test
    @DisplayName("Creates ticket with all fields (legacy constructor)")
    void createsTicketWithLegacyConstructor() {
        Ticket ticket = new Ticket(
            new TicketId("t-001"),
            1L,
            "Juan Perez",
            "juan@email.com");

        assertThat(ticket.getId().value()).isEqualTo("t-001");
        assertThat(ticket.getEventId().value()).isEqualTo("1");
        assertThat(ticket.getCustomerName()).isEqualTo("Juan Perez");
        assertThat(ticket.getCustomerEmail().value()).isEqualTo("juan@email.com");
    }

    @Test
    @DisplayName("Creates ticket with anonymous customer")
    void createsTicketWithAnonymousCustomer() {
        Ticket ticket = new Ticket(
            new TicketId("t-002"),
            1L,
            "anonymous",
            null);

        assertThat(ticket.getCustomerName())
            .as("Anonymous customer should have name 'anonymous'")
            .isEqualTo("anonymous");
        assertThat(ticket.getCustomerEmail())
            .as("Anonymous customer should have null email")
            .isNull();
    }

    @Test
    @DisplayName("Creates enriched ticket with all fields")
    void createsEnrichedTicket() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket(
            new TicketId("t-003"),
            new EventId("evt-001"),
            "Pablo",
            new Email("pablo@test.com"),
            "order-001",
            new Money(25000.0),
            new Money(50000.0),
            OrderStatus.CONFIRMED,
            now);

        assertThat(ticket.getId().value()).isEqualTo("t-003");
        assertThat(ticket.getEventId().value()).isEqualTo("evt-001");
        assertThat(ticket.getCustomerName()).isEqualTo("Pablo");
        assertThat(ticket.getCustomerEmail().value()).isEqualTo("pablo@test.com");
        assertThat(ticket.getOrderId()).isEqualTo("order-001");
        assertThat(ticket.getUnitPrice().value()).isEqualTo(25000.0);
        assertThat(ticket.getTotalAmount().value()).isEqualTo(50000.0);
        assertThat(ticket.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(ticket.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Throws when customerName is blank")
    void throwsWhenCustomerNameIsBlank() {
        assertThatThrownBy(() -> new Ticket(
            new TicketId("t-004"),
            new EventId("evt-001"),
            "  ",
            new Email("test@test.com"),
            "order-001",
            new Money(25000.0),
            new Money(50000.0),
            OrderStatus.CONFIRMED,
            LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer name cannot be blank");
    }

    @Test
    @DisplayName("Throws when customerName is null")
    void throwsWhenCustomerNameIsNull() {
        assertThatThrownBy(() -> new Ticket(
            new TicketId("t-005"),
            new EventId("evt-001"),
            null,
            new Email("test@test.com"),
            "order-001",
            new Money(25000.0),
            new Money(50000.0),
            OrderStatus.CONFIRMED,
            LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer name cannot be blank");
    }

    @Test
    @DisplayName("Enriched ticket with null email for anonymous")
    void enrichedTicketWithNullEmail() {
        Ticket ticket = new Ticket(
            new TicketId("t-006"),
            new EventId("evt-001"),
            "anonymous",
            null,
            "order-002",
            new Money(10000.0),
            new Money(10000.0),
            OrderStatus.CONFIRMED,
            LocalDateTime.now());

        assertThat(ticket.getCustomerEmail()).isNull();
        assertThat(ticket.getCustomerName()).isEqualTo("anonymous");
    }

    @Test
    @DisplayName("Legacy constructor with blank email results in null")
    void legacyConstructorWithBlankEmailResultsInNull() {
        Ticket ticket = new Ticket(
            new TicketId("t-007"),
            1L,
            "Juan",
            "");

        assertThat(ticket.getCustomerEmail())
            .as("Blank email should result in null (anonymous)")
            .isNull();
    }

    @Test
    @DisplayName("Enriched ticket with userId keeps the linked user id")
    void enrichedTicketWithUserId() {
        Ticket ticket = new Ticket(
            new TicketId("t-008"),
            new EventId("evt-001"),
            "Pablo",
            new Email("pablo@test.com"),
            "order-003",
            new Money(25000.0),
            new Money(25000.0),
            OrderStatus.CONFIRMED,
            42L,
            LocalDateTime.now());

        assertThat(ticket.getUserId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("Enriched ticket without userId stays anonymous")
    void enrichedTicketWithoutUserIdIsAnonymous() {
        Ticket ticket = new Ticket(
            new TicketId("t-009"),
            new EventId("evt-001"),
            "Guest",
            null,
            "order-004",
            new Money(25000.0),
            new Money(25000.0),
            OrderStatus.CONFIRMED,
            LocalDateTime.now());

        assertThat(ticket.getUserId()).isNull();
    }
}