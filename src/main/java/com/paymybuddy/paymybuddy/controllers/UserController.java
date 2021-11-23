package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("signup")
    public String signup() {

        return "signup";
    }

    @PostMapping("users/{id}/links")
    public String addConnexions(@ModelAttribute String email, @PathVariable Long id, Model model) {

        User user = this.userService.findById(id).orElseThrow();

        Optional<User> contact = this.userService.findByEmail(email);

        if (contact.isPresent()) {

            model.addAttribute("alert", "success");

        } else {

            model.addAttribute("alert, warning");

        }

        return "transactions";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }
}
