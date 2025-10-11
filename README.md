# 🎬 BookMyShow Backend

A robust **movie ticket booking system backend** built with **Spring Boot**, featuring advanced concurrency control to prevent double-booking scenarios.

---

## 🚀 Project Overview

This project is an academic implementation of a movie ticket booking platform similar to **BookMyShow**.  
It demonstrates **enterprise-level backend development practices** with a strong focus on **handling concurrent booking requests safely**.

---

## 🧩 Key Features

- **Thread-Safe Seat Booking:** Implements database transaction isolation to prevent race conditions  
- **User Authentication:** Secure sign-up and login with BCrypt password hashing  
- **Dynamic Pricing:** Different prices for different seat types and shows  
- **Real-Time Seat Availability:** Seat status management (Available / Blocked / Occupied)  
- **RESTful Architecture:** Clean separation of concerns using the Controller–Service–Repository pattern  

---

## 🏗️ Architecture

### 🧰 Technology Stack

| Technology | Version | Purpose |
|-------------|----------|----------|
| **Java** | 17 | Core programming language |
| **Spring Boot** | 3.5.6 | Application framework |
| **Spring Data JPA** | 3.5.6 | ORM and database operations |
| **Spring Security** | 3.5.6 | Authentication and password encryption |
| **MySQL** | 8.0+ | Relational database |
| **Lombok** | Latest | Boilerplate code reduction |
| **Maven** | 3.x | Dependency management |

---

## 📁 Project Structure

```bash
BookMyShow/
├── .idea/                        # IntelliJ IDEA configuration
├── .mvn/                         # Maven wrapper files
├── demo/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── MyFirstProject/demo/
│   │   │   │       ├── controller/              # HTTP request handlers
│   │   │   │       │   ├── BookingController.java
│   │   │   │       │   └── UserController.java
│   │   │   │       ├── services/                # Business logic layer
│   │   │   │       │   ├── BookingServices.java  ⭐ Concurrency
│   │   │   │       │   ├── UserService.java
│   │   │   │       │   └── PriceCalculator.java
│   │   │   │       ├── Repository/              # Data access layer
│   │   │   │       │   ├── UserRepository.java
│   │   │   │       │   ├── ShowRepository.java
│   │   │   │       │   ├── ShowSeatRepository.java
│   │   │   │       │   └── ShowSeatTypeRepository.java
│   │   │   │       ├── models/                  # Domain entities
│   │   │   │       │   ├── BaseModel.java
│   │   │   │       │   ├── User.java
│   │   │   │       │   ├── Booking.java
│   │   │   │       │   ├── Movie.java
│   │   │   │       │   ├── Show.java
│   │   │   │       │   ├── ShowSeat.java        ⭐ Critical
│   │   │   │       │   ├── ShowSeatType.java
│   │   │   │       │   ├── Seat.java
│   │   │   │       │   ├── SeatType.java
│   │   │   │       │   ├── Screen.java
│   │   │   │       │   ├── Theatre.java
│   │   │   │       │   ├── Region.java
│   │   │   │       │   ├── Payment.java
│   │   │   │       │   ├── Enums/ (BookingStatus, SeatStatus, etc.)
│   │   │   │       ├── DTO/
│   │   │   │       │   ├── BookMovieRequestDTO.java
│   │   │   │       │   ├── BookMovieResponseDTO.java
│   │   │   │       │   ├── SignUpRequestDTO.java
│   │   │   │       │   └── SignUpResponseDTO.java
│   │   │   │       ├── Exceptions/
│   │   │   │       │   ├── InvalidUserException.java
│   │   │   │       │   ├── InvalidShowException.java
│   │   │   │       │   └── ShowSeatNotAvailableException.java
│   │   │   │       └── BookMyShowApplication.java  # Main app class
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── BookMyShowApplicationTests.java
│   ├── target/                     # Compiled classes
│   ├── pom.xml                     # Maven configuration
│   ├── .gitignore
│   ├── mvnw / mvnw.cmd             # Maven wrapper
│   └── README.md                   # Project documentation
```

    
⚙️ Concurrency Control Implementation

🧠 The Problem: Race Conditions in Seat Booking

When multiple users try to book the same seat simultaneously:

| Time | User A                    | User B                                |
| ---- | ------------------------- | ------------------------------------- |
| T1   | Check seat A1 → AVAILABLE |                                       |
| T2   |                           | Check seat A1 → AVAILABLE             |
| T3   | Mark A1 as BLOCKED        |                                       |
| T4   |                           | Mark A1 as BLOCKED ❌ (Double booking) |
| T5   | Create booking            |                                       |
| T6   |                           | Create booking ❌                      |

✅ The Solution: SERIALIZABLE Transaction Isolation
```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public Booking bookMovie(Long userId, Long showId, List<ShowSeat> showSeatList) {
    // Critical section protected by database transaction
    // Only one transaction can execute at a time for the same seats
}
```


How It Works:

SERIALIZABLE Isolation Level:
The highest isolation level in databases — prevents dirty, non-repeatable, and phantom reads.
Transactions execute serially when accessing the same data.

Atomic Operations:
Check seat availability → Update seat status → Create booking.
Either all succeed or all fail (rollback).

Automatic Rollback:
If any exception occurs, the transaction is rolled back automatically.

🔒 Booking Flow with Concurrency Control
```
User A Request                User B Request
     |                             |
     ├─ Transaction START          ├─ Transaction START (WAITS)
     ├─ Lock acquired on ShowSeat  |
     ├─ Check seat → AVAILABLE     |
     ├─ Mark seat → BLOCKED        |
     ├─ Create booking             |
     ├─ COMMIT + Release Lock      |
                                   ├─ Lock acquired
                                   ├─ Check seat → BLOCKED
                                   └─ Exception thrown (seat unavailable)
```

🔐 Security Features
🔑 Password Security

BCrypt Hashing:
```java
// Signup: Password is hashed before storage
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
user.setPassword(encoder.encode(plainPassword));

// Login: Compare provided password with stored hash
boolean isValid = encoder.matches(inputPassword, storedHash);
```

Why BCrypt?

Slow by design → brute-force resistant

Automatic salting → unique hashes per user

Adaptive → cost factor can increase as hardware improves

🗄️ Database Schema
Core Tables
🧍 Users
```
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

**Shows**
```
sqlshows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id BIGINT FOREIGN KEY,
    screen_id BIGINT FOREIGN KEY,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
```
**ShowSeats (⭐ Critical for concurrency)**
```
sqlshow_seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    show_id BIGINT FOREIGN KEY,
    seat_id BIGINT FOREIGN KEY,
    seat_status INT,  -- 0: OCCUPIED, 1: AVAILABLE, 2: BLOCKED
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
```
**Bookings**
```
sqlbookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT FOREIGN KEY,
    show_id BIGINT FOREIGN KEY,
    booking_status INT,  -- 0: PENDING, 1: CONFIRMED, 2: CANCELLED
    price INT,
    time_of_booking TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
```
**Entity Relationships**
```
Region (1) ──────< (N) Theatre
Theatre (1) ─────< (N) Screen
Screen (1) ──────< (N) Seat
Seat (1) ────────< (N) ShowSeat
Show (1) ────────< (N) ShowSeat
Movie (1) ───────< (N) Show
Screen (1) ──────< (N) Show
User (1) ────────< (N) Booking
Show (1) ────────< (N) Booking
Booking (N) ─────> (M) ShowSeat
```

### Design Decisions

**Why DTOs (Data Transfer Objects)?**

API Stability: Internal models can change without breaking the API

Security: Expose only necessary fields to clients (hide sensitive data)

Validation: Separate validation logic from domain models

Flexibility: Request and response can have different structures

**Why Repository Pattern?**

Abstraction: Separates data access logic from business logic

Testability: Easy to mock repositories for unit testing

Maintainability: Database changes don't affect service layer

Spring Data JPA: Provides pre-built CRUD operations

**Why Service Layer?**

Business Logic: Centralized place for all business rules

Reusability: Services can be used by multiple controllers

Transaction Management: Natural place for @Transactional annotations

Testing: Easier to write unit tests for business logic

### Learning Outcomes

This project demonstrates understanding of:

**1. Concurrency Control**

Transaction isolation levels

Race condition prevention

ACID properties in practice

Database locking mechanisms

**2. Security Best Practices**

Password hashing with BCrypt

Protection against SQL injection (via JPA)

Input validation

Secure authentication flow

**3. Spring Boot Ecosystem**

Dependency injection

Spring Data JPA

Transaction management

Repository pattern

Entity relationships

**4. Software Architecture**

Layered architecture (Controller-Service-Repository)

Separation of concerns

DTO pattern

Exception handling strategy

**5. Database Design**

Entity-Relationship modeling

Normalization

Foreign key relationships

Indexing strategy

### Future Enhancements

**1. REST API Endpoints**

Add @RestController annotations

Implement proper HTTP status codes

Add request/response validation

**2. Payment Integration**

Integrate with Razorpay/Stripe

Handle payment callbacks

Implement payment retry logic

**3. Seat Unlocking**

Auto-unblock seats after timeout (e.g., 10 minutes)

Scheduled task to clean up expired bookings

**4. Enhanced Security**

JWT-based authentication

Role-based access control (Admin, User)

API rate limiting

**5. Caching**

Redis for session management

Cache frequently accessed data (movies, theaters)

**6. Notifications**

Email confirmation on booking

SMS for booking reminders

Push notifications

**7. Analytics**

Booking trends

Revenue reports

Popular movies/shows

**8. Testing**

Unit tests with JUnit 5Integration tests

Concurrency stress tests
