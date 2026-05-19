package com.ratelimiting.rulshortener.controller;

import com.ratelimiting.rulshortener.dto.AnalyticsResponse;
import com.ratelimiting.rulshortener.dto.UrlRequest;
import com.ratelimiting.rulshortener.model.UrlEntity;
import com.ratelimiting.rulshortener.repository.AnalyticsRepository;
import com.ratelimiting.rulshortener.repository.UrlRepository;
import com.ratelimiting.rulshortener.service.Base62Encoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final AnalyticsRepository analyticsRepository;

    @PostMapping("/shorten")
    public ResponseEntity<String> createShortUrl(@Valid @RequestBody UrlRequest request) {

        UrlEntity newUrl = new UrlEntity();
        newUrl.setOriginalUrl(request.originalUrl());
        newUrl = urlRepository.save(newUrl); // The ID is now populated

        String shortHash = base62Encoder.encode(newUrl.getId());

        newUrl.setShortHash(shortHash);
        urlRepository.save(newUrl);

        String finalUrl = "http://localhost:8080/" + shortHash;
        return ResponseEntity.ok(finalUrl);
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<AnalyticsResponse>> analytics(){
        var analytics = analyticsRepository.findAll().stream()
                .map(a -> {
                            return new AnalyticsResponse(
                                        a.getShortHash(),
                                        a.getIpAddress(),
                                        a.getUserAgent(),
                                        a.getClickedAt());
                        }
                )
                .toList();

        return ResponseEntity.ok(analytics);
    }
}
