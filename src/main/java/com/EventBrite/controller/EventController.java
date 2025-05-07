package com.EventBrite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class EventController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
