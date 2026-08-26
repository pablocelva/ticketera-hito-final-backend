package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.TicketId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ticket")
class TicketTest {

    @Test
    @DisplayName("Creates ticket with all fields")
    void createsTicketWithAllFields() {
        Ticket ticket = new Ticket(
            new TicketId("t-001"),
            1L,
            "Juan Perez",
            "juan@email.com");

        assertThat(ticket.getId().value()).isEqualTo("t-001");
        assertThat(ticket.getEventId()).isEqualTo(1L);
        assertThat(ticket.getCustomerName()).isEqualTo("Juan Perez");
        assertThat(ticket.getCustomerEmail()).isEqualTo("juan@email.com");
    }

    @Test
    @DisplayName("Creates ticket with anonymous customer")
    void createsTicketWithAnonymousCustomer() {
        Ticket ticket = new Ticket(
            new TicketId("t-002"),
            1L,
            "anonymous",
            "");

        assertThat(ticket.getCustomerName())
            .as("Anonymous customer should have name 'anonymous'")
            .isEqualTo("anonymous");
        assertThat(ticket.getCustomerEmail())
            .as("Anonymous customer should have empty email")
            .isEmpty();
    }
}
