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
        String size,
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
                pr.getStatus(),
                pr.getTitle(),
                pr.isDraft(),
                pr.isMerged(),
                pr.getMergedAt(),
                pr.getClosedAt(),
                pr.getSize().name(),
                pr.getHtmlUrl(),
                pr.getAdditions(),
                pr.getDeletions(),
                pr.getFileChanges(),
                pr.getOpenReviewComments(),
                pr.getCreatedAt(),
                pr.getUpdatedAt(),
                ReviewerResponse.from(pr.getReviewers()));
    }
}
