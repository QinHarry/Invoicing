package com.halen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping
public class IndexController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main")
    public String home() {
        return "main";
    }
}
