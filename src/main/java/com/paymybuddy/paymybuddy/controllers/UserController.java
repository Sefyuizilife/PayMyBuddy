package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * Controller specifically for managing the {@link User}.
 */
@Controller
public class UserController {

    private final UserService userService;

    /**
     * /** Create a new instance of this {@link TransactionController}. This will be done automatically by SpringBoot
     * with dependencies injection.
     *
     * @param userService
     *         an instance of {@link UserService}.
     */
    UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Allows you to associate a contact with the logged-in user.
     *
     * @param principal
     *         represents the logged-in user.
     * @param email
     *         represents the contact to be associated.
     * @param redirectAttributes
     *         is linked to the {@link Model} interface and adds attributes for the redirection.
     *
     * @return a {@link String} that refers to the name of a view.
     */
    @PostMapping("connect")
    public String connect(Principal principal, String email, RedirectAttributes redirectAttributes) {

        User currentUser = this.userService.findByEmail(principal.getName()).orElseThrow();

        if (this.userService.addContact(email, currentUser)) {

            redirectAttributes.addAttribute("alert", "success");

        } else {

            redirectAttributes.addAttribute("alert", "failed");
        }

        return "redirect:transactions";
    }

    /**
     * Shows the login page
     *
     * @return a {@link String} that refers to the name of a view.
     */
    @GetMapping("/login")
    public String login() {

        return "login";
    }
}
