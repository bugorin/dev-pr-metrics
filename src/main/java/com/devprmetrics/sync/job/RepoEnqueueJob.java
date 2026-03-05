package com.devprmetrics.sync.job;

import com.devprmetrics.sync.http.RepoSyncHttpService;
import com.devprmetrics.sync.service.RepoHandleService;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class RepoEnqueueJob {

    private final RepoSyncHttpService repoHttpService;
    private final RepoHandleService repoHandleService;
    private final JobScheduler jobScheduler;

    @Job(name = "Sync from repository=%0")
    public void enqueue(String repoName) throws IOException {
        GHRepository repo = repoHttpService.findBy(repoName);
        repoHandleService.createOrMerge(repo);
    }

    @Job(name = "Sync from all repository")
    public void enqueueAll() throws  IOException {
        List<GHRepository> repos = repoHttpService.findAll();
        for (GHRepository repo : repos) {
            jobScheduler.enqueue(() -> enqueue(repo.getName()));
        }
    }

}
