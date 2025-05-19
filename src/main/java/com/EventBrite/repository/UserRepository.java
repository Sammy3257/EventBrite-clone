package com.EventBrite.repository;

import com.EventBrite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // required for login
    boolean existsByEmail(String email);      // required for duplicate check
}
