package com.devprmetrics.domain.sync;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SyncLog")
public class SyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 100)
    private SyncLogOption type;

    @Getter
    @Column(name = "last_sync")
    private LocalDateTime lastSync;

    @Column(name = "last_success")
    private LocalDateTime lastSuccess;

    @Column(name = "last_failure")
    private LocalDateTime lastFailure;

    protected SyncLog() {}

    public SyncLog(SyncLogOption type) {
        this.type = type;
        this.lastSync = LocalDate.now().minusDays(type.getStartDaysAgo()).atStartOfDay();
        this.lastSuccess = LocalDate.now().minusDays(type.getStartDaysAgo()).atStartOfDay();
    }

    public LocalDateTime nextTimeToTry() {
        LocalDateTime next = this.lastSync.plusMinutes(this.type.getTimeToTryMinutes());
        LocalDateTime now = LocalDateTime.now();
        if (next.isAfter(now)) {
            return now.minusSeconds(this.type.getDelaySeconds());
        }
        return next;
    }

    public SyncLog success(LocalDateTime lastSync) {
        if(lastSync == null) throw new IllegalArgumentException("lastSync cannot be null");
        this.lastSync = lastSync;
        this.lastSuccess = LocalDateTime.now();
        return this;
    }

    public SyncLog error() {
        if(lastSync == null) throw new IllegalArgumentException("lastSync cannot be null");
        this.lastSync = LocalDateTime.now();
        this.lastFailure = LocalDateTime.now();
        return this;
    }
}
