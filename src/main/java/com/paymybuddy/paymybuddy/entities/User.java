package com.paymybuddy.paymybuddy.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private String IBAN;
    private Double moneyAvailable;

    @ManyToMany
    private List<User> beneficiaries;

    public User() {

    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getLastname() {

        return lastname;
    }

    public void setLastname(String lastname) {

        this.lastname = lastname;
    }

    public String getFirstname() {

        return firstname;
    }

    public void setFirstname(String firstname) {

        this.firstname = firstname;
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

    public List<User> getBeneficiaries() {

        return beneficiaries;
    }

    public void setBeneficiaries(List<User> beneficiaries) {

        this.beneficiaries = beneficiaries;
    }
}
