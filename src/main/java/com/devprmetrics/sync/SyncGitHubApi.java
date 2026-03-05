package com.devprmetrics.sync;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.sync.http.SyncPrHttpService;
import com.devprmetrics.sync.http.SyncRepoHttpService;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SyncGitHubApi {

    private final SyncPrHttpService syncPrService;
    private final SyncRepoHttpService syncRepoHttpService;

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
}
