package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrStatus;
import com.devprmetrics.domain.repo.Repo;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;

import java.util.List;

import static com.devprmetrics.config.LocalDateTimeUtils.toLocalDateTime;

public record PrEntityMapper() {

    public static Pr mapper(Repo repository, GHPullRequest pullRequest) {
        try {
            if (pullRequest == null) {
                throw new IllegalArgumentException("GHPullRequest is null");
            }

            Pr pr = new Pr(
                    pullRequest.getId(),
                    UserEntityMapper.mapper(pullRequest.getUser()),
                    repository,
                    mapToStatus(pullRequest.getState()),
                    toLocalDateTime(pullRequest.getCreatedAt()),
                    toLocalDateTime(pullRequest.getUpdatedAt())
            );

            List<GHPullRequestReview> ghReviews = pullRequest.listReviews().toList();
            for (GHPullRequestReview review : ghReviews) {
                pr.addOrUpdate(ReviewerEntityMapper.mapper(pr, review));
            }

            return pr;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not map GHPullRequest pr to entity", e);
        }
    }

    private static PrStatus mapToStatus(GHIssueState state) {
        if (state == null) {
            throw new IllegalArgumentException("GHIssueState is null");
        }
        return switch (state) {
            case OPEN -> PrStatus.OPEN;
            case CLOSED -> PrStatus.CLOSED;
            default -> throw new IllegalArgumentException("Unknown GHIssueState: " + state);
        };
    }
}
