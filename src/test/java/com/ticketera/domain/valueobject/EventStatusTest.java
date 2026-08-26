package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EventStatus Value Object")
class EventStatusTest {

    @Test
    @DisplayName("Should have all six statuses")
    void shouldHaveAllSixStatuses() {
        assertThat(EventStatus.values()).hasSize(6);
    }

    @Test
    @DisplayName("Should resolve each status by name")
    void shouldResolveByName() {
        assertThat(EventStatus.valueOf("SCHEDULED")).isEqualTo(EventStatus.SCHEDULED);
        assertThat(EventStatus.valueOf("ON_SALE")).isEqualTo(EventStatus.ON_SALE);
        assertThat(EventStatus.valueOf("SOLD_OUT")).isEqualTo(EventStatus.SOLD_OUT);
        assertThat(EventStatus.valueOf("LIVE")).isEqualTo(EventStatus.LIVE);
        assertThat(EventStatus.valueOf("FINISHED")).isEqualTo(EventStatus.FINISHED);
        assertThat(EventStatus.valueOf("CANCELED")).isEqualTo(EventStatus.CANCELED);
    }

    @Test
    @DisplayName("Should return correct name for each status")
    void shouldReturnCorrectName() {
        assertThat(EventStatus.SCHEDULED.name()).isEqualTo("SCHEDULED");
        assertThat(EventStatus.ON_SALE.name()).isEqualTo("ON_SALE");
        assertThat(EventStatus.SOLD_OUT.name()).isEqualTo("SOLD_OUT");
        assertThat(EventStatus.LIVE.name()).isEqualTo("LIVE");
        assertThat(EventStatus.FINISHED.name()).isEqualTo("FINISHED");
        assertThat(EventStatus.CANCELED.name()).isEqualTo("CANCELED");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid name")
    void shouldThrowForInvalidName() {
        assertThatThrownBy(() -> EventStatus.valueOf("INVALID"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
