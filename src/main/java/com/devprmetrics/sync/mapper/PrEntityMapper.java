package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrStatus;
import com.devprmetrics.domain.repo.Repo;
import org.kohsuke.github.*;

import java.util.List;

import static com.devprmetrics.config.LocalDateTimeUtils.toLocalDateTime;

public record PrEntityMapper() {

    public static Pr mapper(Repo repository, GHPullRequest pullRequest) {
        try {
            if (pullRequest == null) {
                throw new IllegalArgumentException("GHPullRequest is null");
            }

            List<String> githubLabels = pullRequest.getLabels().stream()
                    .map(GHLabel::getName)
                    .toList();

            List<Long> githubRequestedReviewers = pullRequest.getRequestedReviewers().stream()
                    .map(GHUser::getId)
                    .toList();

            List<GHPullRequestReview> ghReviews = pullRequest.listReviews().toList();
            Pr pr = new Pr(
                    pullRequest.getId(),
                    pullRequest.getNumber(),
                    UserEntityMapper.mapper(pullRequest.getUser()),
                    repository,
                    mapToStatus(pullRequest.getState()),
                    pullRequest.getTitle(),
                    pullRequest.isDraft(),
                    pullRequest.isMerged(),
                    toLocalDateTime(pullRequest.getMergedAt()),
                    toLocalDateTime(pullRequest.getClosedAt()),
                    pullRequest.getHtmlUrl() != null ? pullRequest.getHtmlUrl().toString() : null,
                    pullRequest.getAdditions(),
                    pullRequest.getDeletions(),
                    pullRequest.getChangedFiles(),
                    pullRequest.getReviewComments(),
                    pullRequest.getCommentsCount(),
                    pullRequest.getCommits(),
                    pullRequest.getBase().getRef(),
                    pullRequest.getHead().getRef(),
                    githubLabels,
                    githubRequestedReviewers,
                    toLocalDateTime(pullRequest.getCreatedAt()),
                    toLocalDateTime(pullRequest.getUpdatedAt())
            );

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
