package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.sync.service.PrHandleService;
import com.devprmetrics.sync.service.RepoHandleService;
import lombok.AllArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class PrSyncHttpService {

    private final GitHub gitHub;
    private final Envie envie;
    private final PrHandleService prHandleService;
    private final RepoHandleService repoHandleService;

    public void sync(String repoName, int pullRequestNumber) throws IOException {
        GHRepository ghRepository = gitHub.getRepository(envie.getOrganization() + "/" + repoName);
        Repo repository = repoHandleService.findOrCreate(ghRepository);
        GHPullRequest ghPullRequest = ghRepository.getPullRequest(pullRequestNumber);
        prHandleService.createOrMerge(repository, ghPullRequest);
    }
}
