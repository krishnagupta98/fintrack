---
title: fintrack-backend
emoji: 💰
colorFrom: blue
colorTo: green
sdk: docker
pinned: false
---

# FinTrack 💰
> A production-ready personal expense tracking REST API built with Spring Boot.

---

## What is FinTrack?

FinTrack is a secure, scalable backend application that helps users track their personal expenses. It features real-time notifications, intelligent caching, and API rate limiting — built with the same patterns used in production systems.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3 |
| Database | PostgreSQL + Spring Data JPA |
| Caching | Redis + Spring Cache |
| Security | Spring Security + JWT |
| Async Processing | Spring @Async with custom thread pool |
| Rate Limiting | Redis Sorted Sets (sliding window algorithm) |
| Build Tool | Maven |

---

## Key Features

### 🔐 JWT Authentication
- Secure stateless authentication using JSON Web Tokens
- Every endpoint protected — users can only access their own data

### ⚡ Redis Caching
- Expense summaries cached per user using `@Cacheable`
- Cache automatically evicted on create, update, and delete using `@CacheEvict`
- Reduces database load significantly for read-heavy operations

### 🔔 Async Notifications
- High-value expense alerts (above ₹5000) sent asynchronously
- Custom thread pool executor (`notificationExecutor`) ensures non-blocking request handling
- Notification status tracked in DB — PENDING → SENT / FAILED

### 🚦 Rate Limiting
- Sliding window rate limiter using Redis Sorted Sets
- Max 5 requests per minute per user
- Prevents API abuse without any external library

### 📊 Expense Summary
- Category-wise breakdown of all expenses
- Total spending calculated per user

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Expenses
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/expenses` | Add new expense |
| GET | `/api/expenses` | Get all expenses |
| GET | `/api/expenses/{id}` | Get expense by ID |
| PUT | `/api/expenses/{id}` | Update expense |
| DELETE | `/api/expenses/{id}` | Delete expense |
| GET | `/api/expenses/summary` | Get category-wise summary |

### Notifications
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/notifications` | Get notification history |

---

## Architecture

```
Client Request
      ↓
JWT Security Filter
      ↓
Rate Limiter (Redis)
      ↓
Controller
      ↓
Service Layer
    ↓         ↓
Repository   Async Notification
(PostgreSQL)  (Thread Pool)
      ↓
Redis Cache
```

---

## How to Run

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Redis

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/krishnagupta2006/fintrack.git
cd fintrack

# 2. Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fintrack
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.redis.host=localhost
spring.redis.port=6379
jwt.secret=your_jwt_secret

# 3. Run the application
mvn spring-boot:run
```
### Run with Docker

```bash
# 1. Clone the repository
git clone https://github.com/krishnagupta2006/fintrack.git
cd fintrack

# 2. Start everything with Docker Compose
docker-compose up --build
```

App will be running at `http://localhost:8080`
> Redis and PostgreSQL are automatically started via docker-compose.

---

## What I Learned Building This

- How Spring Security filter chain works with JWT
- Redis data structures — Strings for cache, Sorted Sets for rate limiting
- How `@Async` works internally with thread pools
- SpEL (Spring Expression Language) for dynamic cache keys
- Importance of cache eviction strategy in write-heavy APIs

---

## Author

**Krishna Gupta**
- GitHub: [@krishnagupta2006](https://github.com/krishnagupta2006)

---

> Built with ❤️ to learn production-grade Spring Boot development#   f i n t r a c k 
 
 