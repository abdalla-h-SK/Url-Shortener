CREATE TABLE IF NOT EXISTS urls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    short_hash VARCHAR(255) UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS analytics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_hash VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(512),
    clicked_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analytics_short_hash ON analytics(short_hash);