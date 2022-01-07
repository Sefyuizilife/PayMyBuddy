package com.paymybuddy.paymybuddy.controllers;

import com.paymybuddy.paymybuddy.entities.Transaction;
import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.services.TransactionService;
import com.paymybuddy.paymybuddy.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

/**
 * Controller specifically for managing the {@link Transaction}.
 */
@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final UserService        userService;


    /**
     * Create a new instance of this {@link TransactionController}. This will be done automatically by SpringBoot with
     * dependencies injection.
     *
     * @param transactionService
     *         instance of {@link TransactionService} .
     * @param userService
     *         instance of {@link UserService}
     */
    public TransactionController(TransactionService transactionService, UserService userService) {

        this.transactionService = transactionService;
        this.userService        = userService;
    }

    /**
     * Retrieve a view containing the list of {@link Transaction} for the logged-in {@link User}.
     *
     * @param principal
     *         represents the currently logged-in user.
     * @param model
     *         provides the attributes used by the views.
     * @param alert
     *         optional parameter of type {@link String} to alert the result of a user action.
     * @param pageNumber
     *         optional parameter that defines the page to send.
     *
     * @return a {@link String} that refers to the name of a view
     */
    @GetMapping()
    public String browseByUser(Principal principal, Model model, @RequestParam(required = false) String alert, @RequestParam(required = false) Integer pageNumber) {

        User currentUser = this.userService.findByEmail(principal.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Transaction> transactions = this.transactionService.readAllByUser(currentUser);

        int indexBegin = pageNumber == null || pageNumber == 0 ? 0 : pageNumber * 3 - 3;
        int indexEnd   = Math.min((pageNumber == null || pageNumber == 0 ? 3 : pageNumber * 3), transactions.size());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("beneficiaries", currentUser.getContacts());
        model.addAttribute("transactions", transactions.subList(indexBegin, indexEnd));
        model.addAttribute("totalTransactions", transactions.size());
        model.addAttribute("totalPages", transactions.size() / 3);
        model.addAttribute("alert", alert);

        return "transactions";
    }

    /**
     * Create a {@link Transaction} for the logged-in {@link User}.
     *
     * @param principal
     *         represents the currently logged-in user.
     * @param transaction
     *         represents the transaction to be recorded.
     * @param redirectAttributes
     *         is linked to the {@link Model} interface and adds attributes for the redirection.
     *
     * @return a {@link String} that refers to the name of a view.
     */
    @PostMapping()
    public String create(Principal principal, @ModelAttribute Transaction transaction, RedirectAttributes redirectAttributes) {

        User currentUser = this.userService.findByEmail(principal.getName())
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        transaction.setDonor(currentUser);

        if (!this.validate(transaction, redirectAttributes)) {

            return "redirect:transactions";
        }

        if (this.transactionService.create(transaction) != null) {

            redirectAttributes.addAttribute("alert", "success");
        }

        LOGGER.info(String.format("Invoice - Bank transfer from user %s (sender) to user %s (beneficiary), amounting to %s, the fee is %s.", transaction.getDonor()
                                                                                                                                                        .getId(), transaction.getBeneficiary()
                                                                                                                                                                             .getId(), transaction.getAmount(), transaction.getAmount() * .05));

        return "redirect:transactions";
    }

    private boolean validate(Transaction transaction, RedirectAttributes redirectAttributes) {

        if (transaction.getId() != null) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        if (transaction.getDescription() == null || transaction.getDescription().isBlank()) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        if (transaction.getAmount() <= 0) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        if (transaction.getBeneficiary() == null) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        if (transaction.getDonor() == null) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        if (transaction.getDonor() == transaction.getBeneficiary()) {

            redirectAttributes.addAttribute("alert", "failed");
            return false;
        }

        return true;
    }
}