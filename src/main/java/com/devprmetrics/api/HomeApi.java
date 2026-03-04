package com.devprmetrics.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeApi {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
