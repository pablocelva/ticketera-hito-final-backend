package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderStatus Value Object")
class OrderStatusTest {

    @Test
    @DisplayName("Should have all three statuses")
    void shouldHaveAllThreeStatuses() {
        assertThat(OrderStatus.values()).hasSize(3);
    }

    @Test
    @DisplayName("Should resolve each status by name")
    void shouldResolveByName() {
        assertThat(OrderStatus.valueOf("CONFIRMED")).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(OrderStatus.valueOf("CANCELLED")).isEqualTo(OrderStatus.CANCELLED);
        assertThat(OrderStatus.valueOf("REFUNDED")).isEqualTo(OrderStatus.REFUNDED);
    }

    @Test
    @DisplayName("Should return correct name for each status")
    void shouldReturnCorrectName() {
        assertThat(OrderStatus.CONFIRMED.name()).isEqualTo("CONFIRMED");
        assertThat(OrderStatus.CANCELLED.name()).isEqualTo("CANCELLED");
        assertThat(OrderStatus.REFUNDED.name()).isEqualTo("REFUNDED");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid name")
    void shouldThrowForInvalidName() {
        assertThatThrownBy(() -> OrderStatus.valueOf("INVALID"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}