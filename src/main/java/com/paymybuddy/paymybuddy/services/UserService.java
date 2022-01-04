package com.paymybuddy.paymybuddy.services;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Intended to contain the business code for {@link User}. It implements the {@link UserDetailsService} interface.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository        userRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * Create a new instance of this {@link UserService}. This will be done automatically by SpringBoot with
     * dependencies injection.
     *
     * @param userRepository
     *         instance of {@link UserRepository}.
     * @param encoder
     *         instance of {@link BCryptPasswordEncoder}.
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder        = encoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.findByEmail(username)
                   .orElseThrow(() -> new UsernameNotFoundException("Email " + username + " not found"));
    }

    /**
     * Requests the database registration of a {@link User}.
     *
     * @param user
     *         to be registered in database
     *
     * @return the registered user.
     */
    public User save(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    /**
     * Search for a user by an id.
     *
     * @param id
     *         internal identifier of a {@link User}
     *
     * @return an {@link Optional} {@link User}
     */
    public Optional<User> findById(Long id) {

        return this.userRepository.findById(id);
    }

    /**
     * Search for a user by an id.
     *
     * @param email
     *         used to find a {@link User}
     *
     * @return an {@link Optional} {@link User}
     */
    public Optional<User> findByEmail(String email) {

        return this.userRepository.findByEmail(email);
    }

    /**
     * Link a user as a contact via their email address
     *
     * @param email
     *         of the contact to be linked
     * @param user
     *
     * @return who is assigned the contact
     */
    public boolean addContact(String email, User user) {

        Optional<User> contact = this.userRepository.findByEmail(email);

        if (contact.isPresent()) {

            List<User> contacts = user.getContacts();
            contacts.add(contact.get());
            this.userRepository.save(user);

            return true;
        }

        return false;
    }
}