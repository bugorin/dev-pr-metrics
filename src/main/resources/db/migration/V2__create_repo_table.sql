CREATE TABLE `Repo` (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    html_url VARCHAR(500) NULL,
    is_private TINYINT(1) NOT NULL DEFAULT 0,
    default_branch VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
