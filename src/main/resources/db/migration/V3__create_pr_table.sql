CREATE TABLE `Pr` (
    id BIGINT NOT NULL,
    github_author_id BIGINT NOT NULL,
    github_repository_id BIGINT NULL,
    github_status ENUM('OPEN','CLOSED') NOT NULL,
    github_created_at DATETIME(6) NOT NULL,
    github_updated_at DATETIME(6) NOT NULL,
    infos JSON NULL,
    PRIMARY KEY (id),
    KEY idx_pr_github_repository_id (github_repository_id),
    CONSTRAINT fk_pr_user_author FOREIGN KEY (github_author_id) REFERENCES `User` (id),
    CONSTRAINT fk_pr_github_repository FOREIGN KEY (github_repository_id) REFERENCES `Repo` (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
