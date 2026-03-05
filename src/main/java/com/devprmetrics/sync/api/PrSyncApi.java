package com.devprmetrics.sync.api;

import com.devprmetrics.sync.http.*;
import com.devprmetrics.sync.job.PrEnqueueJob;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PrSyncApi {

    private final PrQuerySearchService querySearchService;
    private final JobScheduler jobScheduler;
    private final PrEnqueueJob prEnqueueJob;

    @GetMapping("/sync/pr/search/{repo}")
    public ResponseEntity<List<String>> searchUpdatedPrIds(
            @PathVariable("repo") String repo,
            @RequestParam(value = "updatedSince", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedSince)
            throws IOException {

        List<PrQuerySearchService.PrSearchItem> items =
                querySearchService.searchUpdatedPullRequests(repo, updatedSince);

        List<String> jobsId = items.stream()
                .map(prInfo -> jobScheduler.enqueue(() -> prEnqueueJob.enqueue(repo, prInfo.number())))
                .map(JobId::asUUID)
                .map(UUID::toString)
                .toList();

        return ResponseEntity.ok(jobsId);
    }
}
