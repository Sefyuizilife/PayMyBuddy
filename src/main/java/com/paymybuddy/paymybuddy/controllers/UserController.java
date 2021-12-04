package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {

        this.userService = userService;
    }

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

    @GetMapping("/login")
    public String login() {

        return "login";
    }
}
