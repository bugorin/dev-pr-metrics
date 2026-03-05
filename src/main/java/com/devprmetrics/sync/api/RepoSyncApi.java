package com.devprmetrics.sync.api;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.repo.RepoRepository;
import com.devprmetrics.sync.job.RepoEnqueueJob;
import com.devprmetrics.sync.service.RepoHandleService;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
public class RepoSyncApi {

    private final RepoRepository repoRepository;
    private final JobScheduler jobScheduler;
    private final RepoEnqueueJob repoEnqueueJob;

    @GetMapping("/sync/repositories")
    public ResponseEntity<String> syncRepositories() {
        JobId enqueue = jobScheduler.enqueue(repoEnqueueJob::enqueueAll);
        return ResponseEntity.ok(enqueue.asUUID().toString());
    }

    @GetMapping("/sync/repository/{repo}")
    public ResponseEntity<String> syncRepository(@PathVariable String repo) {
        Optional<Repo> possibleRepo = repoRepository.findAllByNameContainingIgnoreCase(repo);

        return possibleRepo
                .map(r -> jobScheduler.enqueue(() -> repoEnqueueJob.enqueue(r.getName())))
                .map(JobId::asUUID)
                .map(UUID::toString)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
