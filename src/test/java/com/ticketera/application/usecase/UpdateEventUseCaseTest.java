package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateEventUseCaseTest {

    private EventRepository repository;
    private UpdateEventUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new UpdateEventUseCase(repository);
    }

    @Test
    @DisplayName("Updates event details successfully")
    void updatesEventDetails() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, false, "SCHEDULED", "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        LocalDateTime newDate = LocalDateTime.of(2027, 6, 15, 21, 0);
        Event result = useCase.execute(1L, "Rock Night", "Estadio", 500,
            "AC/DC", newDate, "21:00", 50000.0, "/images/rock.webp", true);

        assertEquals("Rock Night", result.getName());
        assertEquals("Estadio", result.getVenue());
        assertEquals(500, result.getCapacity());
        assertEquals("AC/DC", result.getArtist());
        assertEquals(newDate, result.getEventDate());
        assertEquals(50000.0, result.getPrice().value());
        assertTrue(result.isFeatured());
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
            () -> useCase.execute(999L, "New", "Venue", 100,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false));
    }

    @Test
    @DisplayName("Throws when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz", "Teatro", 100, 80,
            "Art", LocalDateTime.now(), "20:00", 10000.0, false, "SCHEDULED", "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> useCase.execute(1L, "Small", "Venue", 10,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false));
        assertTrue(ex.getMessage().contains("cannot be less than sold tickets"));
        verify(repository, never()).save(any());
    }
}