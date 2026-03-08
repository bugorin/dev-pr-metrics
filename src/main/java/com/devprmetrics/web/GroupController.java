package com.devprmetrics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {

    @GetMapping("/admin/groups")
    private String teams(Model model) {
        return "pages/groups";
    }

}
