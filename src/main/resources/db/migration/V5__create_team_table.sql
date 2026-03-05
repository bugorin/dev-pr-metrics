CREATE TABLE `Team` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_team_name (name)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
