CREATE TABLE `member` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) NOT NULL,
                          `email` varchar(255) NOT NULL,
                          `password` varchar(255) NOT NULL,
                          `university` int NOT NULL,
                          `auth` enum('ROLE_USER','ROLE_ADMIN') NOT NULL,
                          `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`)
);