CREATE TABLE `User` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    github_id BIGINT NOT NULL,
    role ENUM('REGULAR', 'ADMIN') NOT NULL DEFAULT 'REGULAR',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_github_id (github_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
