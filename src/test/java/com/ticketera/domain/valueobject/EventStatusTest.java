package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EventStatus Value Object")
public class EventStatusTest {

    @Test
    @DisplayName("Should have all six statuses")
    public void shouldHaveAllSixStatuses() {
        assertEquals(6, EventStatus.values().length);
    }

    @Test
    @DisplayName("Should resolve each status by name")
    public void shouldResolveByName() {
        assertEquals(EventStatus.SCHEDULED, EventStatus.valueOf("SCHEDULED"));
        assertEquals(EventStatus.ON_SALE, EventStatus.valueOf("ON_SALE"));
        assertEquals(EventStatus.SOLD_OUT, EventStatus.valueOf("SOLD_OUT"));
        assertEquals(EventStatus.LIVE, EventStatus.valueOf("LIVE"));
        assertEquals(EventStatus.FINISHED, EventStatus.valueOf("FINISHED"));
        assertEquals(EventStatus.CANCELED, EventStatus.valueOf("CANCELED"));
    }

    @Test
    @DisplayName("Should return correct name for each status")
    public void shouldReturnCorrectName() {
        assertEquals("SCHEDULED", EventStatus.SCHEDULED.name());
        assertEquals("ON_SALE", EventStatus.ON_SALE.name());
        assertEquals("SOLD_OUT", EventStatus.SOLD_OUT.name());
        assertEquals("LIVE", EventStatus.LIVE.name());
        assertEquals("FINISHED", EventStatus.FINISHED.name());
        assertEquals("CANCELED", EventStatus.CANCELED.name());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid name")
    public void shouldThrowForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> EventStatus.valueOf("INVALID"));
    }
}
