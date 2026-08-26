package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateCityUseCaseTest {

    private CityRepository repository;
    private CreateCityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new CreateCityUseCase(repository);
    }

    @Test
    @DisplayName("Creates city successfully")
    void createsCitySuccessfully() {
        City result = useCase.execute("LIM", "Lima");

        assertThat(result.getCode()).isEqualTo("LIM");
        assertThat(result.getName()).isEqualTo("Lima");
        verify(repository).save(any(City.class));
    }

    @Test
    @DisplayName("Throws when code is null")
    void throwsWhenCodeIsNull() {
        assertThatThrownBy(() -> useCase.execute(null, "Lima"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City code is required");
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        assertThatThrownBy(() -> useCase.execute("LIM", "  "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("City name is required");
    }
}
