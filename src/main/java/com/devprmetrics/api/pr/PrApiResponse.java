package com.devprmetrics.api.pr;

import com.devprmetrics.api.user.UserApiResponse;
import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PrApiResponse(
        Long id,
        UserApiResponse author,
        PrStatus ghStatus,
        LocalDateTime ghCreatedAt,
        LocalDateTime ghUpdatedAt,
        List<ReviewerResponse> ghReviewers) {

    public static PrApiResponse from(Pr pr) {
        return new PrApiResponse(
                pr.getId(),
                UserApiResponse.from(pr.getAuthor()),
                pr.getGithubStatus(),
                pr.getGithubCreatedAt(),
                pr.getGithubUpdatedAt(),
                ReviewerResponse.from(pr.getReviewers())
        );
    }
}