package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TicketQuantity Value Object")
public class TicketQuantityTest {
    @Test
    @DisplayName("Should create valid quantity")
    public void shouldCreateValidQuantity() {
        TicketQuantity qty = new TicketQuantity(2);

        assertThat(qty.value())
            .as("Quantity value should be 2")
            .isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("Should throw InvalidOrderException when quantity is less than or equal to zero")
    public void shouldThrowWhenQuantityIsNotPositive(int invalid) {
        assertThatThrownBy(() -> new TicketQuantity(invalid))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessage("Quantity must be positive");
    }
}
