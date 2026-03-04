package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.review.ReviewerStatus;
import org.kohsuke.github.GHPullRequestReview;

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
                    ReviewerStatus.from(ghReview.getState()),
                    toLocalDateTime(ghReview.getSubmittedAt())
            );
        }catch (Exception e) {
            throw new IllegalArgumentException("Could not map ghReview to reviewer entity", e);
        }
    }
}
