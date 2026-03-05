package com.devprmetrics.sync;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.config.LocalDateTimeUtils;
import com.devprmetrics.sync.http.GitHubPrSearchTestService;
import com.devprmetrics.sync.http.SyncPrHttpService;
import com.devprmetrics.sync.http.SyncRepoHttpService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SyncGitHubApi {

    private final SyncPrHttpService syncPrService;
    private final SyncRepoHttpService syncRepoHttpService;
    private final GitHubPrSearchTestService gitHubPrSearchTestService;

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
    public ResponseEntity<List<GitHubPrSearchTestService.PrSearchItem>> searchUpdatedPrIds(
            @PathVariable("repo") String repo,
            @RequestParam(value = "updatedSince", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedSince)
            throws IOException {

//        LocalDateTime effectiveUpdatedSince = updatedSince.orElse(LocalDateTime.now().minusMinutes(30));
        List<GitHubPrSearchTestService.PrSearchItem> items =
                gitHubPrSearchTestService.searchUpdatedPullRequests(repo, updatedSince);
        return ResponseEntity.ok(items);
    }
}
