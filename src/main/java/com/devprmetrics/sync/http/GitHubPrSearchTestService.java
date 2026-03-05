package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import com.devprmetrics.config.LocalDateTimeUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GitHubPrSearchTestService {

    private final Envie envie;
    private final GitHub gitHub;

    public List<PrSearchItem> searchUpdatedPullRequests(String repository, LocalDateTime updatedSince) throws IOException {
        LocalDateTime updatedSinceUtc = LocalDateTimeUtils.toUtc(updatedSince, ZoneId.systemDefault());
        return searchUpdatedPullRequestsUTC(repository, updatedSinceUtc);
    }

    public List<PrSearchItem> searchUpdatedPullRequestsUTC(String repository, LocalDateTime updatedSinceUtc) throws IOException {
        String query = buildQuery(repository, updatedSinceUtc);
        PagedSearchIterable<GHIssue> listed = gitHub.searchIssues().q(query).list();
        return toPrSearchItem(listed);
    }

    private String buildQuery(String repository, LocalDateTime updatedSinceUtc) {
        return String.format(
                """
                org:%s repo:%s is:pr updated:>=%s
                """,
                envie.getOrganization(),
                repository,
                LocalDateTimeUtils.toIsoInstantUtc(updatedSinceUtc)).trim();
    }

    private List<PrSearchItem> toPrSearchItem(PagedSearchIterable<GHIssue> issues) throws IOException {
        List<PrSearchItem> results = new ArrayList<>();
        for (GHIssue issue : issues) {
            results.add(new PrSearchItem(issue));
        }
        return results;
    }

    public record PrSearchItem(Long id, LocalDateTime updatedAt) {

        public PrSearchItem(GHIssue issue) throws IOException {
            this(
                    issue.getId(),
                    LocalDateTimeUtils.toLocalDateTime(issue.getUpdatedAt()));
        }

    }
}
