package com.devprmetrics.api.pr;

import com.devprmetrics.api.user.UserApiResponse;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.review.ReviewerStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewerResponse(
        UserApiResponse user,
        ReviewerStatus status,
        LocalDateTime submittedAt) {

    public static List<ReviewerResponse> from(List<Reviewer> reviewers) {
        return reviewers.stream().map(ReviewerResponse::from).toList();
    }

    public static ReviewerResponse from(Reviewer reviewer) {
        return new ReviewerResponse(
                UserApiResponse.from(reviewer.getUser()),
                reviewer.getStatus(),
                reviewer.getSubmittedAt()
        );
    }
}