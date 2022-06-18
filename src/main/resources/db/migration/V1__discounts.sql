CREATE TABLE IF NOT EXISTS `dicounts`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `discount_value` int DEFAULT NULL,
    `from_age`       int DEFAULT NULL,
    `to_age`         int DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `dicounts` (`discount_value`, `from_age`, `to_age`)
VALUES (100, 0, 6),
       (50, 6, 14),
       (33, 14, 26),
       (0, 26, 65),
       (100, 65, 999);