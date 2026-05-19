package com.ratelimiting.rulshortener.service;

import com.ratelimiting.rulshortener.event.ClickEvent;
import com.ratelimiting.rulshortener.model.AnalyticsEntity;
import com.ratelimiting.rulshortener.repository.AnalyticsRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class AnalyticsWorker {

    private final AnalyticsRepository repository;

    public AnalyticsWorker(AnalyticsRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void processClick(ClickEvent event) {
        AnalyticsEntity entity = new AnalyticsEntity();
        entity.setShortHash(event.getShortHash());
        entity.setIpAddress(event.getIpAddress());
        entity.setUserAgent(event.getUserAgent());
        entity.setClickedAt(LocalDateTime.now());
        repository.save(entity);
    }
}