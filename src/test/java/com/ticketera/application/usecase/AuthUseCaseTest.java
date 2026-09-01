package com.ticketera.application.usecase;

import com.ticketera.domain.entity.User;
import com.ticketera.domain.exception.UserAlreadyExistsException;
import com.ticketera.domain.repository.UserRepository;
import com.ticketera.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    private UserRepository userRepository;
    private AuthUseCase authUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        Function<String, String> encode = password -> "encoded:" + password;
        authUseCase = new AuthUseCase(userRepository, encode);
    }

    @Test
    @DisplayName("Register creates a ROLE_USER user and persists it")
    void registerCreatesUser() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authUseCase.register("Jane@Example.com", "Jane Doe", "secret");
        assertThat(result.getEmail().value()).isEqualTo("jane@example.com");
        assertThat(result.getFullName()).isEqualTo("Jane Doe");
        assertThat(result.getEncodedPassword()).isEqualTo("encoded:secret");
        assertThat(result.getRole()).isEqualTo(Role.ROLE_USER);

        verify(userRepository).existsByEmail("jane@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register throws UserAlreadyExistsException when email exists")
    void registerDuplicateEmailThrows() {
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authUseCase.register("jane@example.com", "Jane", "secret"))
            .isInstanceOf(UserAlreadyExistsException.class)
            .hasMessageContaining("jane@example.com");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("findByEmail returns the user when present")
    void findByEmailReturnsUser() {
        User existing = new User(1L, "jane@example.com", "Jane", "pw", Role.ROLE_USER, null);
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(existing));

        User result = authUseCase.findByEmail("jane@example.com");
        assertThat(result).isSameAs(existing);
    }

    @Test
    @DisplayName("findByEmail throws IllegalArgumentException when absent")
    void findByEmailMissingThrows() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authUseCase.findByEmail("ghost@example.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ghost@example.com");
    }
}
