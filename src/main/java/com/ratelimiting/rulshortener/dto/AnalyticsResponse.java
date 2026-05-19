package com.ratelimiting.rulshortener.dto;

import java.time.LocalDateTime;

public record AnalyticsResponse(
        String shortHash,
        String ipAddress,
        String userAgent,
        LocalDateTime clickedAt
) {
}
