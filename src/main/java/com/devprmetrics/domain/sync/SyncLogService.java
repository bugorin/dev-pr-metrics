package com.devprmetrics.domain.sync;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SyncLogService {

    private final SyncLogRepository syncLogRepository;

    @Transactional
    public SyncLog findOrCreate(SyncLogOption type) {
        return syncLogRepository.findByType(type)
                .orElseGet(() -> syncLogRepository.save(new SyncLog(type)));
    }
}
