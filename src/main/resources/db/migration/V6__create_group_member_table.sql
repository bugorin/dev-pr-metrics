CREATE TABLE `GroupMember` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role ENUM('DEV', 'TECH_LEAD', 'PRODUCT') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_group_member_group_user (group_id, user_id),
    KEY idx_group_member_group (group_id),
    KEY idx_group_member_user (user_id),
    CONSTRAINT fk_group_member_group FOREIGN KEY (group_id) REFERENCES `Group` (id),
    CONSTRAINT fk_group_member_user FOREIGN KEY (user_id) REFERENCES `User` (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
