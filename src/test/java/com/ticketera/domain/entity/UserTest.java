package com.ticketera.domain.entity;

import com.ticketera.domain.exception.InvalidEmailException;
import com.ticketera.domain.valueobject.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Creates user with default role and current timestamp")
    void createUserWithDefaults() {
        User user = new User(1L, "Jane@Example.COM", "Jane Doe", "$2a$10$abc", null, null);
        assertEquals(1L, user.getId());
        assertEquals("jane@example.com", user.getEmail().value());
        assertEquals("Jane Doe", user.getFullName());
        assertEquals(Role.ROLE_USER, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getEncodedPassword());
    }

    @Test
    @DisplayName("Preserves the ROLE_ADMIN role when supplied")
    void createAdminRolePreserved() {
        User user = new User(null, "admin@x.com", "Admin", "pw", Role.ROLE_ADMIN, null);
        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Throws when full name is blank")
    void blankFullNameThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new User(1L, "jane@x.com", "", "pw", null, null));
    }

    @Test
    @DisplayName("Throws when full name is null")
    void nullFullNameThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new User(1L, "jane@x.com", null, "pw", null, null));
    }

    @Test
    @DisplayName("Throws when password is blank")
    void blankPasswordThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new User(1L, "jane@x.com", "Jane", "  ", null, null));
    }

    @Test
    @DisplayName("Throws when password is null")
    void nullPasswordThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new User(1L, "jane@x.com", "Jane", null, null, null));
    }

    @Test
    @DisplayName("Throws InvalidEmailException for an invalid email")
    void invalidEmailThrows() {
        assertThrows(InvalidEmailException.class,
            () -> new User(1L, "not-an-email", "Jane", "pw", null, null));
    }

    @Test
    @DisplayName("Trims whitespace from the full name")
    void trimFullName() {
        User user = new User(null, "jane@x.com", "  Jane Doe  ", "pw", null, null);
        assertEquals("Jane Doe", user.getFullName());
    }
}