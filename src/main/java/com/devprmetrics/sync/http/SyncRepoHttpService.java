package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import com.devprmetrics.sync.service.RepoHandleService;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class SyncRepoHttpService {

    private final GitHub gitHub;
    private final RepoHandleService repoHandleService;
    private final Envie envie;

    public void sync() throws IOException {
        List<GHRepository> repositories = gitHub.getOrganization(envie.getOrganization())
                .listRepositories()
                .toList();

        for (GHRepository repository : repositories) {
            repoHandleService.createOrMerge(repository);
        }
    }
}
