package com.devprmetrics.web;

import com.devprmetrics.github.GitHubOrgService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final GitHubOrgService gitHubOrgService;

    public HomeController(GitHubOrgService gitHubOrgService) {
        this.gitHubOrgService = gitHubOrgService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Dev PR Metrics");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", gitHubOrgService.organization());
        model.addAttribute("repositories", gitHubOrgService.listOrganizationRepositories());
        return "home";
    }

    @GetMapping("/repos")
    public String repos(Model model) {
        List<GitHubOrgService.GitHubRepository> repositories = gitHubOrgService.listOrganizationRepositories();
        model.addAttribute("title", "Repositórios da Organização");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", gitHubOrgService.organization());
        model.addAttribute("repositories", repositories);
        return "home";
    }
}
