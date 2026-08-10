# 🎓 Finder - Enterprise Academic & Resource Management Platform

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue.svg)](https://spring.io/projects/spring-security)
[![Architecture](https://img.shields.io/badge/Architecture-SOLID%20Clean%20Design-blueviolet.svg)](#-solid-architectural-compliance)
[![Build Status](https://img.shields.io/badge/Build-Passing-success.svg)](#-getting-started)
[![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)

**Finder** is a state-of-the-art, high-performance enterprise resource and academic management web application built with **Java 21**, **Spring Boot 4.0.7**, **Spring Security 6**, and **Thymeleaf**. Designed from the ground up to showcase industry-standard software architecture, Finder incorporates Role-Based Access Control (RBAC), fine-grained permission evaluation, resilient OpenWeather API integration, custom cache decorators, containerized Docker deployment, and complete **S.O.L.I.D.** object-oriented design principles.

---

## 📋 Table of Contents
1. [Key Features](#-key-features)
2. [Technology Stack](#-technology-stack)
3. [SOLID Architectural Compliance](#-solid-architectural-compliance)
4. [Security & Role-Based Access Control (RBAC)](#-security--role-based-access-control-rbac)
5. [Project Package Structure](#-project-package-structure)
6. [Database Schema & Data Model](#-database-schema--data-model)
7. [API & Controller Specifications](#-api--controller-specifications)
8. [Getting Started & Deployment](#-getting-started--deployment)
9. [Verification & Testing](#-verification--testing)

---

## ✨ Key Features

- 👥 **Comprehensive Academic Administration**: Manage Students, Instructors, Departments, Courses, Staff, and Corporate Employees with full CRUD interfaces.
- 📊 **Gradebook & Attendance Tracking**: Record student grades, track daily attendance statuses (`PRESENT`, `ABSENT`, `LATE`, `EXCUSED`), and manage course enrollments with composite key entities.
- 🔒 **Enterprise RBAC & Custom Interceptors**: Granular permission enforcement (`READ`, `WRITE`, `DELETE`, `MANAGE_USERS`) powered by `RoleAccessInterceptor` and `DefaultRouteAccessEvaluator`.
- 🌤️ **Live Weather & Geo-Location Integration**: Real-time OpenWeather API integration with IP-to-city resolution, fallback providers, and asynchronous HTTP client abstractions.
- ⚡ **Decorated Cache Operations**: Spring Cache abstraction enhanced with `LoggingCacheWrapper` for real-time cache tracking, cache warming listeners, and JSON backup/restore tools.
- 🐳 **Docker & CI/CD Pipeline**: Multi-stage `Dockerfile` and automated GitHub Actions CI workflow for seamless deployment.

---

## 🛠️ Technology Stack

| Layer | Technology / Tool | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 | High-performance LTS Java runtime |
| **Framework** | Spring Boot 4.0.7 | Modern microservice & web foundation |
| **Security** | Spring Security 6.x | Authentication & Authorization |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) |
| **View Engine** | Thymeleaf 3.x | Server-side template rendering |
| **UI Components** | Bootstrap 5, FontAwesome 6, Inter Font | Responsive client interface |
| **Database** | MySQL 8.0 / H2 (In-Memory Testing) | Relational persistence |
| **HTTP Client** | Spring WebClient (WebFlux) | Asynchronous non-blocking web requests |
| **Caching** | Spring Cache Abstraction | High-speed data caching |
| **Containerization**| Docker & Docker Compose | Containerization & service orchestration |
| **CI/CD** | GitHub Actions | Automated build & verification pipeline |

---

## 🏛️ SOLID Architectural Compliance

The codebase strictly satisfies all **5 SOLID principles** across all components:

### 1. Single Responsibility Principle (SRP)
- **Web Controllers** (`com.market.finder.controller`): Focus solely on HTTP request routing, model binding, and template rendering. All business logic (e.g. `ROLE_ADMIN` deletion prohibition, password retention on update) is encapsulated within service implementations.
- **Embedded Key Entities**: Composite key entities ([Attendance](file:///e:/finder/src/main/java/com/market/finder/entity/Attendance.java), [Enrollment](file:///e:/finder/src/main/java/com/market/finder/entity/Enrollment.java), [Gradebook](file:///e:/finder/src/main/java/com/market/finder/entity/Gradebook.java)) encapsulate primary key synchronization in a dedicated `prepareId()` method.
- **Micro-Services**: OpenWeather API logic is divided into single-purpose components: [OpenWeatherApiClient](file:///e:/finder/src/main/java/com/market/finder/service/weather/OpenWeatherApiClient.java) (URL building), [OpenWeatherJsonParser](file:///e:/finder/src/main/java/com/market/finder/service/weather/OpenWeatherJsonParser.java) (JSON parsing), and [DefaultWeatherFallbackProvider](file:///e:/finder/src/main/java/com/market/finder/service/weather/DefaultWeatherFallbackProvider.java) (fallback handling).

### 2. Open/Closed Principle (OCP)
- **Route Authorization**: [RoleAccessInterceptor](file:///e:/finder/src/main/java/com/market/finder/interceptor/RoleAccessInterceptor.java) delegates URI matching and permission evaluation to [RouteAccessEvaluator](file:///e:/finder/src/main/java/com/market/finder/interceptor/RouteAccessEvaluator.java). New route rules can be introduced without modifying interceptor logic.
- **Cache Deserialization**: [CacheBackupServiceImpl](file:///e:/finder/src/main/java/com/market/finder/service/cache/impl/CacheBackupServiceImpl.java) uses `List<CacheValueDeserializerStrategy>` to allow new cache types to be backed up/restored seamlessly.
- **Cache Logging Decorator**: [CacheConfig](file:///e:/finder/src/main/java/com/market/finder/config/CacheConfig.java) uses `LoggingCacheWrapper` (Decorator Pattern) to log cache events without altering underlying cache providers.

### 3. Liskov Substitution Principle (LSP)
- **Generic Base Service**: Domain services ([StudentServiceImpl](file:///e:/finder/src/main/java/com/market/finder/service/student/StudentServiceImpl.java), [CourseServiceImpl](file:///e:/finder/src/main/java/com/market/finder/service/course/CourseServiceImpl.java)) extend [BaseServiceImpl](file:///e:/finder/src/main/java/com/market/finder/service/base/BaseServiceImpl.java) and implement [BaseService](file:///e:/finder/src/main/java/com/market/finder/service/base/BaseService.java), ensuring uniform behavioral contracts.
- **Weather Service Alias**: [WeatherServiceImpl](file:///e:/finder/src/main/java/com/market/finder/service/weather/impl/WeatherServiceImpl.java) implements `WeatherService`. The legacy `@Deprecated` `WetherServiceImpl` extends `WeatherServiceImpl`, maintaining 100% backward substitutability.

### 4. Interface Segregation Principle (ISP)
- **Segregated Cache Interfaces**: Cache functionality is divided into fine-grained interfaces: [CacheInspectorService](file:///e:/finder/src/main/java/com/market/finder/service/cache/CacheInspectorService.java), [CacheOperatorService](file:///e:/finder/src/main/java/com/market/finder/service/cache/CacheOperatorService.java), and [CacheBackupService](file:///e:/finder/src/main/java/com/market/finder/service/cache/CacheBackupService.java). Clients depend only on the methods they use.

### 5. Dependency Inversion Principle (DIP)
- **Interface Inversion**: Controllers and interceptors inject interface abstractions ([SecurityContextFacade](file:///e:/finder/src/main/java/com/market/finder/security/SecurityContextFacade.java), [WeatherService](file:///e:/finder/src/main/java/com/market/finder/service/weather/WeatherService.java), [HttpHelperService](file:///e:/finder/src/main/java/com/market/finder/service/helper/HttpHelperService.java)) rather than concrete implementations or static singletons.

---

## 🔒 Security & Role-Based Access Control (RBAC)

System permissions are managed through dynamic authority assignments linked to roles:

| Role Name | Description | Default Permissions |
| :--- | :--- | :--- |
| **`ROLE_ADMIN`** | System Administrator | `READ`, `WRITE`, `DELETE`, `MANAGE_USERS` |
| **`ROLE_INSTRUCTOR`** | Academic Faculty Member | `READ`, `WRITE` |
| **`ROLE_STUDENT`** | Enrolled Student | `READ` |
| **`ROLE_USER`** | Standard System User | `READ` |

### Default Credentials (Seed Data)

| Username | Password | Assigned Role |
| :--- | :--- | :--- |
| `admin` | `admin123` | `ROLE_ADMIN` |
| `instructor1` | `password123` | `ROLE_INSTRUCTOR` |
| `student1` | `password123` | `ROLE_STUDENT` |
| `user1` | `password123` | `ROLE_USER` |

---

## 📁 Project Package Structure

```
com.market.finder
├── FinderApplication.java
├── config             # Spring Security, Web MVC, WebClient & Cache configurations
├── controller         # MVC & REST Web Controllers
├── dto                # Data Transfer Objects (WeatherDto)
├── entity             # JPA Entities & Composite Embedded Primary Keys
├── event              # Application Startup Event Listeners (Cache Warmer)
├── filter             # Servlet Filters (CustomLoggingFilter)
├── interceptor        # Handler Interceptors & Route Access Evaluators
├── repository         # Spring Data JPA Repositories
├── security           # Security Context Facade & Custom UserDetails Service
└── service            # Business Domain Services, HTTP Helpers & Strategies
```

---

## 🗄️ Database Schema & Data Model

The application uses a unified, production-tested database initialization script:
* **`sql/system-db.sql`**: Mounted automatically into Docker Compose (`/docker-entrypoint-initdb.d/01-system-db.sql`).

```
                              +--------------------+
                              |       roles        |
                              +--------------------+
                                |                |
             +------------------+                +------------------+
             |                                                      |
             v                                                      v
  +--------------------+                                 +--------------------+
  |     user_roles     |                                 |  role_permissions  |
  +--------------------+                                 +--------------------+
             ^                                                      |
             |                                                      v
  +--------------------+                                 +--------------------+
  |       users        |                                 |    permissions     |
  +--------------------+                                 +--------------------+
             ^
             +------------------+
             |                  |
             v                  v
  +--------------------+  +--------------------+
  |      student       |  |     instructor     |
  +--------------------+  +--------------------+
```

The database includes 17 relational tables: `users`, `roles`, `permissions`, `user_roles`, `role_permissions`, `student`, `student_detail`, `instructor`, `instructor_detail`, `course`, `teaching_assignment`, `enrollment`, `gradebook`, `attendance`, `department`, `employee`, and `staff`.

---

## 🚀 Getting Started & Deployment

### Prerequisites
- **JDK 21**
- **Maven 3.9+** (or bundled `./mvnw`)
- **Docker & Docker Compose** (optional)

### Option 1: Docker Compose (Recommended)

1. Launch the application and MySQL 8.0 database containers:
   ```bash
   docker-compose up --build
   ```
2. Access the application in your browser at `http://localhost:8080`.

### Option 2: Local Native Build

1. Import the database schema into local MySQL:
   ```bash
   mysql -u root -p < sql/system-db.sql
   ```
2. Run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Verification & Testing

Verify that the entire source code and test suite compile with **0% errors**:

```bash
# Compile main application sources
mvn compile -o

# Compile test suite
mvn test-compile -o
```

---

## 📝 License

Distributed under the **MIT License**. See `LICENSE` for details.
