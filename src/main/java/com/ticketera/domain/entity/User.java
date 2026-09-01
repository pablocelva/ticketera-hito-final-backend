package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import com.ticketera.domain.valueobject.Role;

import java.time.LocalDateTime;

public class User {
    
    private final Long id;
    private final Email email;
    private final String fullName;
    private final String encodedPassword;
    private final Role role;
    private final LocalDateTime createdAt;

    public User(Long id, String email, String fullName, String encodedPassword, Role role, LocalDateTime createdAt) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("User full name cannot be blank");
        }
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("User password cannot be blank");
        }
        this.id = id;
        this.email = new Email(email); 
        this.fullName = fullName.trim();
        this.encodedPassword = encodedPassword;
        this.role = role != null ? role : Role.ROLE_USER;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
