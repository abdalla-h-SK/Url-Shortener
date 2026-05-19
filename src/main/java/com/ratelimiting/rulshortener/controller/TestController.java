package com.ratelimiting.rulshortener.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured")
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello! If you see this, you are within the rate limit.";
    }
}
