package com.ticketera.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("City")
class CityTest {

    @Test
    @DisplayName("Creates city with valid data")
    void createsCityWithValidData() {
        City city = new City(1L, "LIM", "Lima");

        assertThat(city.getId().value()).isEqualTo(1L);
        assertThat(city.getCode()).isEqualTo("LIM");
        assertThat(city.getName()).isEqualTo("Lima");
    }

    @Test
    @DisplayName("Throws when code is null")
    void throwsWhenCodeIsNull() {
        assertThatThrownBy(() -> new City(1L, null, "Lima"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City code is required");
    }

    @Test
    @DisplayName("Throws when code is blank")
    void throwsWhenCodeIsBlank() {
        assertThatThrownBy(() -> new City(1L, "  ", "Lima"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City code is required");
    }

    @Test
    @DisplayName("Throws when name is null")
    void throwsWhenNameIsNull() {
        assertThatThrownBy(() -> new City(1L, "LIM", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        assertThatThrownBy(() -> new City(1L, "LIM", "  "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
    }

    @Test
    @DisplayName("Renames city successfully")
    void renamesCity() {
        City city = new City(1L, "LIM", "Lima");
        city.rename("Lima Metropolitana");

        assertThat(city.getName()).isEqualTo("Lima Metropolitana");
    }

    @Test
    @DisplayName("Throws when renaming to null")
    void throwsWhenRenamingToNull() {
        City city = new City(1L, "LIM", "Lima");

        assertThatThrownBy(() -> city.rename(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
    }

    @Test
    @DisplayName("Throws when renaming to blank")
    void throwsWhenRenamingToBlank() {
        City city = new City(1L, "LIM", "Lima");

        assertThatThrownBy(() -> city.rename("  "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
    }
}
