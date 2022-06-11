CREATE TABLE IF NOT EXISTS `trains` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `arrival_place` varchar(255) DEFAULT NULL,
    `arrival_time` datetime(6) DEFAULT NULL,
    `departure_place` varchar(255) DEFAULT NULL,
    `departure_time` datetime(6) DEFAULT NULL,
    `distance` int(11) NOT NULL,
    `name_of_train` varchar(255) DEFAULT NULL,
    `train_type` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;