CREATE TABLE `Reviewer` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_pr BIGINT NOT NULL,
    github_user_id BIGINT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'CHANGES_REQUESTED', 'REQUEST_CHANGES', 'COMMENTED', 'DISMISSED') NOT NULL,
    submitted_at DATETIME(6) NOT NULL,
    infos JSON NULL,
    PRIMARY KEY (id),
    KEY idx_reviewer_id_pr (id_pr),
    KEY idx_reviewer_github_user_id (github_user_id),
    CONSTRAINT fk_reviewer_pr FOREIGN KEY (id_pr) REFERENCES `Pr` (id),
    CONSTRAINT fk_reviewer_user FOREIGN KEY (github_user_id) REFERENCES `User` (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
