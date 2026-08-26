package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TicketId Value Object")
class TicketIdTest {

    @Test
    @DisplayName("Creates ticket id with valid value")
    void createsTicketIdWithValidValue() {
        TicketId id = new TicketId("t-001");

        assertThat(id.value())
            .as("TicketId value should be 't-001'")
            .isEqualTo("t-001");
    }

    @Test
    @DisplayName("Trims whitespace")
    void trimsWhitespace() {
        TicketId id = new TicketId("  t-001  ");

        assertThat(id.value())
            .as("TicketId should be trimmed")
            .isEqualTo("t-001");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Throws when value is null, empty or blank")
    void throwsWhenValueIsNullOrBlank(String invalid) {
        assertThatThrownBy(() -> new TicketId(invalid))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ticket ID cannot be blank");
    }
}
