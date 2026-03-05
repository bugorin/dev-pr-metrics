CREATE TABLE `jobrunr_jobs` (
    `id` CHAR(36) NOT NULL,
    `version` INT NOT NULL,
    `jobAsJson` MEDIUMTEXT NULL,
    `jobSignature` VARCHAR(512) NOT NULL,
    `state` VARCHAR(36) NOT NULL,
    `createdAt` DATETIME(6) NOT NULL,
    `updatedAt` DATETIME(6) NOT NULL,
    `scheduledAt` DATETIME(6) NULL,
    `recurringJobId` VARCHAR(128) NULL,
    PRIMARY KEY (`id`),
    KEY `jobrunr_state_idx` (`state`),
    KEY `jobrunr_job_signature_idx` (`jobSignature`),
    KEY `jobrunr_job_created_at_idx` (`createdAt`),
    KEY `jobrunr_job_scheduled_at_idx` (`scheduledAt`),
    KEY `jobrunr_job_rci_idx` (`recurringJobId`),
    KEY `jobrunr_jobs_state_updated_idx` (`state`, `updatedAt`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `jobrunr_recurring_jobs` (
    `id` CHAR(128) NOT NULL,
    `version` INT NOT NULL,
    `jobAsJson` TEXT NOT NULL,
    `createdAt` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `jobrunr_recurring_job_created_at_idx` (`createdAt`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `jobrunr_backgroundjobservers` (
    `id` CHAR(36) NOT NULL,
    `workerPoolSize` INT NOT NULL,
    `pollIntervalInSeconds` INT NOT NULL,
    `firstHeartbeat` DATETIME(6) NOT NULL,
    `lastHeartbeat` DATETIME(6) NOT NULL,
    `running` INT NOT NULL,
    `systemTotalMemory` BIGINT NOT NULL,
    `systemFreeMemory` BIGINT NOT NULL,
    `systemCpuLoad` DECIMAL(3, 2) NOT NULL,
    `processMaxMemory` BIGINT NOT NULL,
    `processFreeMemory` BIGINT NOT NULL,
    `processAllocatedMemory` BIGINT NOT NULL,
    `processCpuLoad` DECIMAL(3, 2) NOT NULL,
    `deleteSucceededJobsAfter` VARCHAR(32) NULL,
    `permanentlyDeleteJobsAfter` VARCHAR(32) NULL,
    `name` VARCHAR(128) NULL,
    PRIMARY KEY (`id`),
    KEY `jobrunr_bgjobsrvrs_fsthb_idx` (`firstHeartbeat`),
    KEY `jobrunr_bgjobsrvrs_lsthb_idx` (`lastHeartbeat`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `jobrunr_metadata` (
    `id` VARCHAR(156) NOT NULL,
    `name` VARCHAR(92) NOT NULL,
    `owner` VARCHAR(64) NOT NULL,
    `value` TEXT NOT NULL,
    `createdAt` DATETIME(6) NOT NULL,
    `updatedAt` DATETIME(6) NOT NULL,
    PRIMARY KEY (`id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `jobrunr_migrations` (
    `id` CHAR(36) NOT NULL,
    `script` VARCHAR(64) NOT NULL,
    `installedOn` VARCHAR(29) NOT NULL,
    PRIMARY KEY (`id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE VIEW `jobrunr_jobs_stats` AS
WITH job_stat_results AS (
    SELECT state, COUNT(0) AS count
    FROM jobrunr_jobs
    GROUP BY state
)
SELECT
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results), 0) AS total,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'AWAITING'), 0) AS awaiting,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'SCHEDULED'), 0) AS scheduled,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'ENQUEUED'), 0) AS enqueued,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'PROCESSING'), 0) AS processing,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'PROCESSED'), 0) AS processed,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'FAILED'), 0) AS failed,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'SUCCEEDED'), 0) AS succeeded,
    COALESCE((SELECT CAST(CAST(jm.value AS CHAR(10)) AS DECIMAL(10, 0))
              FROM jobrunr_metadata jm
              WHERE jm.id = 'succeeded-jobs-counter-cluster'), 0) AS allTimeSucceeded,
    COALESCE((SELECT SUM(job_stat_results.count) FROM job_stat_results WHERE job_stat_results.state = 'DELETED'), 0) AS deleted,
    (SELECT COUNT(0) FROM jobrunr_backgroundjobservers) AS nbrOfBackgroundJobServers,
    (SELECT COUNT(0) FROM jobrunr_recurring_jobs) AS nbrOfRecurringJobs;
