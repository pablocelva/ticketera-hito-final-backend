package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Customer")
class CustomerTest {

    @Test
    @DisplayName("Creates customer with valid data")
    void createsCustomerWithValidData() {
        Email email = new Email("pablo@example.com");
        Customer customer = new Customer("CUS-001", "Pablo", email);

        assertThat(customer.getId()).isEqualTo("CUS-001");
        assertThat(customer.getName()).isEqualTo("Pablo");
        assertThat(customer.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Throws when id is null")
    void throwsWhenIdIsNull() {
        assertThatThrownBy(() -> new Customer(null, "Pablo", new Email("pablo@example.com")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer ID cannot be blank");
    }

    @Test
    @DisplayName("Throws when id is blank")
    void throwsWhenIdIsBlank() {
        assertThatThrownBy(() -> new Customer("", "Pablo", new Email("pablo@example.com")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer ID cannot be blank");
    }

    @Test
    @DisplayName("Throws when name is null")
    void throwsWhenNameIsNull() {
        assertThatThrownBy(() -> new Customer("CUS-001", null, new Email("pablo@example.com")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer name cannot be blank");
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        assertThatThrownBy(() -> new Customer("CUS-001", "  ", new Email("pablo@example.com")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Customer name cannot be blank");
    }
}
