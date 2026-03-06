package com.devprmetrics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PullRequestController {

    @GetMapping("/pull-requests")
    public String pullRequests(Model model) {
        model.addAttribute("page", "pull-requests");
        return "pull-requests";
    }
}
