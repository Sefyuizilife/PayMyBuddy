package com.paymybuddy.paymybuddy.repositories;

import com.paymybuddy.paymybuddy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data Access Layer for the {@link User}. It's managed here by the JPA interface.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email
     *
     * @param email
     *         used to find a {@link User}
     *
     * @return a {@link Optional} {@link User}
     */
    Optional<User> findByEmail(String email);
}
