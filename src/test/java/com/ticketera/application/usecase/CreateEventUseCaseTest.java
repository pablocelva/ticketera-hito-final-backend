package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        "Miles Davis", date, "20:00", 25000.0, "/images/jazz.webp", true, EventStatus.SCHEDULED);

        assertThat(result.getCode()).as("Event code should be generated").isNotNull();
        assertThat(result.getName()).isEqualTo("Jazz Night");
        assertThat(result.getVenue()).isEqualTo("Gran Teatro");
        assertThat(result.getCapacity()).isEqualTo(500);
        assertThat(result.getCityId().value()).isEqualTo(1L);
        assertThat(result.getArtist()).isEqualTo("Miles Davis");
        assertThat(result.getEventDate()).isEqualTo(date);
        assertThat(result.getEventTime()).isEqualTo("20:00");
        assertThat(result.getPrice().value()).isEqualTo(25000.0);
        assertThat(result.getImageUrl()).isEqualTo("/images/jazz.webp");
        assertThat(result.isFeatured()).isTrue();
        assertThat(result.getStatus()).isEqualTo(EventStatus.SCHEDULED);
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Delegates validation to domain")
    void delegatesValidationToDomain() {
        assertThatThrownBy(() -> useCase.execute(1L, "Jazz Night", "Gran Teatro", 0,
        "Art", LocalDateTime.now(), "20:00", 10000.0, "/img.jpg", false, EventStatus.SCHEDULED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Capacity must be positive");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Creates event with ON_SALE status")
    void createsEventWithOnSaleStatus() {
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500,
            "Miles Davis", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", true, EventStatus.ON_SALE);

        assertThat(result.getStatus()).isEqualTo(EventStatus.ON_SALE);
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Creates event with SOLD_OUT status")
    void createsEventWithSoldOutStatus() {
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500,
            "Miles Davis", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", true, EventStatus.SOLD_OUT);

        assertThat(result.getStatus()).isEqualTo(EventStatus.SOLD_OUT);
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Creates event with CANCELED status")
    void createsEventWithCanceledStatus() {
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500,
            "Miles Davis", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", true, EventStatus.CANCELED);

        assertThat(result.getStatus()).isEqualTo(EventStatus.CANCELED);
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Creates event with null status defaults to SCHEDULED")
    void createsEventWithNullStatusDefaultsToScheduled() {
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500,
            "Miles Davis", LocalDateTime.now(), "20:00", 25000.0, "/img.jpg", true, null);

        assertThat(result.getStatus()).isEqualTo(EventStatus.SCHEDULED);
        verify(repository).save(any(Event.class));
    }
}
