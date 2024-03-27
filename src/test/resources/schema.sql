DROP TABLE IF EXISTS `university`;
CREATE TABLE `university` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `university_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh2ap9lv99txektaou64wpx8b2` (`university_id`),
  CONSTRAINT `FKh2ap9lv99txektaou64wpx8b2` FOREIGN KEY (`university_id`) REFERENCES `university` (`id`)
);

DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `department_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ipv4` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `server_username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm3dq34w7w85n1q1omwqtykh1i` (`department_id`),
  CONSTRAINT `FKm3dq34w7w85n1q1omwqtykh1i` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `no` int NOT NULL,
  `university_id` int DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `roles` enum('ADMIN','MANAGER','USER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfi832e3qv89fq376fuh8920y4` (`department_id`),
  KEY `FKg6bhc1g69lfy3mquw927rmr9m` (`university_id`),
  CONSTRAINT `FKfi832e3qv89fq376fuh8920y4` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKg6bhc1g69lfy3mquw927rmr9m` FOREIGN KEY (`university_id`) REFERENCES `university` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `lecture`;
CREATE TABLE `lecture` (
  `department_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `server_id` bigint DEFAULT NULL,
  `introducing` varchar(255) DEFAULT NULL,
  `lecture_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe8ya8sfreowqeagfjrql0hl5t` (`department_id`),
  KEY `FKrrsu0fo9ivy8xkw0lwfpi1jt1` (`server_id`),
  CONSTRAINT `FKe8ya8sfreowqeagfjrql0hl5t` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKrrsu0fo9ivy8xkw0lwfpi1jt1` FOREIGN KEY (`server_id`) REFERENCES `server` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `instance`;
CREATE TABLE `instance` (
  `code` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `port` int NOT NULL,
  `storage` double DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL,
  `lecture_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `keyname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `os` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoqgdt3exovpx5h4q0f5n3l62n` (`lecture_id`),
  KEY `FKcl5wy3iep5bdo2a689khevl7v` (`user_id`),
  CONSTRAINT `FKcl5wy3iep5bdo2a689khevl7v` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKoqgdt3exovpx5h4q0f5n3l62n` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `instance_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r2qenaxhhlr854ydsk5k4emvx` (`instance_id`),
  CONSTRAINT `FK3vo45csjxoisocruuvjlnqbyd` FOREIGN KEY (`instance_id`) REFERENCES `instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `inbound_policy`;
CREATE TABLE `inbound_policy` (
  `host_port` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `instance_id` int DEFAULT NULL,
  `instance_port` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6eqfk0seo4agvq1n3cybwjrfe` (`instance_id`),
  CONSTRAINT `FK6eqfk0seo4agvq1n3cybwjrfe` FOREIGN KEY (`instance_id`) REFERENCES `instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `lecture_user`;
CREATE TABLE `lecture_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lecture_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `permit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr62qnmds9nt2ejaxk3lmhc6m1` (`lecture_id`),
  KEY `FKfktvgv1cvx5tskb4sxq6l3e6r` (`user_id`),
  CONSTRAINT `FKfktvgv1cvx5tskb4sxq6l3e6r` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr62qnmds9nt2ejaxk3lmhc6m1` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `auth_num` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `manager_authority`;
CREATE TABLE `manager_authority` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lecture_id` bigint DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_at` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3lwj4k6ae5bnabk98o1b05t7n` (`lecture_id`),
  CONSTRAINT `FK3lwj4k6ae5bnabk98o1b05t7n` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `refresh_token`;
CREATE TABLE `refresh_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f95ixxe7pa48ryn1awmh2evt7` (`user_id`),
  CONSTRAINT `FKjtx87i0jvq2svedphegvdwcuy` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;