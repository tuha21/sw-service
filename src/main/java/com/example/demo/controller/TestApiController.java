package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestApiController {

    @GetMapping("/hello")
    public String checkServiceHeartBeat () {
        return "Hello! I'm fine";
    }

}
