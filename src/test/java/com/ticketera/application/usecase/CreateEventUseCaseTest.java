package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateEventUseCaseTest {

    private EventRepository repository;
    private CreateEventUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new CreateEventUseCase(repository);
    }

    @Test
    @DisplayName("Creates event with all fields and persists it")
    void createsEventWithAllFieldsAndPersistsIt() {
        LocalDateTime date = LocalDateTime.of(2026, 12, 1, 20, 0);
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500,
            "Miles Davis", date, "20:00", 25000.0, "/images/jazz.webp", true);

        assertNotNull(result.getCode());
        assertEquals("Jazz Night", result.getName());
        assertEquals("Gran Teatro", result.getVenue());
        assertEquals(500, result.getCapacity());
        assertEquals(1L, result.getCityId().value());
        assertEquals("Miles Davis", result.getArtist());
        assertEquals(date, result.getEventDate());
        assertEquals("20:00", result.getEventTime());
        assertEquals(25000.0, result.getPrice().value());
        assertEquals("/images/jazz.webp", result.getImageUrl());
        assertTrue(result.isFeatured());
        assertEquals("SCHEDULED", result.getStatus());
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Delegates validation to domain")
    void delegatesValidationToDomain() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(1L, "Jazz Night", "Gran Teatro", 0,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false));
        assertEquals("Capacity must be positive", ex.getMessage());
        verify(repository, never()).save(any());
    }
}