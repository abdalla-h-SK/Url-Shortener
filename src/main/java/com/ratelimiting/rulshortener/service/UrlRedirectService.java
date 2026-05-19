package com.ratelimiting.rulshortener.service;

import com.ratelimiting.rulshortener.model.UrlEntity;
import com.ratelimiting.rulshortener.repository.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class UrlRedirectService {

    private final UrlRepository repository;
    private final Base62Encoder encoder;
    private final StringRedisTemplate redisTemplate;

    public UrlRedirectService(UrlRepository repository, Base62Encoder encoder, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.encoder = encoder;
        this.redisTemplate = redisTemplate;
    }

    public String getOriginalUrl(String shortHash) {
        String cacheKey = "url:" + shortHash;

        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) return cachedUrl;

        long dbId = encoder.decode(shortHash);
        UrlEntity entity = repository.findById(dbId)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        redisTemplate.opsForValue().set(cacheKey, entity.getOriginalUrl(), Duration.ofHours(24));
        return entity.getOriginalUrl();
    }
}