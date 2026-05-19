package com.ratelimiting.rulshortener.controller;

import com.ratelimiting.rulshortener.event.ClickEvent;
import com.ratelimiting.rulshortener.service.UrlRedirectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    private final UrlRedirectService redirectService;
    private final ApplicationEventPublisher eventPublisher;

    public RedirectController(UrlRedirectService redirectService, ApplicationEventPublisher eventPublisher) {
        this.redirectService = redirectService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/{shortHash}")
    public ResponseEntity<String> redirect(@PathVariable String shortHash, HttpServletRequest request) {
        String originalUrl = redirectService.getOriginalUrl(shortHash);

        String ip = request.getHeader("X-Forwarded-For") != null ?
                request.getHeader("X-Forwarded-For").split(",")[0].trim() : request.getRemoteAddr();

        eventPublisher.publishEvent(new ClickEvent(shortHash, ip, request.getHeader("User-Agent")));

        return ResponseEntity.status(HttpStatus.FOUND).body(originalUrl);
    }
}
