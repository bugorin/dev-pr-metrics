package com.devprmetrics.sync;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.user.User;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SyncPrService {

    private final GitHub gitHub;
    private final PrHandleService prHandleService;
    private final RepoHandleService repoHandleService;
    private final String organization;

    public SyncPrService(
            GitHub gitHub,
            PrHandleService prHandleService,
            RepoHandleService repoHandleService,
            @Value("${github.org}") String organization
    ) {
        this.gitHub = gitHub;
        this.prHandleService = prHandleService;
        this.repoHandleService = repoHandleService;
        this.organization = organization;
    }

    public List<User> syncReviewers(String repositoryName) throws IOException {
        GHRepository ghRepository = gitHub.getRepository(organization + "/" + repositoryName);
        Repo repository = repoHandleService.createOrMerge(ghRepository);

        List<GHPullRequest> ghPullRequests = ghRepository
                .queryPullRequests()
                .state(GHIssueState.ALL)
                .list()
                .toList();

        for (GHPullRequest ghPullRequest : ghPullRequests) {
            prHandleService.createOrMerge(repository, ghPullRequest);
        }

        return Collections.emptyList();
    }
}
