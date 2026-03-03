package com.devprmetrics.web;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RepositoryController {

    private final GitHub gitHub;
    private final String organization;

    public RepositoryController(GitHub gitHub, @Value("${github.org}") String organization) {
        this.gitHub = gitHub;
        this.organization = organization;
    }

    @GetMapping("/repository/{name}")
    public String repository(@PathVariable String name, Model model) throws IOException {
        GHRepository repository = gitHub.getRepository(organization + "/" + name);
        Instant lastMonthCutoff = Instant.now().minus(30, ChronoUnit.DAYS);

        List<PullWithReviews> pullsWithReviews = new ArrayList<>();
        for (GHPullRequest pull : repository.queryPullRequests().state(GHIssueState.ALL).list()) {
            if (pull.getCreatedAt() == null || pull.getCreatedAt().toInstant().isBefore(lastMonthCutoff)) {
                continue;
            }
            List<GHPullRequestReview> reviews = new ArrayList<>();

            for (GHPullRequestReview review : pull.listReviews()) {
                reviews.add(review);
            }
            int additions = pull.getAdditions();
            int deletions = pull.getDeletions();
            int changedFiles = pull.getChangedFiles();
            int openReviewComments = pull.getReviewComments();
            pullsWithReviews.add(new PullWithReviews(
                    pull,
                    reviews,
                    additions,
                    deletions,
                    additions + deletions,
                    changedFiles,
                    openReviewComments,
                    openReviewComments > 0));
        }

        Map<String, Long> languages = repository.listLanguages();

        model.addAttribute("title", "Detalhe do Repositório");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", organization);
        model.addAttribute("repository", repository);
        model.addAttribute("pullsWithReviews", pullsWithReviews);
        model.addAttribute("languages", languages);
        return "repository";
    }

    public record PullWithReviews(
            GHPullRequest pull,
            List<GHPullRequestReview> reviews,
            int additions,
            int deletions,
            int changedLines,
            int changedFiles,
            int openReviewComments,
            boolean hasOpenReviewComments) {
    }
}
