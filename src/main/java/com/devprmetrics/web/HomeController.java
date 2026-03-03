package com.devprmetrics.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final GitHub gitHub;
    private final String organization;

    public HomeController(GitHub gitHub, @Value("${github.org}") String organization) {
        this.gitHub = gitHub;
        this.organization = organization;
    }

    @GetMapping("/")
    public String home(Model model) throws IOException {
        model.addAttribute("title", "Dev PR Metrics");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", organization);
        model.addAttribute("repositories", listRepositories());
        return "home";
    }

    @GetMapping("/repos")
    public String repos(Model model) throws IOException {
        model.addAttribute("title", "Repositórios da Organização");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", organization);
        model.addAttribute("repositories", listRepositories());
        return "home";
    }

    private List<RepositoryView> listRepositories() throws IOException {
        List<RepositoryView> repositories = new ArrayList<>();
        for (GHRepository repository : gitHub.getOrganization(organization).listRepositories()) {
            repositories.add(new RepositoryView(
                    repository.getName(),
                    repository.getHtmlUrl() != null ? repository.getHtmlUrl().toString() : "#",
                    repository.isPrivate(),
                    repository.getDefaultBranch()));
        }
        return repositories;
    }

    public record RepositoryView(String name, String htmlUrl, boolean isPrivate, String defaultBranch) {
    }
}
