package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Email Value Object")
public class EmailTest {
    @Test
    @DisplayName("Should create email and normalize to lowercase trimmed")
    public void shouldCreateEmailAndNormalize() {
        Email email = new Email("  USER@Example.COM  ");

        assertThat(email.value())
            .as("Email should be normalized to lowercase and trimmed")
            .isEqualTo("user@example.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw InvalidEmailException when email is null, empty or blank")
    public void shouldThrowWhenEmailIsNullOrBlank(String invalid) {
        assertThatThrownBy(() -> new Email(invalid))
            .isInstanceOf(InvalidEmailException.class)
            .as("Message should contain 'Invalid email:'")
            .hasMessageContaining("Invalid email:");
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email has no @")
    public void shouldThrowWhenEmailHasNoAtSign() {
        assertThatThrownBy(() -> new Email("juan-sin-arroba"))
            .isInstanceOf(InvalidEmailException.class)
            .hasMessage("Invalid email: juan-sin-arroba");
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email has no domain")
    public void shouldThrowWhenEmailHasNoDomain() {
        assertThatThrownBy(() -> new Email("user@"))
            .isInstanceOf(InvalidEmailException.class)
            .hasMessage("Invalid email: user@");
    }
}
