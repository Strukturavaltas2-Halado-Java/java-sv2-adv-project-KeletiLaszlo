CREATE TABLE IF NOT EXISTS `passengers`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `date_of_birth`  date         DEFAULT NULL,
    `passenger_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;