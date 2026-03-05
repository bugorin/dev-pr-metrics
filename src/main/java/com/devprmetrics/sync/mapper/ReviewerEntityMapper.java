package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.review.ReviewerStatus;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewState;

import static com.devprmetrics.config.LocalDateTimeUtils.toLocalDateTime;

public record ReviewerEntityMapper() {

    public static Reviewer mapper(Pr pr, GHPullRequestReview ghReview) {
        try {
            if (ghReview == null) {
                throw new IllegalArgumentException("ghReview is null");
            }

            return new Reviewer(
                    pr,
                    UserEntityMapper.mapper(ghReview.getUser()),
                    mapper(ghReview.getState()),
                    toLocalDateTime(ghReview.getSubmittedAt()),
                    ghReview.getBody()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not map ghReview to reviewer entity", e);
        }
    }

    private static ReviewerStatus mapper(GHPullRequestReviewState state) {
        if (state == null) {
            throw new IllegalArgumentException("GHPullRequestReviewState is null");
        }
        return switch (state) {
            case PENDING -> ReviewerStatus.PENDING;
            case APPROVED -> ReviewerStatus.APPROVED;
            case CHANGES_REQUESTED -> ReviewerStatus.CHANGES_REQUESTED;
            case REQUEST_CHANGES -> ReviewerStatus.REQUEST_CHANGES;
            case COMMENTED -> ReviewerStatus.COMMENTED;
            case DISMISSED -> ReviewerStatus.DISMISSED;
        };
    }
}
