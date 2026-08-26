package com.ticketera.domain.entity;

import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.exception.SoldOutException;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("evt-001", event.getCode().value());
        assertEquals("Jazz Night", event.getName());
        assertEquals("Jazz Club", event.getVenue());
        assertEquals(500, event.getCapacity());
        assertEquals(0, event.getTicketSold());
        assertEquals("Miles Davis", event.getArtist());
        assertEquals(LocalDateTime.of(2026, 12, 1, 20, 0), event.getEventDate());
        assertEquals("20:00", event.getEventTime());
        assertEquals(25000.0, event.getPrice().value());
        assertEquals("/images/jazz.webp", event.getImageUrl());
        assertTrue(event.isFeatured());
        assertEquals(EventStatus.SCHEDULED, event.getStatus());
    }

    @Test
    @DisplayName("Should return true when tickets are available")
    public void shouldReturnTrueWhenTicketsAreAvailable() {
        assertTrue(newEvent().hasAvailability());
    }

    @Test
    @DisplayName("Should return false when event is sold out")
    public void shouldReturnFalseWhenEventIsSoldOut() {
        Event event = new Event("evt-002", "Full House", "Arena", 1,
            "Artist", LocalDateTime.now(), "21:00", 10000.0, "/img.jpg", false);
        event.reserveTickets(new TicketQuantity(1));
        assertFalse(event.hasAvailability());
    }

    @Test
    @DisplayName("Should calculate available tickets correctly")
    public void shouldCalculateAvailableTicketsCorrectly() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));
        assertEquals(497, event.getAvailableTickets());
        assertEquals(3, event.getTicketSold());
    }

    @Test
    @DisplayName("Should reserve tickets successfully through the aggregate root")
    public void shouldReserveTicketsSuccessfully() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));
        assertEquals(497, event.getAvailableTickets());
    }

    @Test
    @DisplayName("Should throw SoldOutException when reserving more than available")
    public void shouldThrowSoldOutWhenNotEnoughTickets() {
        Event event = newEvent();
        SoldOutException ex = assertThrows(SoldOutException.class,
            () -> event.reserveTickets(new TicketQuantity(600)));
        assertEquals("Not enough tickets available", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    public void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        Event event = newEvent();
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.reserveTickets(new TicketQuantity(0)));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is negative")
    public void shouldThrowInvalidOrderWhenQuantityIsNegative() {
        Event event = newEvent();
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.reserveTickets(new TicketQuantity(-1)));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Reconstitutes event preserving availability")
    void reconstitutesEventPreservingAvailability() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new com.ticketera.domain.valueobject.CityId(1L),
            "Jazz Night", "Teatro", 100, 30,
            "Miles Davis", LocalDateTime.of(2026, 12, 1, 20, 0), "20:00",
            25000.0, true, EventStatus.ON_SALE, "/images/jazz.webp");

        assertEquals(30, event.getAvailableTickets());
        assertEquals(70, event.getTicketSold());
        assertEquals("Miles Davis", event.getArtist());
        assertEquals(25000.0, event.getPrice().value());
        assertTrue(event.isFeatured());
        assertEquals(EventStatus.ON_SALE, event.getStatus());

        event.reserveTickets(new TicketQuantity(10));
        assertEquals(20, event.getAvailableTickets());
    }

    @Test
    @DisplayName("Should update event details successfully")
    void updatesEventDetails() {
        Event event = newEvent();
        event.updateDetails("Rock Night", "Estadio", 1000,
            "AC/DC", LocalDateTime.of(2027, 1, 15, 21, 0), "21:00",
            50000.0, "/images/rock.webp", true);
        assertEquals("Rock Night", event.getName());
        assertEquals("Estadio", event.getVenue());
        assertEquals(1000, event.getCapacity());
        assertEquals("AC/DC", event.getArtist());
        assertEquals(LocalDateTime.of(2027, 1, 15, 21, 0), event.getEventDate());
        assertEquals(50000.0, event.getPrice().value());
    }

    @Test
    @DisplayName("Should throw when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(100));
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.updateDetails("Small", "Venue", 50,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false));
        assertTrue(ex.getMessage().contains("cannot be less than sold tickets"));
    }

    @Test
    @DisplayName("Should detect sold tickets")
    void detectsSoldTickets() {
        Event event = newEvent();
        assertFalse(event.hasSoldTickets());
        event.reserveTickets(new TicketQuantity(1));
        assertTrue(event.hasSoldTickets());
    }

    @Test
    @DisplayName("Should set city id")
    void shouldSetCityId() {
        Event event = newEvent();
        event.setCityId(5L);
        assertEquals(5L, event.getCityId().value());
    }

    @Test
    @DisplayName("Reconstitutes event with city id")
    void reconstitutesEventWithCityId() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new com.ticketera.domain.valueobject.CityId(10L),
            "Rock", "Stadium", 200, 100,
            "Bands", LocalDateTime.now(), "20:00", 30000.0, false, EventStatus.SCHEDULED, "/img.jpg");
        assertEquals(10L, event.getCityId().value());
        assertEquals(200, event.getCapacity());
        assertEquals(100, event.getAvailableTickets());
    }
}