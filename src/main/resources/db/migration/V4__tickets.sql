CREATE TABLE IF NOT EXISTS `tickets` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `full_price` int(11) DEFAULT NULL,
    `price_with_discount` int(11) DEFAULT NULL,
    `discount_id` bigint(20) DEFAULT NULL,
    `passenger_id` bigint(20) DEFAULT NULL,
    `train_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK3q0cyo2pwci2u05hghem4evs7` (`discount_id`),
    KEY `FK1ds262xq345nkvs5o9ptnftwr` (`passenger_id`),
    KEY `FKsdjbjy7xrs49p88m7h0co4wiu` (`train_id`),
    CONSTRAINT `FK1ds262xq345nkvs5o9ptnftwr` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`id`),
    CONSTRAINT `FK3q0cyo2pwci2u05hghem4evs7` FOREIGN KEY (`discount_id`) REFERENCES `dicounts` (`id`),
    CONSTRAINT `FKsdjbjy7xrs49p88m7h0co4wiu` FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;