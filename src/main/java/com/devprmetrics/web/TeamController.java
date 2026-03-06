package com.devprmetrics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamController {

    @GetMapping("/teams")
    public String teams(Model model) {
        model.addAttribute("page", "teams");
        return "teams";
    }
}
