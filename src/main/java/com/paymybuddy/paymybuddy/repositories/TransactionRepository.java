package com.paymybuddy.paymybuddy.repositories;

import com.paymybuddy.paymybuddy.entities.Transaction;
import com.paymybuddy.paymybuddy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data Access Layer for the {@link Transaction}. It's managed here by the JPA interface.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions related to a donor or beneficiary.
     *
     * @param donor
     *         is the transmitter of the {@link Transaction}.
     * @param beneficiary
     *         is the recipient of {@link Transaction}.
     *
     * @return {@link List} of {@link Transaction}.
     */
    List<Transaction> findAllByDonorOrBeneficiary(User donor, User beneficiary);
}
