package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.Transaction;
import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.TransactionService;
import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
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
    public String browseByUser(Principal principal, Model model, @RequestParam(required = false) String alert, @RequestParam(required = false) Integer page) {

        User currentUser = this.userService.findByEmail(principal.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Transaction> transactions = this.transactionService.readAllByUser(currentUser);

        int indexBegin = page == null || page == 0 ? 0 : page * 3 - 3;
        int indexEnd   = Math.min((page == null || page == 0 ? 3 : page * 3), transactions.size());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("beneficiaries", currentUser.getContacts());
        model.addAttribute("transactions", transactions.subList(indexBegin, indexEnd));
        model.addAttribute("totalTransactions", transactions.size());
        model.addAttribute("totalPages", transactions.size() / 3);
        model.addAttribute("alert", alert);

        return "transactions";
    }

    @PostMapping()
    public String create(Principal principal, @ModelAttribute Transaction transaction, RedirectAttributes redirectAttributes) throws IOException {

        User currentUser = this.userService.findByEmail(principal.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        transaction.setDonor(currentUser);

        this.transactionService.create(transaction);

        redirectAttributes.addAttribute("alert", "success");

        return "redirect:transactions";
    }
}
