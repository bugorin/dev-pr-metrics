CREATE TABLE `TeamMember` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role ENUM('DEV', 'TECH_LEAD', 'PRODUCT') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_team_member_team_user (team_id, user_id),
    KEY idx_team_member_team (team_id),
    KEY idx_team_member_user (user_id),
    CONSTRAINT fk_team_member_team FOREIGN KEY (team_id) REFERENCES `Team` (id),
    CONSTRAINT fk_team_member_user FOREIGN KEY (user_id) REFERENCES `User` (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
