INSERT INTO `pay_my_buddy`.`user` (id, iban, email, first_name, last_name, money_available, password, created_at,
                                   updated_at)
VALUES (1, 'FR30001019010000Z67067031', 'alex@email.valid', 'Alex', 'Xela', 50,
        '$2a$10$cfNmqMhnzU1/FztEeodiqeQbmp3ArajI/kAt6KNXgzgPh0xXQ39Qi', now(), now()),
       (2, 'FR30001019010000Z67067032', 'nico@email.valid', 'Nicolas', 'Ocin', 10000,
        '$2a$10$cfNmqMhnzU1/FztEeodiqeQbmp3ArajI/kAt6KNXgzgPh0xXQ39Qi', now(), now()),
       (3, 'FR30001019010000Z67067033', 'mika@email.valid', 'Mickael', 'Akcim', 5000,
        '$2a$10$cfNmqMhnzU1/FztEeodiqeQbmp3ArajI/kAt6KNXgzgPh0xXQ39Qi', now(), now());

INSERT INTO `pay_my_buddy`.`user_contacts` (user_id, contacts_id)
VALUES (1, 2),
       (1, 3);

INSERT INTO `pay_my_buddy`.`transaction` (id, amount, created_at, description, beneficiary_id, donor_id)
VALUES (1, 150.00, now(), 'My credit transfert', 2, 1)