CREATE TABLE IF NOT EXISTS `tickets` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `full_price` int DEFAULT NULL,
    `price_with_discount` int DEFAULT NULL,
    `discount_id` bigint DEFAULT NULL,
    `passenger_id` bigint DEFAULT NULL,
    `train_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK1ds262xq345nkvs5o9ptnftwr` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`id`),
    CONSTRAINT `FK3q0cyo2pwci2u05hghem4evs7` FOREIGN KEY (`discount_id`) REFERENCES `dicounts` (`id`),
    CONSTRAINT `FKsdjbjy7xrs49p88m7h0co4wiu` FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`)
    );