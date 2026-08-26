package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Event")
public class EventTest {
    private Event newEvent() {
        return new Event("evt-001", "Jazz Night", "Jazz Club", 500,
            "Miles Davis", LocalDateTime.of(2026, 12, 1, 20, 0), "20:00",
            25000.0, "/images/jazz.webp", true);
    }

    @Test
    @DisplayName("Should initialize event with correct values")
    public void shouldInitializeEventWithCorrectValues() {
        Event event = newEvent();

        assertThat(event.getCode().value()).isEqualTo("evt-001");
        assertThat(event.getName()).isEqualTo("Jazz Night");
        assertThat(event.getVenue()).isEqualTo("Jazz Club");
        assertThat(event.getCapacity()).isEqualTo(500);
        assertThat(event.getTicketSold()).as("Initially zero tickets sold").isZero();
        assertThat(event.getArtist()).isEqualTo("Miles Davis");
        assertThat(event.getEventDate()).isEqualTo(LocalDateTime.of(2026, 12, 1, 20, 0));
        assertThat(event.getEventTime()).isEqualTo("20:00");
        assertThat(event.getPrice().value()).isEqualTo(25000.0);
        assertThat(event.getImageUrl()).isEqualTo("/images/jazz.webp");
        assertThat(event.isFeatured()).as("Event should be featured").isTrue();
        assertThat(event.getStatus()).isEqualTo(EventStatus.SCHEDULED);
    }

    @Test
    @DisplayName("Should return true when tickets are available")
    public void shouldReturnTrueWhenTicketsAreAvailable() {
        assertThat(newEvent().hasAvailability())
            .as("Event with 500 capacity and 0 sold should have availability")
            .isTrue();
    }

    @Test
    @DisplayName("Should return false when event is sold out")
    public void shouldReturnFalseWhenEventIsSoldOut() {
        Event event = new Event("evt-002", "Full House", "Arena", 1,
            "Artist", LocalDateTime.now(), "21:00", 10000.0, "/img.jpg", false);
        event.reserveTickets(new TicketQuantity(1));

        assertThat(event.hasAvailability())
            .as("Event with 0 remaining tickets should not have availability")
            .isFalse();
    }

    @Test
    @DisplayName("Should calculate available tickets correctly")
    public void shouldCalculateAvailableTicketsCorrectly() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));

        assertThat(event.getAvailableTickets()).isEqualTo(497);
        assertThat(event.getTicketSold()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should reserve tickets successfully through the aggregate root")
    public void shouldReserveTicketsSuccessfully() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));

        assertThat(event.getAvailableTickets())
            .as("After reserving 3 of 500, 497 should remain")
            .isEqualTo(497);
    }

    @Test
    @DisplayName("Should throw SoldOutException when reserving more than available")
    public void shouldThrowSoldOutWhenNotEnoughTickets() {
        Event event = newEvent();

        assertThatThrownBy(() -> event.reserveTickets(new TicketQuantity(600)))
            .isInstanceOf(com.ticketera.domain.exception.SoldOutException.class)
            .hasMessage("Not enough tickets available");
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    public void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        Event event = newEvent();

        assertThatThrownBy(() -> event.reserveTickets(new TicketQuantity(0)))
            .isInstanceOf(com.ticketera.domain.exception.InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is negative")
    public void shouldThrowInvalidOrderWhenQuantityIsNegative() {
        Event event = newEvent();

        assertThatThrownBy(() -> event.reserveTickets(new TicketQuantity(-1)))
            .isInstanceOf(com.ticketera.domain.exception.InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    @DisplayName("Reconstitutes event preserving availability")
    void reconstitutesEventPreservingAvailability() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new com.ticketera.domain.valueobject.CityId(1L),
            "Jazz Night", "Teatro", 100, 30,
            "Miles Davis", LocalDateTime.of(2026, 12, 1, 20, 0), "20:00",
            25000.0, true, EventStatus.ON_SALE, "/images/jazz.webp");

        assertThat(event.getAvailableTickets()).isEqualTo(30);
        assertThat(event.getTicketSold()).isEqualTo(70);
        assertThat(event.getArtist()).isEqualTo("Miles Davis");
        assertThat(event.getPrice().value()).isEqualTo(25000.0);
        assertThat(event.isFeatured()).isTrue();
        assertThat(event.getStatus()).isEqualTo(EventStatus.ON_SALE);

        event.reserveTickets(new TicketQuantity(10));
        assertThat(event.getAvailableTickets())
            .as("After reserving 10 of 30 available, 20 should remain")
            .isEqualTo(20);
    }

    @Test
    @DisplayName("Should update event details successfully")
    void updatesEventDetails() {
        Event event = newEvent();
        event.updateDetails("Rock Night", "Estadio", 1000,
            "AC/DC", LocalDateTime.of(2027, 1, 15, 21, 0), "21:00",
            50000.0, "/images/rock.webp", true);

        assertThat(event.getName()).isEqualTo("Rock Night");
        assertThat(event.getVenue()).isEqualTo("Estadio");
        assertThat(event.getCapacity()).isEqualTo(1000);
        assertThat(event.getArtist()).isEqualTo("AC/DC");
        assertThat(event.getEventDate()).isEqualTo(LocalDateTime.of(2027, 1, 15, 21, 0));
        assertThat(event.getPrice().value()).isEqualTo(50000.0);
    }

    @Test
    @DisplayName("Should throw when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(100));

        assertThatThrownBy(() -> event.updateDetails("Small", "Venue", 50,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false))
            .isInstanceOf(com.ticketera.domain.exception.InvalidOrderException.class)
            .hasMessageContaining("cannot be less than sold tickets");
    }

    @Test
    @DisplayName("Should detect sold tickets")
    void detectsSoldTickets() {
        Event event = newEvent();
        assertThat(event.hasSoldTickets()).as("No tickets sold initially").isFalse();

        event.reserveTickets(new TicketQuantity(1));
        assertThat(event.hasSoldTickets()).as("Should detect sold tickets").isTrue();
    }

    @Test
    @DisplayName("Should set city id")
    void shouldSetCityId() {
        Event event = newEvent();
        event.setCityId(5L);

        assertThat(event.getCityId().value())
            .as("CityId should be updated to 5")
            .isEqualTo(5L);
    }

    @Test
    @DisplayName("Reconstitutes event with city id")
    void reconstitutesEventWithCityId() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new com.ticketera.domain.valueobject.CityId(10L),
            "Rock", "Stadium", 200, 100,
            "Bands", LocalDateTime.now(), "20:00", 30000.0, false, EventStatus.SCHEDULED, "/img.jpg");

        assertThat(event.getCityId().value()).isEqualTo(10L);
        assertThat(event.getCapacity()).isEqualTo(200);
        assertThat(event.getAvailableTickets()).isEqualTo(100);
    }
}
