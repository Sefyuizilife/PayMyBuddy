INSERT INTO `user` (id, iban, email, first_name, last_name, money_available, password, created_at,
                  updated_at)
VALUES (1, 'FR30001019010000Z67067031', 'alex@email.valid', 'Alex', 'Xela', 50,
        'password', null, null);
INSERT INTO `user` (id, iban, email, first_name, last_name, money_available, password, created_at,
                  updated_at)
VALUES (2, 'FR30001019010000Z67067032', 'nico@email.valid', 'Nicolas', 'Ocin', 10000,
        'password', null, null);
INSERT INTO `user` (id, iban, email, first_name, last_name, money_available, password, created_at,
                  updated_at)
VALUES (3, 'FR30001019010000Z67067033', 'mika@email.valid', 'Mickael', 'Akcim', 5000,
        'password', null, null);

INSERT INTO `user_contacts` (user_id, contacts_id)
VALUES (1, 2);
INSERT INTO `user_contacts` (user_id, contacts_id)
VALUES (1, 3);