package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetEventsUseCaseTest {

    private EventRepository repository;
    private GetEventsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new GetEventsUseCase(repository);
    }

    @Test
    @DisplayName("Returns all events from repository")
    void returnsAllEventsFromRepository() {
        List<Event> expected = List.of(
            Event.reconstitute(1L, new EventId("evt-1"),
                new com.ticketera.domain.valueobject.CityId(1L), "Jazz Night", "Teatro", 100, 90,
                "Artist1", java.time.LocalDateTime.now(), "20:00", 25000.0, false, "SCHEDULED", "/img1.jpg"),
            Event.reconstitute(2L, new EventId("evt-2"),
                new com.ticketera.domain.valueobject.CityId(1L), "Rock Fest", "Estadio", 1000, 500,
                "Artist2", java.time.LocalDateTime.now(), "21:00", 50000.0, true, "ON_SALE", "/img2.jpg"));        when(repository.findAll()).thenReturn(expected);

        List<Event> result = useCase.execute();

        assertEquals(expected, result);
        verify(repository).findAll();
    }
}
