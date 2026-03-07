package com.devprmetrics.domain.sync;

import lombok.Getter;

@Getter
public enum SyncLogOption {
    PR_QUERY(30,360, 30);

    private final int startDaysAgo;
    private final int timeToTryMinutes;
    private final int delaySeconds;

    SyncLogOption(int startDaysAgo, int timeToTryMinutes, int delaySeconds) {
        this.startDaysAgo = startDaysAgo;
        this.timeToTryMinutes = timeToTryMinutes;
        this.delaySeconds = delaySeconds;
    }
}
