package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.Transaction;
import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.TransactionService;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService        userService;

    public TransactionController(TransactionService transactionService, UserService userService) {

        this.transactionService = transactionService;
        this.userService        = userService;
    }

    @GetMapping()
    public String browseByUser(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = this.userService.findByEmail(auth.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Transaction> transactions = this.transactionService.readAllByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("beneficiaries", currentUser.getContacts());
        model.addAttribute("transactions", transactions);

        return "transactions";
    }

    @PostMapping()
    public String create(Authentication auth, @ModelAttribute Transaction transaction, Model model) throws IOException {


        this.transactionService.create(transaction);

        model.addAttribute("alert", "success");
        return "redirect:/transactions";
    }
}
