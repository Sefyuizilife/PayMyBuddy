package com.paymybuddy.paymybuddy.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<User> contacts;

    public User() {

    }

    public User(String email, String password) {

        this.email    = email;
        this.password = password;
    }

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

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(""));

        return authorityList;
    }


    @Override
    public String getUsername() {

        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
