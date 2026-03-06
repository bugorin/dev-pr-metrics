package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import com.devprmetrics.config.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PrQuerySearchService {

    private final Envie envie;
    private final GitHub gitHub;

    @Transactional
    public List<PrSearchItem> searchUpdatedPullRequests(LocalDateTime start, LocalDateTime end) throws IOException {
        LocalDateTime updatedSinceUtc = LocalDateTimeUtils.toUtc(start, ZoneId.systemDefault());
        LocalDateTime maxUpdatedAtUtc = LocalDateTimeUtils.toUtc(end, ZoneId.systemDefault());
        return searchUpdatedPullRequestsUTC(updatedSinceUtc, maxUpdatedAtUtc);
    }

    private List<PrSearchItem> searchUpdatedPullRequestsUTC(
            LocalDateTime updatedSinceUtc,
            LocalDateTime maxUpdatedAtUtc) throws IOException {

        String query = buildQuery(updatedSinceUtc, maxUpdatedAtUtc);
        PagedSearchIterable<GHIssue> listed = gitHub.searchIssues().q(query).list();
        return toPrSearchItem(listed);
    }

    private String buildQuery(LocalDateTime updatedSinceUtc, LocalDateTime maxUpdatedAtUtc) {
        return String.format(
                """
                org:%s is:pr updated:%s..%s
                """,
                envie.getOrganization(),
                LocalDateTimeUtils.toIsoInstantUtc(updatedSinceUtc),
                LocalDateTimeUtils.toIsoInstantUtc(maxUpdatedAtUtc)).trim();
    }

    private List<PrSearchItem> toPrSearchItem(PagedSearchIterable<GHIssue> issues) throws IOException {
        List<PrSearchItem> results = new ArrayList<>();
        for (GHIssue issue : issues) {
            results.add(new PrSearchItem(issue));
        }
        return results;
    }

    public record PrSearchItem(String repo, Long id, Integer number, LocalDateTime updatedAt) {

        public PrSearchItem(GHIssue issue) throws IOException {
            this(
                    issue.getRepository().getName(),
                    issue.getId(),
                    issue.getNumber(),
                    LocalDateTimeUtils.toLocalDateTime(issue.getUpdatedAt()));
        }

    }
}
