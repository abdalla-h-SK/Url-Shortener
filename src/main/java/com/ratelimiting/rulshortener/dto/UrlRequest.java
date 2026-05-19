package com.ratelimiting.rulshortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UrlRequest(
        @NotBlank(message = "The URL cannot be empty.")
        @URL(message = "You must provide a valid web address (e.g., https://example.com).")
        String originalUrl
) {}