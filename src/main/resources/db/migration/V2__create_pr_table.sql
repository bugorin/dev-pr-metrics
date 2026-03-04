CREATE TABLE `Pr` (
    id BIGINT NOT NULL,
    github_author_id BIGINT NOT NULL,
    github_status ENUM('OPEN','CLOSED') NOT NULL,
    github_created_at DATETIME(6) NOT NULL,
    github_updated_at DATETIME(6) NOT NULL,
    infos JSON NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pr_user_author FOREIGN KEY (github_author_id) REFERENCES `User` (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
