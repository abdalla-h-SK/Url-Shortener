package com.ratelimiting.rulshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication @EnableAsync
public class RateLimitingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimitingApplication.class, args);
    }

}
