package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.controllers.validator.UserValidator;
import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
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
     * Aggregates or subtracts a {@link Double} to a {@link User} according to its {@link User#getMoneyAvailable()}.
     *
     * @param principal
     *         represents the logged-in user.
     * @param redirectAttributes
     *         is linked to the {@link Model} interface and adds attributes for the redirection.
     * @param addMoney
     *         is the amount to be added to the
     * @param withdrawMoney
     *         is the amount to be withdrawn from the account
     *
     * @return a {@link String} that refers to the name of a view.
     */
    @PostMapping("money")
    public String getMoneyAvailable(Principal principal, RedirectAttributes redirectAttributes, @RequestParam(required = false) Double addMoney, @RequestParam(required = false) Double withdrawMoney) {

        User currentUser = this.userService.findByEmail(principal.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Double moneyAvailable = currentUser.getMoneyAvailable();

        addMoney      = addMoney == null ? 0.00 : addMoney;
        withdrawMoney = withdrawMoney == null ? 0.00 : withdrawMoney;

        if (addMoney > 0) {

            currentUser.setMoneyAvailable(moneyAvailable + addMoney);
            this.userService.save(currentUser);
            redirectAttributes.addAttribute("alert", "success");

        } else if (withdrawMoney > 0) {

            if (withdrawMoney <= moneyAvailable) {
                currentUser.setMoneyAvailable(moneyAvailable - withdrawMoney);
                this.userService.save(currentUser);
                redirectAttributes.addAttribute("alert", "success");
            } else {

                redirectAttributes.addAttribute("alert", "failed");
            }
        } else {

            redirectAttributes.addAttribute("alert", "failed");
        }

        return "redirect:transactions";
    }

    @GetMapping("/signup")
    public String signup(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user, BindingResult result) {

        this.validate(user, result);

        if (result.hasErrors()) {

            return "signup";
        }

        user.setMoneyAvailable(0.0);
        userService.save(user);

        return "redirect:/login";
    }

    public void validate(User user, Errors errors) {

        new UserValidator(this.userService).validate(user, errors);
    }
}
