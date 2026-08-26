package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EventId Value Object")
public class EventIdTest {
    @Test
    @DisplayName("Should create EventId and trim value")
    public void shouldCreateAndTrim() {
        EventId id = new EventId("  EVT-001  ");

        assertThat(id.value())
            .as("EventId should be trimmed")
            .isEqualTo("EVT-001");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw IllegalArgumentException when id is null, empty or blank")
    public void shouldThrowWhenNullOrBlank(String invalid) {
        assertThatThrownBy(() -> new EventId(invalid))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Event ID cannot be blank");
    }
}
