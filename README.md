# URL Shortener with Rate Limiting, Caching & Click Analytics

A Spring Boot REST API that shortens URLs, redirects users, tracks click analytics asynchronously, and protects all endpoints with distributed rate limiting using Redis and Bucket4j.

## Features

- **URL Shortening**: Encodes database IDs using Base62 to generate short, unique hashes
- **URL Redirecting**: Resolves short hashes back to original URLs and returns a `302 Found` redirect
- **Redis Caching**: Resolved URLs are cached in Redis for 24 hours to avoid repeated DB lookups
- **Click Analytics**: Every redirect asynchronously records the short hash, client IP, and User-Agent to a MySQL `analytics` table
- **Distributed Rate Limiting**: Per-IP rate limiting (10 requests/minute) enforced via Bucket4j backed by Redis — works across multiple instances
- **Database Migrations**: Schema managed with Flyway (`urls` and `analytics` tables)
- **Spring Security Integration**: Rate limit filter runs inside the Spring Security filter chain

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.5.9 |
| Language | Java 17 |
| Database | MySQL 8 |
| Cache / Rate Limit Store | Redis 7 |
| Rate Limiting | Bucket4j 8.10.1 + Lettuce |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| Boilerplate | Lombok |

## Prerequisites

- Java 17+
- Maven 3.6+
- MySQL running on `localhost:3306`
- Redis running on `localhost:6379` (or use Docker — see below)

## Getting Started

### 1. Start Redis with Docker

```bash
docker-compose up -d
```

This starts a Redis 7 container on port `6379`.

### 2. Configure the database

Update `src/main/resources/application.yml` with your MySQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/urlshortener?createDatabaseIfNotExist=true
    username: your_username
    password: your_password
```

Flyway will automatically create the `urls` and `analytics` tables on first run.

### 3. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

## API Endpoints

### Shorten a URL

**POST** `/api/v1/urls/shorten`

Request body:
```json
{
  "originalUrl": "https://www.example.com/some/very/long/path"
}
```

Response:
```
http://localhost:8080/aB3xY
```

### Redirect to Original URL

**GET** `/{shortHash}`

Returns `302 Found` with the original URL in the response body. Also fires an async click event that saves analytics to the database.

### Rate Limit Test Endpoint

**GET** `/api/v1/secured/hello`

Returns a simple message if the request is within the rate limit. Useful for testing.

## Rate Limiting

All endpoints are rate-limited to **10 requests per minute per IP address**. The limit is enforced using a token bucket stored in Redis, making it compatible with clustered/multi-instance deployments.

**When within the limit**, the response includes:
```
X-Rate-Limit-Remaining: 8
```

**When the limit is exceeded**, the response is:

Status: `429 Too Many Requests`
```json
{
    "status": 429,
    "error": "Too Many Requests",
    "message": "You have exhausted your API Request Quota",
    "retryAfterSeconds": 45
}
```
Header:
```
X-Rate-Limit-Retry-After-Seconds: 45
```

Client IP is extracted from the `X-Forwarded-For` header when present, so it works correctly behind proxies and load balancers.

## Project Structure

```
src/main/java/com/ratelimiting/rulshortener/
├── config/
│   ├── RateLimitConfig.java        # Bucket4j ProxyManager + Redis/Lettuce setup
│   └── SecurityConfig.java         # Spring Security filter chain
├── controller/
│   ├── UrlShortenerController.java  # POST /api/v1/urls/shorten
│   ├── RedirectController.java      # GET /{shortHash}
│   └── TestController.java          # GET /api/v1/secured/hello
├── dto/
│   └── UrlRequest.java              # Request record with validation
├── event/
│   └── ClickEvent.java              # Event fired on each redirect
├── filter/
│   └── RateLimitFilter.java         # OncePerRequestFilter for rate limiting
├── model/
│   ├── UrlEntity.java               # urls table entity
│   └── AnalyticsEntity.java         # analytics table entity
├── repository/
│   ├── UrlRepository.java
│   └── AnalyticsRepository.java
├── service/
│   ├── Base62Encoder.java           # Encodes/decodes DB IDs to short hashes
│   ├── RateLimiterService.java      # Resolves per-IP token buckets from Redis
│   ├── UrlRedirectService.java      # Lookup with Redis cache fallback to DB
│   └── AnalyticsWorker.java         # Async event listener — saves click data
└── RateLimitingApplication.java
```

## How It Works

1. **Shortening**: A URL is saved to MySQL, its auto-generated ID is Base62-encoded into a short hash (e.g., `aB3xY`), and the full short URL is returned.
2. **Redirecting**: The short hash is decoded back to a DB ID. The original URL is fetched from a Redis cache first; on a miss it queries MySQL and caches the result for 24 hours.
3. **Analytics**: On every redirect, a `ClickEvent` is published and handled asynchronously by `AnalyticsWorker`, which writes the IP, User-Agent, and timestamp to the `analytics` table without blocking the response.
4. **Rate Limiting**: Every incoming request passes through `RateLimitFilter`. It resolves a Redis-backed token bucket for the client's IP, tries to consume one token, and either passes the request through or returns a `429` response.

## License

MIT
