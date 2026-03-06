package com.devprmetrics.domain.sync;

import lombok.Getter;

@Getter
public enum SyncLogOption {
    PR_QUERY(1,12, 30);

    private final int startDaysAgo;
    private final int timeToTryHours;
    private final int delaySeconds;

    SyncLogOption(int startDaysAgo, int timeToTryHours, int delaySeconds) {
        this.startDaysAgo = startDaysAgo;
        this.timeToTryHours = timeToTryHours;
        this.delaySeconds = delaySeconds;
    }
}
