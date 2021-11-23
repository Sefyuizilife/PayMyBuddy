package com.paymybuddy.paymybuddy.services;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository        userRepository;
    private final BCryptPasswordEncoder encoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder        = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.findByEmail(username)
                   .orElseThrow(() -> new UsernameNotFoundException("Email " + username + " not found"));
    }

    public User create(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public Optional<User> findById(Long id) {

        return this.userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {

        return this.userRepository.findByEmail(email);
    }

}