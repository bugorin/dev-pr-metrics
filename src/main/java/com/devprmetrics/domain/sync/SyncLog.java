package com.devprmetrics.domain.sync;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "SyncLog")
public class SyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 100)
    private SyncLogOption type;

    @Column(name = "last_sync")
    private LocalDateTime lastSync;

    protected SyncLog() {}

    public SyncLog(SyncLogOption type) {
        this.type = type;
        this.lastSync = LocalDate.now().minusDays(type.getStartDaysAgo()).atStartOfDay();
    }

    public LocalDateTime nextTimeToTry() {
        LocalDateTime next = this.lastSync.plusHours(this.type.getTimeToTryHours());
        LocalDateTime now = LocalDateTime.now();
        if (next.isAfter(now)) {
            return now.minusSeconds(this.type.getDelaySeconds());
        }
        return next;
    }
}
