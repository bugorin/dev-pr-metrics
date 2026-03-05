package com.devprmetrics.domain.sync;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {

    Optional<SyncLog> findByType(SyncLogOption type);
}
