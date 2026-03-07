package com.devprmetrics.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeApi {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/alerts")
    public String alerts() {
        return "alerts";
    }

    @GetMapping("/avatars")
    public String avatars() {
        return "avatars";
    }

    @GetMapping("/badge")
    public String badge() {
        return "badge";
    }

    @GetMapping("/bar-chart")
    public String barChart() {
        return "bar-chart";
    }

    @GetMapping("/basic-tables")
    public String basicTables() {
        return "basic-tables";
    }

    @GetMapping("/blank")
    public String blank() {
        return "blank";
    }

    @GetMapping("/buttons")
    public String buttons() {
        return "buttons";
    }

    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }

    @GetMapping("/form-elements")
    public String formElements() {
        return "form-elements";
    }

    @GetMapping("/images")
    public String images() {
        return "images";
    }

    @GetMapping("/line-chart")
    public String lineChart() {
        return "line-chart";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/sidebar")
    public String sidebar() {
        return "sidebar";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/videos")
    public String videos() {
        return "videos";
    }

    @GetMapping("/404")
    public String notFoundPage() {
        return "404";
    }

}
