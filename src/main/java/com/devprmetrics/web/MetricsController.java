package com.devprmetrics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetricsController {

    @GetMapping("/metrics")
    public String metrics(Model model) {
        model.addAttribute("page", "metrics");
        return "metrics";
    }
}
