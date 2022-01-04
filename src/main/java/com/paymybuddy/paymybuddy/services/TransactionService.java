package com.paymybuddy.paymybuddy.services;

import com.paymybuddy.paymybuddy.entities.Transaction;
import com.paymybuddy.paymybuddy.entities.User;
import com.paymybuddy.paymybuddy.repositories.TransactionRepository;
import com.paymybuddy.paymybuddy.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

/**
 * Intended to contain the business code for {@link Transaction}
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository        userRepository;

    /**
     * Create a new instance of this {@link TransactionService}. This will be done automatically by SpringBoot with
     * dependencies injection.
     *
     * @param transactionRepository
     *         instance of {@link TransactionRepository}.
     * @param userRepository
     *         instance of {@link UserRepository}.
     */
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository        = userRepository;
    }

    /**
     * Allows you to create a bank transfer using the transaction to ensure data consistency.
     *
     * @param transaction
     *         represents the banking operation to be created.
     *
     * @return the banking operation created
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public Transaction create(Transaction transaction) throws IOException {

        if (transaction.getId() != null) {
            throw new IOException("Transaction id will be null");
        }

        User donor = this.userRepository.findById(transaction.getDonor().getId())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404)));

        User beneficiary = this.userRepository.findById(transaction.getBeneficiary().getId())
                                              .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404)));

        if (donor.getMoneyAvailable() >= transaction.getAmount()) {

            Double amount = transaction.getAmount();

            donor.setMoneyAvailable(donor.getMoneyAvailable() - amount);

            beneficiary.setMoneyAvailable(beneficiary.getMoneyAvailable() + amount);

            this.userRepository.save(donor);
            this.userRepository.save(beneficiary);
        }

        Transaction save = this.transactionRepository.save(transaction);
        return save;
    }

    /**
     * Returns a {@link List} of {@link Transaction} linked to a {@link User}.
     *
     * @param user
     *         concerned by the history to be returned.
     *
     * @return {@link List} of {@link Transaction}.
     */
    public List<Transaction> readAllByUser(User user) {

        return this.transactionRepository.findAllByDonorOrBeneficiary(user, user);
    }
}
