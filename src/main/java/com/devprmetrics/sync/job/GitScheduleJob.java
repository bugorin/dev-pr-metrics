package com.devprmetrics.sync.job;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.devprmetrics.domain.sync.SyncLogService;
import com.devprmetrics.sync.http.PrQuerySearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import static com.devprmetrics.domain.sync.SyncLogOption.PR_QUERY;

@Service
@Slf4j
@AllArgsConstructor
public class GitScheduleJob {

    private final SyncLogService syncLogService;
    private final PrQuerySearchService prQuerySearchService;
    private final JobScheduler jobScheduler;
    private final PrEnqueueJob prEnqueueJob;

    @Recurring(id = "sync-pr-weekday-2min", cron = "*/1 8-23 * * 1-5")
    public void scheduleWeekday() {
        execute();
    }

    @Recurring(id = "sync-pr-weekend-30min", cron = "*/30 8-22 * * 6,0")
    public void scheduleWeekend() {
        execute();
    }

    @Recurring(id = "sync-pr-night-1h", cron = "0 23,0-7 * * *")
    public void scheduleNight() {
        execute();
    }

    private void execute() {
        syncLogService.execute(PR_QUERY, this::executeRange);
    }

    private void executeRange(java.time.LocalDateTime startLocalDate, java.time.LocalDateTime endLocalDate) throws IOException {
        List<PrQuerySearchService.PrSearchItem> items = prQuerySearchService
                .searchUpdatedPullRequests(startLocalDate, endLocalDate);
        execute(items);
    }

    private void execute(List<PrQuerySearchService.PrSearchItem> items) {
        items.forEach(prInfo -> enqueuePr(prInfo.repo(), prInfo.number()));
    }

    private void enqueuePr(String repo, Integer number) {
        JobId jobId = jobScheduler.enqueue(() -> prEnqueueJob.enqueue(repo, number));
        log.debug("Enqueued PR sync job {}", jobId.asUUID());
    }
}
