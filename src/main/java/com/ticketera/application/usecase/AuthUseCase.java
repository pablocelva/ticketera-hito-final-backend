package com.ticketera.application.usecase;

import com.ticketera.domain.entity.User;
import com.ticketera.domain.exception.UserAlreadyExistsException;
import com.ticketera.domain.repository.UserRepository;
import com.ticketera.domain.valueobject.Role;

import java.util.function.Function;

public class AuthUseCase {

    private final UserRepository userRepository;
    private final Function<String, String> encode;

    public AuthUseCase(UserRepository userRepository, Function<String, String> encode) {
        this.userRepository = userRepository;
        this.encode = encode;
    }

    public User register(String email, String fullName, String password) {
        String normalizedEmail = email.toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(
                "A user with email '" + normalizedEmail + "' already exists");
        }
        User user = new User(null, normalizedEmail, fullName, encode.apply(password), Role.ROLE_USER, null);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}
