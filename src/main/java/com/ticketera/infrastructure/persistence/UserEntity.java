package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.User;
import com.ticketera.domain.valueobject.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "full_name", nullable = false, length = 160)
    private String fullName;

    @Column(name = "encoded_password", nullable = false, length = 255)
    private String encodedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected UserEntity() {
    }

    private UserEntity(Long id, String email, String fullName, String encodedPassword, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.encodedPassword = encodedPassword;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(
            user.getId(),
            user.getEmail().value(),
            user.getFullName(),
            user.getEncodedPassword(),
            user.getRole(),
            user.getCreatedAt());
    }

    public User toDomain() {
        return new User(id, email, fullName, encodedPassword, role, createdAt);
    }
}