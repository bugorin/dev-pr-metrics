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
        String ghTitle,
        boolean ghDraft,
        boolean ghMerged,
        LocalDateTime ghMergedAt,
        LocalDateTime ghClosedAt,
        String ghHtmlUrl,
        int ghAdditions,
        int ghDeletions,
        int ghFileChanges,
        int ghOpenReviewComments,
        LocalDateTime ghCreatedAt,
        LocalDateTime ghUpdatedAt,
        List<ReviewerResponse> ghReviewers) {

    public static PrApiResponse from(Pr pr) {
        return new PrApiResponse(
                pr.getId(),
                UserApiResponse.from(pr.getAuthor()),
                pr.getGithubStatus(),
                pr.getGithubTitle(),
                pr.isGithubDraft(),
                pr.isGithubMerged(),
                pr.getGithubMergedAt(),
                pr.getGithubClosedAt(),
                pr.getGithubHtmlUrl(),
                pr.getGithubAdditions(),
                pr.getGithubDeletions(),
                pr.getGithubFileChanges(),
                pr.getGithubOpenReviewComments(),
                pr.getGithubCreatedAt(),
                pr.getGithubUpdatedAt(),
                ReviewerResponse.from(pr.getReviewers()));
    }
}
