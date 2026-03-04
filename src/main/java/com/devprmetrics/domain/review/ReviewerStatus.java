package com.devprmetrics.domain.review;

import org.kohsuke.github.GHPullRequestReviewState;

import java.util.stream.Stream;

public enum ReviewerStatus {
    PENDING(GHPullRequestReviewState.PENDING),
    APPROVED(GHPullRequestReviewState.APPROVED),
    CHANGES_REQUESTED(GHPullRequestReviewState.CHANGES_REQUESTED),
    REQUEST_CHANGES(GHPullRequestReviewState.REQUEST_CHANGES),
    COMMENTED(GHPullRequestReviewState.COMMENTED),
    DISMISSED(GHPullRequestReviewState.DISMISSED);

    private final GHPullRequestReviewState ghState;

    ReviewerStatus(GHPullRequestReviewState ghState) {
        this.ghState = ghState;
    }

    private boolean is(GHPullRequestReviewState ghState) {
        return this.ghState.equals(ghState);
    }

    public static ReviewerStatus from(GHPullRequestReviewState state) {
        return Stream.of(ReviewerStatus.values())
                .filter(s -> s.is(state))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
