CREATE TABLE IF NOT EXISTS `dicounts`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `discount_value` int(11) DEFAULT NULL,
    `from_age`       int(11) DEFAULT NULL,
    `to_age`         int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `dicounts` (`discount_value`, `from_age`, `to_age`)
VALUES (100, 0, 6),
       (50, 6, 14),
       (33, 14, 26),
       (0, 26, 65),
       (100, 65, 999);