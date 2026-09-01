package com.ticketera.domain.repository;

import com.ticketera.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);
}
