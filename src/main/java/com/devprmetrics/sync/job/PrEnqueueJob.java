package com.devprmetrics.sync.job;

import com.devprmetrics.sync.http.PrSyncHttpService;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class PrEnqueueJob {

    private final PrSyncHttpService prSyncService;

    @Job(name = "Sync from repo=%0 and pr number=%1")
    public void enqueue(String repoName, int pullRequestNumber) throws IOException {
        prSyncService.sync(repoName, pullRequestNumber);
    }

}
