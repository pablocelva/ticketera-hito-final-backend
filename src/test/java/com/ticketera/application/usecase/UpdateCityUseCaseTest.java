package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.exception.CityNotFoundException;
import com.ticketera.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateCityUseCaseTest {

    private CityRepository repository;
    private UpdateCityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new UpdateCityUseCase(repository);
    }

    @Test
    @DisplayName("Updates city name successfully")
    void updatesCityNameSuccessfully() {
        City city = new City(1L, "LIM", "Lima");
        when(repository.findById(1L)).thenReturn(Optional.of(city));

        City result = useCase.execute(1L, "Lima Metropolitana");

        assertThat(result.getName()).isEqualTo("Lima Metropolitana");
        verify(repository).save(city);
    }

    @Test
    @DisplayName("Throws CityNotFoundException when city does not exist")
    void throwsCityNotFoundWhenCityDoesNotExist() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, "New Name"))
            .isInstanceOf(CityNotFoundException.class)
            .hasMessage("City with id '999' not found");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        City city = new City(1L, "LIM", "Lima");
        when(repository.findById(1L)).thenReturn(Optional.of(city));

        assertThatThrownBy(() -> useCase.execute(1L, "  "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
        verify(repository, never()).save(any());
    }
}
