# Finder Application Documentation

Finder is a web application for managing academic resources, student records, course enrollments, attendance, and department staff. It is built using Java 21, Spring Boot 4.0.7, Spring Security 6, Thymeleaf, and MySQL 8.0.

---

## Contents
- [1. Technology Stack](#1-technology-stack)
- [2. Application Structure and Package Hierarchy](#2-application-structure-and-package-hierarchy)
- [3. SOLID Architecture Implementation](#3-solid-architecture-implementation)
- [4. Security and Access Control](#4-security-and-access-control)
- [5. Database Schema and Initial Data](#5-database-schema-and-initial-data)
- [6. Running the Application](#6-running-the-application)
- [7. Compilation and Test Commands](#7-compilation-and-test-commands)

---

## 1. Technology Stack

| Layer | Tool / Library | Version | Note |
| :--- | :--- | :--- | :--- |
| **Java SDK** | OpenJDK / Temurin | 21 | Target JVM runtime |
| **Backend Framework** | Spring Boot | 4.0.7 | Core web framework |
| **Security** | Spring Security | 6.x | Form authentication and RBAC |
| **Persistence** | Spring Data JPA / Hibernate | 6.x | Database ORM |
| **Template Engine** | Thymeleaf | 3.x | HTML rendering |
| **Client Assets** | Bootstrap / FontAwesome | 5.3 / 6.x | Loaded via CDN in HTML templates |
| **Database** | MySQL | 8.0 | Primary relational database |
| **HTTP Client** | Spring WebClient | WebFlux | Calls external OpenWeather endpoints |
| **Containers** | Docker / Docker Compose | 24+ / v2 | Container deployment setup |
| **Build Tool** | Apache Maven | 3.9 | Project build and dependency management |

---

## 2. Application Structure and Package Hierarchy

```
com.market.finder
├── FinderApplication.java
├── config             # Spring Security, MVC, WebClient, and cache configurations
├── controller         # MVC web controllers and REST endpoints
├── dto                # Data transfer objects (WeatherDto)
├── entity             # JPA domain entities and composite key IDs
├── event              # Application event listeners (Cache warmer)
├── filter             # Servlet filters (Logging filter)
├── interceptor        # Spring HandlerInterceptors and route evaluators
├── repository         # Spring Data JPA repositories
├── security           # Security context facade and UserDetailsService
└── service            # Service interfaces, implementations, and helpers
```

---

## 3. SOLID Architecture Implementation

The project code is organized according to standard SOLID design principles across packages and classes:

### 3.1 Single Responsibility Principle (SRP)
- **Controllers**: Controllers in `com.market.finder.controller` handle request mappings, model binding, and template views. They delegate validation, role checks, and database modifications to service classes.
- **Composite Key Preparation**: The `Attendance`, `Enrollment`, and `Gradebook` entities contain a `prepareId()` method to synchronize composite primary key fields before saving.
- **Weather Services**: OpenWeather API logic is divided across distinct classes:
  - `OpenWeatherApiClient`: Builds endpoint URLs.
  - `OpenWeatherJsonParser`: Converts raw JSON strings into `WeatherDto` instances.
  - `DefaultWeatherFallbackProvider`: Returns fallback weather values when network calls fail.

### 3.2 Open/Closed Principle (OCP)
- **Route Authorization**: `RoleAccessInterceptor` delegates path matching to `RouteAccessEvaluator`. New route rules can be added by modifying the evaluator implementation without changing the interceptor class.
- **Cache Deserialization**: `CacheBackupServiceImpl` reads a list of `CacheValueDeserializerStrategy` beans. Supporting new cached types requires implementing the strategy interface without altering backup logic.
- **Cache Logging**: `CacheConfig` wraps Spring's `Cache` implementation using `LoggingCacheWrapper` to log cache hits and misses without altering framework code.

### 3.3 Liskov Substitution Principle (LSP)
- **Base Service**: Domain services (`StudentServiceImpl`, `CourseServiceImpl`, `RoleServiceImpl`, `UserServiceImpl`) extend `BaseServiceImpl<T, ID, R>` and implement `BaseService<T, ID>`.
- **Deprecated Aliases**: `WetherServiceImpl` extends `WeatherServiceImpl` to maintain backwards compatibility for existing code references.

### 3.4 Interface Segregation Principle (ISP)
- **Cache Service Split**: Cache operations are divided into distinct interfaces:
  - `CacheInspectorService`: Checks cache key counts and names.
  - `CacheOperatorService`: Handles clearing and pre-warming caches.
  - `CacheBackupService`: Exports and imports JSON cache dumps.
  - `CacheManagementService`: Combines the three interfaces for administrative UI controllers.

### 3.5 Dependency Inversion Principle (DIP)
- **Interface Inversion**: Controllers and security classes inject interface types (`SecurityContextFacade`, `WeatherService`, `HttpHelperService`, `UserService`, `RoleService`) rather than concrete implementation classes.

---

## 4. Security and Access Control

Spring Security handles form login and URL authorization. Endpoint access permissions are checked by `RoleAccessInterceptor` and `DefaultRouteAccessEvaluator`.

### Roles and Assigned Permissions

| Role Name | Description | Permissions |
| :--- | :--- | :--- |
| `ROLE_ADMIN` | Administrator | `READ`, `WRITE`, `DELETE`, `MANAGE_USERS` |
| `ROLE_INSTRUCTOR` | Instructor | `READ`, `WRITE` |
| `ROLE_STUDENT` | Student | `READ` |
| `ROLE_USER` | Regular User | `READ` |

### System Seed Accounts

| Username | Password | Role |
| :--- | :--- | :--- |
| `admin` | `admin123` | `ROLE_ADMIN` |
| `instructor1` | `password123` | `ROLE_INSTRUCTOR` |
| `student1` | `password123` | `ROLE_STUDENT` |
| `user1` | `password123` | `ROLE_USER` |

---

## 5. Database Schema and Initial Data

Database tables and initial seed records are defined in `sql/system-db.sql`.

### Table List
1. `department`: Academic departments.
2. `roles`: Security roles.
3. `users`: User login accounts.
4. `user_roles`: User role assignments.
5. `permissions`: System permissions.
6. `role_permissions`: Role permission assignments.
7. `instructor_detail`: Additional instructor information.
8. `instructor`: Instructor profiles.
9. `student`: Student profiles.
10. `student_detail`: Additional student information.
11. `course`: Course catalog.
12. `teaching_assignment`: Instructor course assignments.
13. `enrollment`: Student course enrollments.
14. `gradebook`: Assignment scores.
15. `attendance`: Daily attendance records.
16. `employee`: Corporate employee records.
17. `staff`: Support staff records.

---

## 6. Running the Application

### Option A: Docker Compose

Start the MySQL database and application containers together:

```bash
docker-compose up --build
```

The web application runs at `http://localhost:8080`.

### Option B: Local Maven Setup

1. Import `sql/system-db.sql` into a local MySQL instance:
   ```bash
   mysql -u root -p < sql/system-db.sql
   ```

2. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 7. Compilation and Test Commands

Run Maven commands offline to verify compilation:

```bash
# Compile application source files
mvn compile -o

# Compile unit test files
mvn test-compile -o
```

---

## License

MIT License.
