package com.devprmetrics.sync;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.sync.http.*;
import com.devprmetrics.sync.job.JobRunrTestJob;
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
public class SyncGitHubApi {

    private final SyncPrHttpService syncPrService;
    private final SyncRepoHttpService syncRepoHttpService;
    private final GitHubPrSearchTestService gitHubPrSearchTestService;
    private final JobScheduler jobScheduler;
    private final JobRunrTestJob jobRunrTestJob;

    @GetMapping("/sync/pr/repositories")
    public ResponseEntity<List<Repo>> syncRepositories() throws IOException {
        syncRepoHttpService.sync();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sync/pr/repository/{nome}")
    public ResponseEntity<Void> syncRepository(@PathVariable("nome") String nome) throws IOException {
        syncPrService.sync(nome);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sync/pr/search/{repo}")
    public ResponseEntity<List<String>> searchUpdatedPrIds(
            @PathVariable("repo") String repo,
            @RequestParam(value = "updatedSince", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedSince)
            throws IOException {

        List<GitHubPrSearchTestService.PrSearchItem> items =
                gitHubPrSearchTestService.searchUpdatedPullRequests(repo, updatedSince);

        List<String> jobsId = items.stream()
                .map(prInfo -> jobScheduler.enqueue(() -> jobRunrTestJob.execute(prInfo.id())))
                .map(JobId::asUUID)
                .map(UUID::toString)
                .toList();

        return ResponseEntity.ok(jobsId);
    }
    
}
