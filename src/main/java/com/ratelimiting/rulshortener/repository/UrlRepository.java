package com.ratelimiting.rulshortener.repository;

import com.ratelimiting.rulshortener.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
}
