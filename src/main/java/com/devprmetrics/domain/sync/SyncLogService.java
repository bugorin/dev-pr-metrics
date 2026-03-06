package com.devprmetrics.domain.sync;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SyncLogService {

    private final SyncLogRepository syncLogRepository;

    @Transactional
    public void execute(
            SyncLogOption type,
            SyncExecution action
    ) {
        SyncLog log = findOrCreate(type);
        LocalDateTime start = log.getLastSync();
        LocalDateTime end = log.nextTimeToTry();

        try {
            action.accept(start, end);
            syncLogRepository.save(log.success(end));
        } catch (Exception exception) {
            syncLogRepository.save(log.error());
            throw new RuntimeException("Error executing sync action for type " + type, exception);
        }
    }

    private SyncLog findOrCreate(SyncLogOption type) {
        return syncLogRepository.findByType(type)
                .orElseGet(() -> syncLogRepository.save(new SyncLog(type)));
    }

    @FunctionalInterface
    public interface SyncExecution {
        void accept(LocalDateTime start, LocalDateTime end) throws Exception;
    }
}
