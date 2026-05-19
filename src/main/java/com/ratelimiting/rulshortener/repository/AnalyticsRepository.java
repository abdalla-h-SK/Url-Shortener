package com.ratelimiting.rulshortener.repository;

import com.ratelimiting.rulshortener.model.AnalyticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, Long> {
}
