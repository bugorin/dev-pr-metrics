package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.sync.service.PrHandleService;
import com.devprmetrics.sync.service.RepoHandleService;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class SyncPrHttpService {

    private final GitHub gitHub;
    private final Envie envie;
    private final PrHandleService prHandleService;
    private final RepoHandleService repoHandleService;

    public void sync(String repoName) throws IOException {
        GHRepository ghRepository = gitHub.getRepository(envie.getOrganization() + "/" + repoName);
        Repo repository = repoHandleService.createOrMerge(ghRepository);

        List<GHPullRequest> ghPullRequests = ghRepository
                .queryPullRequests()
                .state(GHIssueState.ALL)
                .list()
                .toList();

        for (GHPullRequest ghPullRequest : ghPullRequests) {
            prHandleService.createOrMerge(repository, ghPullRequest);
        }
    }
}
