CREATE TABLE `SyncLog` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    type VARCHAR(100) NOT NULL,
    last_sync DATETIME(6) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_sync_log_type (type)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
