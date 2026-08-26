package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Money Value Object")
public class MoneyTest {
    @Test
    @DisplayName("Should create valid money")
    public void shouldCreateValidMoney() {
        Money money = new Money(50.0);

        assertThat(money.value())
            .as("Money value should be 50.0")
            .isEqualTo(50.0);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -100.0})
    @DisplayName("Should throw InvalidOrderException when price is less than or equal to zero")
    public void shouldThrowWhenPriceIsNotPositive(double invalid) {
        assertThatThrownBy(() -> new Money(invalid))
            .isInstanceOf(InvalidOrderException.class)
            .hasMessage("Price must be positive");
    }
}
