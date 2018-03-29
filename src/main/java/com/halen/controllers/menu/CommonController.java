package com.halen.controllers.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/stockSearch")
    public String stockSearch() {
        return "common/stockSearch";
    }
}
