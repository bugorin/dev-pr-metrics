package com.devprmetrics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RepositoryController {

    @GetMapping("/repositories")
    public String repositories(Model model) {
        model.addAttribute("page", "repositories");
        return "repositories";
    }
}
