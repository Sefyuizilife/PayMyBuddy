package com.paymybuddy.paymybuddy.controllers.validator;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {

        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "iban", "iban.empty");

        User user = (User) target;

        if (user.getId() != null) {

            errors.rejectValue("id", "id.error", "The id must be null");
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {

            errors.rejectValue("email", "email.error", "The email has already been used");
        }

        if (user.getFirstName().length() < 3 || user.getFirstName().length() > 30) {

            errors.rejectValue("firstName", "firstName.error", "The number of characters must be between 3 and 30");
        }

        if (user.getLastName().length() < 3 || user.getLastName().length() > 30) {

            errors.rejectValue("lastName", "lastName.error", "The number of characters must be between 3 and 30");
        }

        if (user.getContacts().size() != 0) {

            errors.rejectValue("contact", "contact.error", "The user has abnormal contacts");
        }

        String[] passwords = user.getPassword().split(",");

        if (passwords.length > 2 || !passwords[0].equals(passwords[1])) {

            errors.rejectValue("password", "password.error", "Passwords don't match");
        }

        user.setPassword(passwords[0]);
    }
}
