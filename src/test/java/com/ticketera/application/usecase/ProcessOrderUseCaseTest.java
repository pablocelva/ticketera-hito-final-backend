package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.TicketRepository;
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

class ProcessOrderUseCaseTest {

    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private com.ticketera.application.port.MessageNotifier notifier;
    private ProcessOrderUseCase useCase;
    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        ticketRepository = mock(TicketRepository.class);
        notifier = mock(com.ticketera.application.port.MessageNotifier.class);
        useCase = new ProcessOrderUseCase(eventRepository, ticketRepository, notifier);
        event = Event.reconstitute(
            1L, new EventId("evt-001"),
            new CityId(1L),
            "Jazz Night", "Gran Teatro", 500, 500,
            "Miles Davis", LocalDateTime.now(), "20:00",
            25000.0, false, EventStatus.SCHEDULED, "/img.jpg");
        when(eventRepository.findById(1L))
            .thenReturn(Optional.of(event));
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event id is null")
    void throwsWhenEventIdIsNull() {
        when(eventRepository.findById(null)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(null, 2))
            .isInstanceOf(EventNotFoundException.class)
            .hasMessageContaining("Event not found");
    }

    @Test
    @DisplayName("Throws when quantity is zero")
    void throwsWhenQuantityIsZero() {
        assertThatThrownBy(() -> useCase.execute(1L, 0))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    @DisplayName("Throws when quantity is negative")
    void throwsWhenQuantityIsNegative() {
        assertThatThrownBy(() -> useCase.execute(1L, -1))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFoundWhenEventDoesNotExist() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, 2))
            .isInstanceOf(EventNotFoundException.class)
            .hasMessage("Event not found: 999");

        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Reserves tickets, persists and returns enriched confirmation")
    void reservesTicketsPersistsAndReturnsConfirmation() {
        OrderResult result = useCase.execute(1L, 2);

        assertThat(result.id()).as("Order ID should not be null").isNotNull();
        assertThat(result.eventId()).isEqualTo("evt-001");
        assertThat(result.eventName()).isEqualTo("Jazz Night");
        assertThat(result.ticketsPurchased()).isEqualTo(2);
        assertThat(result.remainingTickets()).isEqualTo(498);
        assertThat(result.unitPrice()).isEqualTo(25000.0);
        assertThat(result.totalPrice()).isEqualTo(50000.0);
        assertThat(result.status()).isEqualTo("CONFIRMED");
        assertThat(result.createdAt()).as("createdAt should not be null").isNotNull();
        verify(eventRepository).save(event);
        verify(ticketRepository, times(2)).save(any());
        verify(notifier).send(eq("admin@ticketera.com"), contains("Jazz Night"));
    }

    @Test
    @DisplayName("Uses anonymous when customerName and customerEmail are null")
    void usesAnonymousWhenCustomerInfoIsNull() {
        OrderResult result = useCase.execute(1L, 1, null, null);

        assertThat(result.eventId()).isEqualTo("evt-001");
        assertThat(result.customerName()).isEqualTo("anonymous");
        assertThat(result.customerEmail()).isNull();
        verify(ticketRepository).save(argThat(ticket ->
            ticket.getCustomerName().equals("anonymous")
                && ticket.getCustomerEmail() == null));
    }

    @Test
    @DisplayName("Uses provided customer name and email when supplied")
    void usesProvidedCustomerInfo() {
        OrderResult result = useCase.execute(1L, 1, "Pablo", "pablo@test.com");

        assertThat(result.eventId()).isEqualTo("evt-001");
        assertThat(result.customerName()).isEqualTo("Pablo");
        assertThat(result.customerEmail()).isEqualTo("pablo@test.com");
        verify(ticketRepository).save(argThat(ticket ->
            ticket.getCustomerName().equals("Pablo")
                && ticket.getCustomerEmail().value().equals("pablo@test.com")));
    }

    @Test
    @DisplayName("Blank email is treated as anonymous")
    void blankEmailIsTreatedAsAnonymous() {
        OrderResult result = useCase.execute(1L, 1, "Juan", "");

        assertThat(result.eventId()).isEqualTo("evt-001");
        assertThat(result.customerName()).isEqualTo("Juan");
        assertThat(result.customerEmail()).isNull();
        verify(ticketRepository).save(argThat(ticket ->
            ticket.getCustomerName().equals("Juan")
                && ticket.getCustomerEmail() == null));
    }
}