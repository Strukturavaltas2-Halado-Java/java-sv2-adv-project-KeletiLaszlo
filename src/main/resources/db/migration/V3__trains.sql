CREATE TABLE IF NOT EXISTS `trains` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `arrival_place` varchar(255) DEFAULT NULL,
    `arrival_time` datetime DEFAULT NULL,
    `departure_place` varchar(255) DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `distance` int NOT NULL,
    `name_of_train` varchar(255) DEFAULT NULL,
    `train_type` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);