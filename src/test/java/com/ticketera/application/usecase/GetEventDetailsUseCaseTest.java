package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEventDetailsUseCaseTest {

    private EventRepository repository;
    private GetEventDetailsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new GetEventDetailsUseCase(repository);
    }

    @Test
    @DisplayName("Returns event when found")
    void returnsEventWhenFound() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
        new com.ticketera.domain.valueobject.CityId(1L), "Jazz Night", "Teatro", 100, 90,
        "Artist", java.time.LocalDateTime.now(), "20:00", 25000.0, false, EventStatus.SCHEDULED, "/img.jpg");        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertEquals(event, useCase.execute(1L));
    }

    @Test
    @DisplayName("Throws EventNotFoundException when missing")
    void throwsEventNotFoundWhenMissing() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute(999L));
        assertEquals("Event not found: 999", ex.getMessage());
    }
}
