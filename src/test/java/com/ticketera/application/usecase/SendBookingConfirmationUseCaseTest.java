package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Send Booking Confirmation Use Case")
class SendBookingConfirmationUseCaseTest {

    @Test
    @DisplayName("Should fail when email is null")
    void shouldFailWhenEmailIsNull() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        assertThatThrownBy(() -> useCase.execute(null, "Jazz Night"))
            .isInstanceOf(InvalidEmailException.class)
            .hasMessage("Invalid email: null");
    }

    @Test
    @DisplayName("Should fail when email is empty")
    void shouldFailWhenEmailIsEmpty() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        assertThatThrownBy(() -> useCase.execute("", "Jazz Night"))
            .isInstanceOf(InvalidEmailException.class)
            .hasMessageContaining("Invalid email:");
    }

    @Test
    @DisplayName("Should send confirmation successfully")
    void shouldSendConfirmationSuccessfully() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        useCase.execute("customer@email.com", "Jazz Night");

        verify(notifierMock, times(1)).send("customer@email.com", "Booking confirmed for: Jazz Night");
    }
}
