package com.devprmetrics.web;

import com.devprmetrics.github.repos.RepositoryApi;
import com.devprmetrics.github.repos.models.Repository;
import com.devprmetrics.github.repos.models.RepositoryPull;
import com.devprmetrics.github.repos.models.RepositoryPullReview;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.devprmetrics.github.repos.request.RepositoryPullsRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RepositoryController {

    private static final String ORGANIZATION = "bugorindevtest";

    private final RepositoryApi repositoryApi;

    public RepositoryController(RepositoryApi repositoryApi) {
        this.repositoryApi = repositoryApi;
    }

    @GetMapping("/repository/{name}")
    public String repository(@PathVariable String name, Model model) {
        Repository repository = repositoryApi.getRepository(ORGANIZATION, name);
        List<RepositoryPull> openPullRequests = repositoryApi.listPullRequests(new RepositoryPullsRequest(ORGANIZATION, name));
        Map<String, Long> languages = repositoryApi.listLanguages(ORGANIZATION, name);

        List<PullWithReviews> pullsWithReviews = new ArrayList<>();
        for (RepositoryPull pull : openPullRequests) {
            List<RepositoryPullReview> reviews = repositoryApi.listPullRequestReviews(ORGANIZATION, name, pull.number());
            pullsWithReviews.add(new PullWithReviews(pull, reviews));
        }

        model.addAttribute("title", "Detalhe do Repositório");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", ORGANIZATION);
        model.addAttribute("repository", repository);
        model.addAttribute("pullsWithReviews", pullsWithReviews);
        model.addAttribute("languages", languages);
        return "repository";
    }

    public record PullWithReviews(RepositoryPull pull, List<RepositoryPullReview> reviews) {
    }
}
