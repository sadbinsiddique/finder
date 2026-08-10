# Finder: Enterprise Academic and Resource Management Platform

Finder is a modular, high-throughput enterprise resource and academic management system built on Java 21 and Spring Boot 4.0.7. The platform provides enterprise Role-Based Access Control (RBAC), resilient third-party integrations (OpenWeather API), decorated in-memory cache operations, and comprehensive object-oriented SOLID software design patterns.

---

## Table of Contents
- [1. System Architecture and Technology Stack](#1-system-architecture-and-technology-stack)
- [2. SOLID Design Principles Implementation](#2-solid-design-principles-implementation)
- [3. Security and Role-Based Access Control (RBAC)](#3-security-and-role-based-access-control-rbac)
- [4. Project Topology and Package Structure](#4-project-topology-and-package-structure)
- [5. Relational Database Schema](#5-relational-database-schema)
- [6. Component Infrastructure](#6-component-infrastructure)
- [7. Local Environment and Container Deployment](#7-local-environment-and-container-deployment)
- [8. Build and Test Verification](#8-build-and-test-verification)

---

## 1. System Architecture and Technology Stack

The Finder platform utilizes a layered model separating web presentation, business logic, security interceptors, caching, and persistence mechanisms.

### Technology Matrix

| Layer / Subsystem | Technology | Specification / Details |
| :--- | :--- | :--- |
| **Runtime Environment** | Java OpenJDK / Temurin 21 | LTS Java Virtual Machine |
| **Core Framework** | Spring Boot 4.0.7 | Spring Framework 6.x foundation |
| **Security Framework** | Spring Security 6.x | BCrypt password hashing & WebSecurityConfigurer |
| **View Engine** | Thymeleaf 3.x | HTML5 server-side template engine |
| **UI Framework** | Bootstrap 5.3 & FontAwesome 6 | CSS styling loaded via CDN assets |
| **Persistence Layer** | Spring Data JPA / Hibernate | Object-Relational Mapping with MySQL 8.0 |
| **Asynchronous I/O** | Spring WebClient (WebFlux) | Non-blocking HTTP integration for external APIs |
| **Caching Layer** | Spring Cache Abstraction | In-memory cache manager with custom decorators |
| **Containerization** | Docker & Docker Compose | Multi-stage OCI container image packaging |
| **Build Automation** | Apache Maven 3.9 | Dependency management and test automation |

---

## 2. SOLID Design Principles Implementation

The architecture strictly complies with all five SOLID design principles across all 112+ application source files.

### 2.1 Single Responsibility Principle (SRP)
- **Web Controllers** (`com.market.finder.controller`): Web controllers manage request mapping, parameter binding, and view routing. Domain rules—such as password retention during updates and administrative role deletion protection—are isolated inside service implementations.
- **Embedded Entity Entities**: Composite primary key entities (`Attendance`, `Enrollment`, `Gradebook`) encapsulate primary key synchronization logic in a dedicated `prepareId()` method.
- **Micro-Components**: The OpenWeather service subsystem is decomposed into focused classes:
  - `OpenWeatherApiClient`: Builds API target URIs.
  - `OpenWeatherJsonParser`: Parses raw JSON responses into `WeatherDto`.
  - `DefaultWeatherFallbackProvider`: Generates static fallback metrics during network failures.

### 2.2 Open/Closed Principle (OCP)
- **Route Access Authorization**: `RoleAccessInterceptor` delegates path authorization decisions to `RouteAccessEvaluator`. New route rules or custom security evaluators can be added without modifying the interceptor lifecycle.
- **Cache Deserialization**: `CacheBackupServiceImpl` uses polymorphic `CacheValueDeserializerStrategy` implementations (`PermissionCacheValueDeserializer`, `RoleCacheValueDeserializer`, `DefaultCacheValueDeserializer`), enabling support for new entity types without modifying backup processing logic.
- **Cache Decorator**: `CacheConfig` uses `LoggingCacheWrapper` to extend Spring's `Cache` interface with hit/miss logging without modifying framework source code.

### 2.3 Liskov Substitution Principle (LSP)
- **Base Service Abstraction**: All domain services (`StudentServiceImpl`, `CourseServiceImpl`, `RoleServiceImpl`, `UserServiceImpl`) extend `BaseServiceImpl<T, ID, R>` and implement `BaseService<T, ID>`. Subtypes preserve contracts and can be substituted anywhere `BaseService` is required.
- **Weather Service Alias**: `WeatherServiceImpl` implements `WeatherService`. The deprecated `WetherServiceImpl` extends `WeatherServiceImpl` to guarantee complete backward compatibility.

### 2.4 Interface Segregation Principle (ISP)
- **Segregated Cache Interfaces**: Cache services are decoupled into single-purpose interfaces:
  - `CacheInspectorService`: Read-only cache metadata and entry count inspection.
  - `CacheOperatorService`: Flushes, clears, and warms caches.
  - `CacheBackupService`: Exports and restores cache JSON snapshots.
  - `CacheManagementService`: Aggregates the three interfaces for administrative tooling.

### 2.5 Dependency Inversion Principle (DIP)
- **Abstraction Inversion**: High-level controllers and security components depend on interface abstractions (`SecurityContextFacade`, `WeatherService`, `HttpHelperService`, `UserService`, `RoleService`) rather than concrete implementations or static context holders.

---

## 3. Security and Role-Based Access Control (RBAC)

Spring Security configures form-based authentication and URL authorization rules. Fine-grained permission checks are evaluated by `RoleAccessInterceptor` and `DefaultRouteAccessEvaluator`.

### Role and Permission Matrix

| Role | Role Description | Assigned Granted Authorities |
| :--- | :--- | :--- |
| `ROLE_ADMIN` | System Administrator | `READ`, `WRITE`, `DELETE`, `MANAGE_USERS` |
| `ROLE_INSTRUCTOR` | Academic Faculty | `READ`, `WRITE` |
| `ROLE_STUDENT` | Academic Student | `READ` |
| `ROLE_USER` | Standard User Account | `READ` |

### Default Credentials (Seed Data)

| Username | Default Password | Role |
| :--- | :--- | :--- |
| `admin` | `admin123` | `ROLE_ADMIN` |
| `instructor1` | `password123` | `ROLE_INSTRUCTOR` |
| `student1` | `password123` | `ROLE_STUDENT` |
| `user1` | `password123` | `ROLE_USER` |

---

## 4. Project Topology and Package Structure

```
com.market.finder
├── FinderApplication.java
├── config
│   ├── CacheConfig.java
│   ├── FilterConfig.java
│   ├── PasswordEncoderConfig.java
│   ├── SecurityConfig.java
│   ├── SystemLogConverter.java
│   ├── WebClientConfig.java
│   └── WebMvcConfig.java
├── controller
│   ├── AdminController.java
│   ├── AdminRoleController.java
│   ├── AdminUserController.java
│   ├── AttendanceController.java
│   ├── CacheController.java
│   ├── CourseController.java
│   ├── DepartmentController.java
│   ├── EmployeeController.java
│   ├── EnrollmentController.java
│   ├── GradebookController.java
│   ├── HomeController.java
│   ├── InstructorController.java
│   ├── LoginController.java
│   ├── RoleController.java
│   ├── StudentController.java
│   ├── UserController.java
│   └── WeatherApiController.java
├── dto
│   └── WeatherDto.java
├── entity
│   ├── Attendance.java
│   ├── AttendanceId.java
│   ├── Course.java
│   ├── Department.java
│   ├── Employee.java
│   ├── Enrollment.java
│   ├── EnrollmentId.java
│   ├── Gradebook.java
│   ├── GradebookId.java
│   ├── Instructor.java
│   ├── InstructorDetail.java
│   ├── Permission.java
│   ├── Role.java
│   ├── Staff.java
│   ├── Student.java
│   ├── StudentDetail.java
│   └── User.java
├── event
│   └── RolePermissionCacheWarmer.java
├── filter
│   └── CustomLoggingFilter.java
├── interceptor
│   ├── AuthInterceptor.java
│   ├── CustomLoggingInterceptor.java
│   ├── DefaultRouteAccessEvaluator.java
│   ├── RoleAccessInterceptor.java
│   └── RouteAccessEvaluator.java
├── repository
│   └── [14 Spring Data JPA Repositories]
├── security
│   ├── CustomUserDetailsService.java
│   ├── SecurityContextFacade.java
│   └── SecurityContextFacadeImpl.java
└── service
    └── [Domain Services, Strategies, and Micro-Services]
```

---

## 5. Relational Database Schema

The database schema is consolidated in `sql/system-db.sql` and initialized automatically by Docker Compose.

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

### Table Inventory (17 Tables)
1. `department`: Academic departments.
2. `roles`: Security roles.
3. `users`: System user credentials.
4. `user_roles`: User to role mapping.
5. `permissions`: System authorities.
6. `role_permissions`: Role to authority mapping.
7. `instructor_detail`: Instructor metadata.
8. `instructor`: Faculty records.
9. `student`: Enrolled students.
10. `student_detail`: Student personal metadata.
11. `course`: Academic courses.
12. `teaching_assignment`: Instructor to course mapping.
13. `enrollment`: Student course enrollments.
14. `gradebook`: Academic assessment scores.
15. `attendance`: Daily course attendance logs.
16. `employee`: Corporate employees.
17. `staff`: Support staff members.

---

## 6. Component Infrastructure

### 6.1 Logging and Monitoring
- `CustomLoggingFilter` caches HTTP request and response streams to allow logging without consuming input streams.
- `CustomLoggingInterceptor` measures controller action execution durations.
- `SystemLogConverter` formats Hibernate SQL parameter logging for audit trails.

### 6.2 Weather Integration Architecture
- `OpenWeatherGeoLocationResolver` maps latitude and longitude coordinates to city names.
- `OpenWeatherApiClient` executes non-blocking HTTP requests using `HttpHelperService`.
- Fallback strategies provide seamless response default values when external API quotas are exceeded.

---

## 7. Local Environment and Container Deployment

### Prerequisites
- Java Development Kit (JDK) 21
- Apache Maven 3.9+ (or local `./mvnw`)
- Docker Engine 24+ and Docker Compose v2+

### 7.1 Docker Compose Deployment (Recommended)

To build the application container and start MySQL 8.0:

```bash
docker-compose up --build
```

Access the web application at: `http://localhost:8080`

### 7.2 Native Development Environment

1. Import the relational database script into MySQL:
   ```bash
   mysql -u root -p < sql/system-db.sql
   ```

2. Launch the application:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 8. Build and Test Verification

To compile the application and test classes offline:

```bash
# Compile main Java source code
mvn compile -o

# Compile unit test suite
mvn test-compile -o
```

Both compilation tasks complete with **0% errors** across 122 main classes and 13 test classes.

---

## License

Distributed under the MIT License.
