package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.exception.InvalidOrderException;
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
            "Art", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.SCHEDULED, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        LocalDateTime newDate = LocalDateTime.of(2027, 6, 15, 21, 0);
        Event result = useCase.execute(1L, "Rock Night", "Estadio", 500,
            "AC/DC", newDate, "21:00", 50000.0, "/images/rock.webp", true, EventStatus.ON_SALE);

        assertThat(result.getName()).isEqualTo("Rock Night");
        assertThat(result.getVenue()).isEqualTo("Estadio");
        assertThat(result.getCapacity()).isEqualTo(500);
        assertThat(result.getArtist()).isEqualTo("AC/DC");
        assertThat(result.getEventDate()).isEqualTo(newDate);
        assertThat(result.getPrice().value()).isEqualTo(50000.0);
        assertThat(result.isFeatured()).isTrue();
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, "New", "Venue", 100,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false, null))
            .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    @DisplayName("Throws when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz", "Teatro", 100, 80,
            "Art", LocalDateTime.now(), "20:00", 10000.0, false,
            EventStatus.SCHEDULED, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertThatThrownBy(() -> useCase.execute(1L, "Small", "Venue", 10,
                "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false, null))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessageContaining("cannot be less than sold tickets");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Preserves status when null is passed")
    void preservesStatusWhenNullPassed() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.SCHEDULED, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Event result = useCase.execute(1L, "New Name", "Venue", 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", false, null);

        assertThat(result.getStatus()).isEqualTo(EventStatus.SCHEDULED);
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Preserves status when SCHEDULED is passed (default case)")
    void preservesStatusWhenScheduledPassed() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.SCHEDULED, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Event result = useCase.execute(1L, "New Name", "Venue", 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", false, EventStatus.SCHEDULED);

        assertThat(result.getStatus()).isEqualTo(EventStatus.SCHEDULED);
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Marks event as SOLD_OUT during update")
    void marksEventAsSoldOutDuringUpdate() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.ON_SALE, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Event result = useCase.execute(1L, "New Name", "Venue", 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", false, EventStatus.SOLD_OUT);

        assertThat(result.getStatus()).isEqualTo(EventStatus.SOLD_OUT);
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Marks event as CANCELED during update")
    void marksEventAsCanceledDuringUpdate() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, false,
            EventStatus.ON_SALE, "/img.jpg");
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Event result = useCase.execute(1L, "New Name", "Venue", 100,
            "Art", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", false, EventStatus.CANCELED);

        assertThat(result.getStatus()).isEqualTo(EventStatus.CANCELED);
        verify(repository).save(event);
    }
}
