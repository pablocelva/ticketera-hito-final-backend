package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
            new CityId(1L), "Jazz Night", "Teatro", 100, 90,
            "Artist", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.SCHEDULED, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertThat(useCase.execute(1L)).isEqualTo(event);
    }

    @Test
    @DisplayName("Throws EventNotFoundException when missing")
    void throwsEventNotFoundWhenMissing() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L))
            .isInstanceOf(EventNotFoundException.class)
            .hasMessage("Event not found: 999");
    }
}
