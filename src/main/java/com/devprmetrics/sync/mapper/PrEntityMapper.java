package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrStatus;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;

import java.util.List;

import static com.devprmetrics.config.LocalDateTimeUtils.toLocalDateTime;

public record PrEntityMapper() {

    public static Pr mapper(GHPullRequest pullRequest) {
        try {
            if(pullRequest == null) {
                throw new IllegalArgumentException("GHPullRequest is null");
            }
            Pr pr = new Pr(
                    pullRequest.getId(),
                    UserEntityMapper.mapper(pullRequest.getUser()),
                    PrStatus.from(pullRequest.getState()),
                    toLocalDateTime(pullRequest.getCreatedAt()),
                    toLocalDateTime(pullRequest.getUpdatedAt())
            );

            List<GHPullRequestReview> ghReviews = pullRequest.listReviews().toList();
            for (GHPullRequestReview review : ghReviews) {
                pr.addOrUpdate(ReviewerEntityMapper.mapper(pr, review));
            }

            return pr;
        }catch (Exception e) {
            throw new IllegalArgumentException("Could not map GHPullRequest pr to entity", e);
        }
    }
}
