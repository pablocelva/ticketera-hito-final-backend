package com.ticketera.domain.entity;

import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.exception.SoldOutException;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Ticket Pool")
class TicketPoolTest {

    @Test
    @DisplayName("Should throw IllegalArgumentException when capacity is not positive")
    void shouldThrowWhenCapacityIsNotPositive() {
        assertThatThrownBy(() -> new TicketPool(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Capacity must be positive");

        assertThatThrownBy(() -> new TicketPool(-5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Capacity must be positive");
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        TicketPool pool = new TicketPool(10);

        assertThatThrownBy(() -> pool.reserve(new TicketQuantity(0)))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    @DisplayName("Should throw SoldOutException when not enough tickets")
    void shouldThrowSoldOutWhenNotEnoughTickets() {
        TicketPool pool = new TicketPool(5);

        assertThatThrownBy(() -> pool.reserve(new TicketQuantity(10)))
            .isInstanceOf(SoldOutException.class)
            .hasMessage("Not enough tickets available");
    }

    @Test
    @DisplayName("Should reserve tickets successfully when available")
    void shouldReserveTicketsSuccessfully() {
        TicketPool pool = new TicketPool(10);
        pool.reserve(new TicketQuantity(3));

        assertThat(pool.getAvailable()).isEqualTo(7);
        assertThat(pool.hasAvailability()).isTrue();
    }

    @Test
    @DisplayName("Should not have availability when pool is empty")
    void shouldNotHaveAvailabilityWhenEmpty() {
        TicketPool pool = new TicketPool(1);
        pool.reserve(new TicketQuantity(1));

        assertThat(pool.hasAvailability()).isFalse();
    }

    @Test
    @DisplayName("Reconstitutes pool preserving available tickets")
    void reconstitutesPoolPreservingAvailableTickets() {
        TicketPool pool = new TicketPool(100, 30);

        assertThat(pool.getAvailable()).isEqualTo(30);
        assertThat(pool.hasAvailability()).isTrue();
    }

    @Test
    @DisplayName("Rejects available greater than capacity on reconstitution")
    void rejectsAvailableGreaterThanCapacity() {
        assertThatThrownBy(() -> new TicketPool(100, 150))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Available must be between 0 and capacity");
    }

    @Test
    @DisplayName("Rejects negative available on reconstitution")
    void rejectsNegativeAvailable() {
        assertThatThrownBy(() -> new TicketPool(100, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Available must be between 0 and capacity");
    }

    @Test
    @DisplayName("Rejects non-positive capacity on reconstitution")
    void rejectsNonPositiveCapacityOnReconstitution() {
        assertThatThrownBy(() -> new TicketPool(0, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Capacity must be positive");

        assertThatThrownBy(() -> new TicketPool(-10, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Capacity must be positive");
    }
}
