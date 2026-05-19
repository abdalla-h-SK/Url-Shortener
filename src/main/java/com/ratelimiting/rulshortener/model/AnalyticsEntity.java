package com.ratelimiting.rulshortener.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AnalyticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortHash;

    private String ipAddress;
    private String userAgent;
    private LocalDateTime clickedAt;
}