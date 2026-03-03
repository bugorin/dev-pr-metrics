package com.devprmetrics.web;

import java.time.LocalDate;
import java.util.List;

import com.devprmetrics.github.repos.RepositoryApi;
import com.devprmetrics.github.repos.models.Repository;
import com.devprmetrics.github.repos.models.RepositoryPull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RepositoryApi repositoryApi;

    public HomeController(RepositoryApi repositoryApi) {
        this.repositoryApi = repositoryApi;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Dev PR Metrics");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", "");
        model.addAttribute("repositories", repositoryApi.listRepositories("bugorindevtest"));
        return "home";
    }

    @GetMapping("/repos")
    public String repos(Model model) {
        List<Repository> repositories = repositoryApi.listRepositories("bugorindevtest");

        for (Repository repository : repositories) {
            List<RepositoryPull> bugorindevtest = repositoryApi.listPullRequests("bugorindevtest", repository.name());
            for (RepositoryPull pull : bugorindevtest) {
                System.out.println(pull.authorLogin());
            }
        }

        model.addAttribute("title", "Repositórios da Organização");
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("organization", "");
        model.addAttribute("repositories", repositories);
        return "home";
    }
}
