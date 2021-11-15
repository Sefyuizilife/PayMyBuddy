START TRANSACTION;

USE pay_my_buddy;

DROP TABLE IF EXISTS `pay_my_buddy`.`transaction`;
DROP TABLE IF EXISTS `pay_my_buddy`.`user_contacts`;
DROP TABLE IF EXISTS `pay_my_buddy`.`user`;

CREATE TABLE `pay_my_buddy`.`user`
(
    `id`              BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `money_available` DOUBLE       NOT NULL,
    `iban`            VARCHAR(34)  NOT NULL,
    `email`           VARCHAR(100) NOT NULL UNIQUE,
    `first_name`      VARCHAR(50)  NOT NULL,
    `last_name`       VARCHAR(50)  NOT NULL,
    `password`        VARCHAR(255) NOT NULL,
    `created_at`      DATETIME     NULL DEFAULT current_timestamp(),
    `updated_at`      DATETIME     NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
);

CREATE TABLE `pay_my_buddy`.`transaction`
(
    `id`             BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `amount`         DOUBLE       NOT NULL,
    `description`    VARCHAR(100) NOT NULL,
    `beneficiary_id` BIGINT       NOT NULL,
    `donor_id`       BIGINT       NOT NULL,
    `created_at`     DATETIME     NULL DEFAULT current_timestamp(),
    CONSTRAINT `fk__donor_id__user__id` FOREIGN KEY (`donor_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk__beneficiary_id__user__id` FOREIGN KEY (`beneficiary_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `pay_my_buddy`.`user_contacts`
(
    `user_id`     BIGINT NOT NULL,
    `contacts_id` BIGINT NOT NULL,
    UNIQUE (`user_id`, `contacts_id`),
    CONSTRAINT `fk__contacts_id__user__id` FOREIGN KEY (`contacts_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk__user_id__user__id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

COMMIT;
