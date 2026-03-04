package com.devprmetrics.sync;

import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserCreateService;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SyncPrService {

    private final GitHub gitHub;
    private final PrHandleService prHandleService;
    private final String organization;

    public SyncPrService(
            GitHub gitHub,
            UserCreateService userCreateService,
            PrHandleService prHandleService,
            @Value("${github.org}") String organization
    ) {
        this.gitHub = gitHub;
        this.prHandleService = prHandleService;
        this.organization = organization;
    }

    public List<User> syncReviewers(String repositoryName) throws IOException {
        List<GHPullRequest> ghPullRequests = gitHub.getRepository(organization + "/" + repositoryName)
                .queryPullRequests()
                .state(GHIssueState.ALL)
                .list()
                .toList();


        for (GHPullRequest ghPullRequest : ghPullRequests) {
            prHandleService.createOrMerge(ghPullRequest);
        }

        return Collections.emptyList();
    }
}
