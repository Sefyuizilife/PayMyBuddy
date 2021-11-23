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

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository        userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository        = userRepository;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Transaction create(Transaction transaction) throws IOException {

        if (transaction.getId() == null) {

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

            return this.transactionRepository.save(transaction);
        }

        throw new IOException("Transaction id will be null");
    }

    public List<Transaction> readAllByUser(User user) {

        return this.transactionRepository.findAllByDonorOrBeneficiary(user, user);
    }
}
