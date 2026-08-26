package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CityId Value Object")
class CityIdTest {

    @Test
    @DisplayName("Creates CityId with Long value")
    void createsCityIdWithLongValue() {
        CityId id = new CityId(1L);

        assertThat(id.value())
            .as("CityId value should be 1L")
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("Throws when value is null")
    void throwsWhenValueIsNull() {
        assertThatThrownBy(() -> new CityId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City id is required");
    }

    @Test
    @DisplayName("Equals returns true for same value")
    void equalsReturnsTrueForSameValue() {
        CityId a = new CityId(1L);
        CityId b = new CityId(1L);

        assertThat(a)
            .as("Two CityIds with same value should be equal")
            .isEqualTo(b);
    }

    @Test
    @DisplayName("Equals returns false for different value")
    void equalsReturnsFalseForDifferentValue() {
        CityId a = new CityId(1L);
        CityId b = new CityId(2L);

        assertThat(a)
            .as("Two CityIds with different values should not be equal")
            .isNotEqualTo(b);
    }

    @Test
    @DisplayName("Equals returns false for different type")
    void equalsReturnsFalseForDifferentType() {
        CityId a = new CityId(1L);

        assertThat(a)
            .as("CityId should not equal a String")
            .isNotEqualTo("not a CityId");
    }

    @Test
    @DisplayName("Equals returns true for same reference")
    void equalsReturnsTrueForSameReference() {
        CityId a = new CityId(1L);

        assertThat(a)
            .as("Same reference should be equal")
            .isEqualTo(a);
    }

    @Test
    @DisplayName("HashCode is consistent for same value")
    void hashCodeIsConsistent() {
        CityId a = new CityId(1L);
        CityId b = new CityId(1L);

        assertThat(a.hashCode())
            .as("HashCode should be consistent for same value")
            .isEqualTo(b.hashCode());
    }
}
