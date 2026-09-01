package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.User;
import com.ticketera.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email.toLowerCase().trim()).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email.toLowerCase().trim());
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(UserEntity.fromDomain(user)).toDomain();
    }
}