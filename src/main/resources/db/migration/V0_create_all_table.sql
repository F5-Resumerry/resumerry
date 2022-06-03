CREATE TABLE IF NOT EXISTS `confirmation_token` (
                                      `id` varchar(50) NOT NULL,
                                      `certification` bit(1) NOT NULL,
                                      `create_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      `expiration_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      `expired` bit(1) NOT NULL,
                                      `last_modified_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      `receiver_email` varchar(255) NOT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `hashtag` (
                           `hashtag_id` bigint NOT NULL AUTO_INCREMENT,
                           `hashtag_name` varchar(255) NOT NULL,
                           PRIMARY KEY (`hashtag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `member` (
                          `member_id` bigint NOT NULL AUTO_INCREMENT,
                          `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `account_name` varchar(255) NOT NULL,
                          `email` varchar(255) NOT NULL,
                          `introduce` varchar(255) DEFAULT NULL,
                          `is_working` varchar(50) NOT NULL DEFAULT 'N',
                          `nickname` varchar(255) NOT NULL,
                          `password` varchar(255) NOT NULL,
                          `role` varchar(255) NOT NULL,
                          `years` int NOT NULL DEFAULT 0,
                          `member_info_id` bigint NOT NULL,
                          `category` varchar(255) NOT NULL DEFAULT 'ALL',
                          `image_src` varchar(255) DEFAULT NULL,
                          `salt` varchar(255) NOT NULL,
                          PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `member_info` (
                               `member_info_id` bigint NOT NULL AUTO_INCREMENT,
                               `email_verified` varchar(255) DEFAULT NULL,
                               `receive_advertisement` varchar(255) DEFAULT NULL,
                               `stack` int NOT NULL DEFAULT 0,
                               `token` int NOT NULL DEFAULT 0,
                               PRIMARY KEY (`member_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `orders` (
                          `orders_id` bigint NOT NULL AUTO_INCREMENT,
                          `amount` bigint NOT NULL,
                          `card_company` varchar(255) DEFAULT NULL,
                          `card_number` varchar(255) DEFAULT NULL,
                          `card_receipt_url` varchar(255) DEFAULT NULL,
                          `client_email` varchar(255) NOT NULL,
                          `client_name` varchar(255) NOT NULL,
                          `created_date` datetime(6) NOT NULL,
                          `order_id` varchar(255) NOT NULL,
                          `order_name` varchar(255) NOT NULL,
                          `pay_fail_reason` varchar(255) DEFAULT NULL,
                          `pay_success_yn` varchar(255) NOT NULL,
                          `pay_type` varchar(255) NOT NULL,
                          `payment_key` varchar(255) DEFAULT NULL,
                          `client_member_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`orders_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `post` (
                        `post_id` bigint NOT NULL AUTO_INCREMENT,
                        `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `category` varchar(255) NOT NULL,
                        `contents` varchar(255) NOT NULL,
                        `is_anonymous` varchar(50) NOT NULL,
                        `title` varchar(255) NOT NULL,
                        `view_cnt` int NOT NULL DEFAULT 0,
                        `member_id` bigint NOT NULL,
                        `resume_id` bigint DEFAULT NULL,
                        `is_delete` varchar(50) NOT NULL DEFAULT 'N',
                        PRIMARY KEY (`post_id`),
                        CONSTRAINT `FK_POST_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `post_comment` (
                                `post_comment_id` bigint NOT NULL AUTO_INCREMENT,
                                `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `contents` varchar(255) NOT NULL DEFAULT '',
                                `is_anonymous` varchar(50) NOT NULL DEFAULT 'N',
                                `post_comment_depth` int NOT NULL DEFAULT 0,
                                `post_comment_group` int NOT NULL DEFAULT 0,
                                `member_id` bigint NOT NULL,
                                `post_id` bigint NOT NULL,
                                `is_delete` varchar(50) NOT NULL DEFAULT 'N',
                                PRIMARY KEY (`post_comment_id`),
                                CONSTRAINT `FK_POSTCOMMENT_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `post_comment_recommend` (
                                          `post_comment_recommend_id` bigint NOT NULL AUTO_INCREMENT,
                                          `member_id` bigint NOT NULL,
                                          `post_comment_id` bigint NOT NULL,
                                          PRIMARY KEY (`post_comment_recommend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `post_comment_report` (
                                       `post_comment_report_id` bigint NOT NULL AUTO_INCREMENT,
                                       `member_id` bigint NOT NULL,
                                       `post_comment_id` bigint NOT NULL,
                                       PRIMARY KEY (`post_comment_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume` (
                          `resume_id` bigint NOT NULL AUTO_INCREMENT,
                          `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `category` varchar(50) NOT NULL DEFAULT 'ALL',
                          `contents` varchar(255) NOT NULL DEFAULT '',
                          `file_link` varchar(255) NOT NULL DEFAULT '',
                          `title` varchar(255) NOT NULL DEFAULT '',
                          `view_cnt` int DEFAULT 0,
                          `years` int NOT NULL DEFAULT 0,
                          `member_id` bigint NOT NULL,
                          `is_delete` varchar(50) NOT NULL DEFAULT 'N',
                          PRIMARY KEY (`resume_id`),
                          CONSTRAINT `FK_RESUME_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_comment` (
                                  `resume_comment_id` bigint NOT NULL AUTO_INCREMENT,
                                  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `contents` varchar(255) NOT NULL DEFAULT '',
                                  `is_anonymous` varchar(50) NOT NULL DEFAULT 'N',
                                  `resume_comment_depth` int NOT NULL DEFAULT 0,
                                  `resume_comment_group` int NOT NULL DEFAULT 0,
                                  `member_id` bigint NOT NULL,
                                  `resume_id` bigint NOT NULL,
                                  `is_delete` varchar(50) NOT NULL DEFAULT 'N',
                                  PRIMARY KEY (`resume_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_comment_recommend` (
                                            `resume_comment_recommend_id` bigint NOT NULL AUTO_INCREMENT,
                                            `member_id` bigint NOT NULL,
                                            `resume_comment_id` bigint NOT NULL,
                                            PRIMARY KEY (`resume_comment_recommend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_comment_report` (
                                         `resume_comment_report_id` bigint NOT NULL AUTO_INCREMENT,
                                         `member_id` bigint NOT NULL,
                                         `resume_comment_id` bigint NOT NULL,
                                         PRIMARY KEY (`resume_comment_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_hashtag` (
                                  `resume_hashtag_id` bigint NOT NULL AUTO_INCREMENT,
                                  `hashtag_id` bigint NOT NULL,
                                  `resume_id` bigint NOT NULL,
                                  PRIMARY KEY (`resume_hashtag_id`),
                                  CONSTRAINT `FK_RESUMEHASHTAG_HASHTAG` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`hashtag_id`),
                                  CONSTRAINT `FK_RESUMEHASHTAG_RESUME` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_recommend` (
                                    `resume_recommend_id` bigint NOT NULL AUTO_INCREMENT,
                                    `member_id` bigint NOT NULL,
                                    `resume_id` bigint NOT NULL,
                                    PRIMARY KEY (`resume_recommend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;

CREATE TABLE IF NOT EXISTS `resume_scrap` (
                                `resume_scrap_id` bigint NOT NULL AUTO_INCREMENT,
                                `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `member_id` bigint NOT NULL,
                                `resume_id` bigint NOT NULL,
                                PRIMARY KEY (`resume_scrap_id`),
                                CONSTRAINT `FK_RESUMESCRAP_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
                                CONSTRAINT `FK_RESUMESCRAP_MEMBER` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;