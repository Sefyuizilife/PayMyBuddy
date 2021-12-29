package com.paymybuddy.paymybuddy.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements {@link UserDetails}, so it can connect to Spring Security, this {@link User} can have contacts
 * and also be one.
 */
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;
    private String        lastName;
    private String        firstName;
    private String        email;
    private String        password;
    private String        IBAN;
    private Double        moneyAvailable;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany
    private List<User> contacts = new ArrayList<>();

    public User() {

    }

    /**
     * Retrieves the full name of a {@link User}.
     *
     * @return a {@link String}
     */
    public String getFullName() {

        return String.format("%s %s", this.lastName, this.firstName);
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastname) {

        this.lastName = lastname;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstname) {

        this.firstName = firstname;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getIBAN() {

        return IBAN;
    }

    public void setIBAN(String IBAN) {

        this.IBAN = IBAN;
    }

    public Double getMoneyAvailable() {

        return moneyAvailable;
    }

    public void setMoneyAvailable(Double moneyAvailable) {

        this.moneyAvailable = moneyAvailable;
    }

    public List<User> getContacts() {

        return contacts;
    }

    public void setContacts(List<User> contacts) {

        this.contacts = contacts;
    }

    public LocalDateTime getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {

        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {

        this.updatedAt = updatedAt;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;

    }

    /**
     * Retrieve static permissions from user.
     *
     * @return a {@link List} of {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.getRoleName()));
        return authorities;
    }

    /**
     * Retrieve the role name of {@link User}
     *
     * @return a {@link String}
     */
    public String getRoleName() {

        return "USER";
    }

    /**
     * Retrieve User Information
     *
     * @return a {@link User#email}
     */
    @Override
    public String getUsername() {

        return this.email;
    }


    /**
     * Spring Security features that must be implemented but are not used for this project.
     *
     * @return static {@link Boolean} with always a true value
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * Spring Security features that must be implemented but are not used for this project.
     *
     * @return static {@link Boolean} with always a true value
     */
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    /**
     * Spring Security features that must be implemented but are not used for this project.
     *
     * @return static {@link Boolean} with always a true value
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * Spring Security features that must be implemented but are not used for this project.
     *
     * @return static {@link Boolean} with always a true value
     */
    @Override
    public boolean isEnabled() {

        return true;
    }
}
