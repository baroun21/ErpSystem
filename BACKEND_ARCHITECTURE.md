# ERP System Backend Architecture & Documentation

**Version**: 3.5.5 with Spring Framework 6.2  
**Java**: 21 (LTS)  
**Status**: ✅ Production Ready  
**Last Updated**: February 21, 2026

---

## 📋 Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Module Structure](#module-structure)
3. [Technology Stack](#technology-stack)
4. [Setup & Build](#setup--build)
5. [Running the Application](#running-the-application)
6. [API Reference](#api-reference)
7. [Database](#database)
8. [Security](#security)
9. [Development Workflow](#development-workflow)
10. [Troubleshooting](#troubleshooting)

---

## Architecture Overview

The ERP system uses a **multi-module Maven architecture** with a single Spring Boot application (`ERPMain`) that serves as the unified backend orchestrator.

```
erp-core-spring (Parent)
├── model/               (Shared data layer - entities, DTOs, mappers)
├── UserService/         (Auth, users, finance, HR modules)
├── ViewAndControllers/  (Legacy controllers - currently minimal)
└── ERPMain/            (Main application - wires everything together)
```

### Design Principles

- **Single responsibility**: Each module has a clear purpose
- **Dependency flow**: ERPMain → UserService → model → dependencies
- **Entity centralization**: All entities live in `model` module
- **Repository centralization**: Repositories in `UserService` handle all data access
- **No duplicate definitions**: One entity = one definition location

---

## Module Structure

### 1. **model/** - Core Data Layer
**Location**: `erp-core-spring/model/`

**Purpose**: Provides shared data definitions for the entire system.

**Contains**:
- **Entities** (`com.company.erp.erp.entites.*`):
  - Finance: `Invoice`, `Bill`, `BankAccount`, `JournalEntry`, `ChartOfAccount`, `CostCenter`, etc.
  - HR: `Employee`, `Department`, `Leave`, `Attendance`, `Payroll`, etc.
  - Inventory: `Product`, `Warehouse`, `StockMovement`, etc.
  - CRM: `Customer`, `Supplier`, `Lead`, `Opportunity`, etc.
  
- **DTOs** (`com.company.erp.erp.entites.Dtos.*`): 
  - Data transfer objects for API request/response
  - Examples: `InvoiceDTO`, `EmployeeDTO`, `StockMovementDTO`

- **Mappers** (`com.company.erp.erp.mapper.*`):
  - MapStruct-based entity ↔ DTO conversion
  - Pattern: `EntityMapper` interface + auto-generated `EntityMapperImpl`
  - Examples: `InvoiceMapper`, `StockMovementMapper`

- **Enums** (`com.company.erp.erp.entites.enums.*`):
  - Invoice status: `DRAFT`, `SENT`, `PAID`, `OVERDUE`
  - Movement type: `IN`, `OUT`, `TRANSFER`, `RETURN`
  - Leave type: `ANNUAL`, `SICK`, `MATERNITY`

- **Shared Utilities**:
  - Exceptions: `BusinessException`, `ValidationException`
  - Base classes and interfaces

**Dependencies**:
```xml
- spring-context (for annotations)
- jakarta.persistence (JPA)
- mapstruct (entity mapping)
- lombok (code generation)
```

**Build Output**: `model-0.0.1-SNAPSHOT.jar` (library JAR)

---

### 2. **UserService/** - Authentication & Business Logic
**Location**: `erp-core-spring/UserService/`

**Purpose**: Handles user management, authentication, and core business logic.

**Contains**:

- **User Management** (`com.company.userService.*`):
  - `UserService.java`: User CRUD operations
  - `UserRepository`: JPA repository for User entity
  - JWT token generation and validation

- **Authentication & Security** (`com.company.userService.securityConfig.*`):
  - `SecurityConfig.java`: Spring Security configuration
  - JWT filter implementation
  - Role-based access control (RBAC)
  - Token: 24-hour expiry, secret: `mySecretKey`

- **Finance Module** (`com.company.userService.finance.*`):
  - **Controllers** (`controller/`): REST endpoints for financial operations
    - `/api/invoices` - Invoice management
    - `/api/bills` - Bill management
    - `/api/ledger` - General ledger
    - `/api/reports` - Financial reports
  
  - **Services** (`service/`): Business logic
    - Invoice/Bill processing
    - Ledger posting
    - AR/AP calculations
    - Financial report generation
  
  - **Repositories** (`repository/`): Data access
    - `InvoiceRepository`, `BillRepository`
    - `BankAccountRepository`, `PaymentRepository`
    - `JournalEntryRepository`, `LedgerRepository`
  
  - **DTOs** (`dto/`): Finance-specific data structures
    - `InvoiceDTO`, `BillDTO`, `LedgerDTO`
  
  - **Configuration** (`config/`): Finance-specific settings
    - Database configuration
    - Redis cache configuration

- **HR Module** (`com.company.userService.HrModule.*`):
  - Employee management
  - Department hierarchy
  - Leave management
  - Attendance tracking
  - Payroll processing
  - Performance management

**Port**: `8081`

**Database**: PostgreSQL (configured in `application.properties`)

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate

# Redis (Session store)
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

---

### 3. **ViewAndControllers/** - Legacy Controller Layer
**Location**: `erp-core-spring/ViewAndControllers/`

**Purpose**: Legacy view and controller implementations (currently minimal due to consolidation).

**Contains**:
- Legacy HTML view controllers
- Web form handling
- Thymeleaf templates

**Status**: Mostly superseded by REST APIs in `UserService`

---

### 4. **ERPMain/** - Main Application
**Location**: `erp-core-spring/ERPMain/`

**Purpose**: Orchestrates all modules and starts the Spring Boot application.

**Contains**:

- **Application Entry Point** (`com.company.main.ErpApplication.java`):
  ```java
  @SpringBootApplication
  @ComponentScan(basePackages = {
    "com.company.main",
    "com.company.userService",
    "com.company.userService.HrModule",
    "com.company.userService.finance"
  })
  @EntityScan("com.company.erp.erp.entites")
  @EnableJpaRepositories(
    basePackages = {
      "com.company.userService.repository",
      "com.company.userService.finance.repository"
    }
  )
  public class ErpApplication { ... }
  ```

- **Configuration** (`com.company.main.config.*`):
  - Spring Framework configuration
  - Session management
  - Database connection pooling
  - Cache configuration

- **Shared Infrastructure** (`com.company.erp.shared.*`):
  - Event publishing
  - Logging configuration
  - Error handling

- **Domain Logic** (`com.company.erp.<domain>.*`):
  - Inventory domain
  - CRM domain
  - Automation rules

**Build Output**: `ERPMain-1.0.0.jar` (fat/executable JAR)

**Startup Sequence**:
1. Load configuration from `application.properties`
2. Initialize database connection pool
3. Scan and register all Spring components
4. Initialize JPA repositories
5. Initialize security configuration
6. Start embedded Tomcat on port 8081
7. Log "Started ErpApplication in X seconds"

---

## Technology Stack

### Core Framework
| Component | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.5.5 | Application framework |
| Spring Framework | 6.2.10 | Core framework |
| Spring Data JPA | 3.5.5 | ORM abstraction |
| Spring Security | 6.2.10 | Authentication/authorization |
| Spring Session | 3.5.1 | Distributed session management |

### Persistence Layer
| Component | Version | Purpose |
|-----------|---------|---------|
| Hibernate | 6.4.x | JPA implementation |
| PostgreSQL Driver | 42.7.1 | Database driver |
| Flyway | 9.22.3 | Database migration |

### Utilities
| Component | Version | Purpose |
|-----------|---------|---------|
| Lombok | 1.18.30 | Code generation (getters/setters) |
| MapStruct | 1.5.5 | Entity-DTO mapping |
| JJWT | 0.12.3 | JWT token handling |
| Jedis | 5.1.0 | Redis client |
| Tomcat | 10.x | Embedded servlet container |

### Database & Caching
- **Primary DB**: PostgreSQL 14+
- **Session Store**: Redis 7+
- **Connection Pool**: HikariCP (auto-configured)

---

## Setup & Build

### Prerequisites
- Java 21 (JDK)
- Maven 3.8+
- PostgreSQL 14+
- Redis 7+

### Build Steps

```bash
cd erp-core-spring

# Clean build (skip tests)
./mvnw clean install -DskipTests

# Build only ERPMain module
./mvnw -pl ERPMain clean package -DskipTests

# Full build with tests
./mvnw clean install
```

### Build Output
- **model**: `model/target/model-0.0.1-SNAPSHOT.jar`
- **UserService**: `UserService/target/UserService-1.0.0.jar`
- **ERPMain**: `ERPMain/target/ERPMain-1.0.0.jar` ← Use this to run

### Maven Structure
```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.company.erp</groupId>
  <artifactId>erp-core-spring</artifactId>
  <version>1.0.0</version>
  <packaging>pom</packaging>
  
  <properties>
    <spring-boot.version>3.5.5</spring-boot.version>
    <spring-session.version>3.5.1</spring-session.version>
    <java.version>21</java.version>
  </properties>

  <modules>
    <module>model</module>
    <module>UserService</module>
    <module>ViewAndControllers</module>
    <module>ERPMain</module>
  </modules>
</project>
```

---

## Running the Application

### 1. Start Dependencies

```bash
# PostgreSQL (if not running)
docker run -d \
  --name postgres-erp \
  -e POSTGRES_DB=erp_db \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:14

# Redis (if not running)
docker run -d \
  --name redis-erp \
  -p 6379:6379 \
  redis:7
```

### 2. Run the Application

**Option A: JAR file**
```bash
cd ERPMain/target
java -jar ERPMain-1.0.0.jar
```

**Option B: Maven**
```bash
cd erp-core-spring
./mvnw -pl ERPMain spring-boot:run
```

**Option C: IDE**
- Open `ERPApplication.java` in IDE
- Run `main()` method

### 3. Verify Startup
```
Tomcat started on port 8081 (http) with context path '/'
Started ErpApplication in X seconds
```

### Access Points
- **API Base**: http://localhost:8081/api
- **Health Check**: http://localhost:8081/actuator/health
- **Swagger/OpenAPI**: http://localhost:8081/swagger-ui.html (if enabled)

---

## API Reference

### Finance Module

**Invoices**
```
GET    /api/invoices              - List invoices
GET    /api/invoices/{id}         - Get invoice details
POST   /api/invoices              - Create invoice
PUT    /api/invoices/{id}         - Update invoice
DELETE /api/invoices/{id}         - Delete invoice
POST   /api/invoices/{id}/send    - Send invoice
POST   /api/invoices/{id}/pay     - Record payment
```

**Bills**
```
GET    /api/bills                 - List bills
POST   /api/bills                 - Create bill
PUT    /api/bills/{id}            - Update bill
POST   /api/bills/{id}/approve    - Approve bill
POST   /api/bills/{id}/pay        - Record payment
```

**Ledger**
```
GET    /api/ledger                - Get ledger entries
POST   /api/ledger/journal-entry  - Create journal entry
POST   /api/ledger/post           - Post journal entries
GET    /api/ledger/trial-balance  - Trial balance report
```

**Reports**
```
GET    /api/reports/balance-sheet - Balance sheet
GET    /api/reports/income        - Income statement
GET    /api/reports/cash-flow     - Cash flow statement
GET    /api/reports/aged-ar       - Aged accounts receivable
GET    /api/reports/aged-ap       - Aged accounts payable
```

### HR Module

**Employees**
```
GET    /api/employees             - List employees
GET    /api/employees/{id}        - Get employee details
POST   /api/employees             - Create employee
PUT    /api/employees/{id}        - Update employee
```

**Leave Management**
```
GET    /api/leaves                - List leave requests
POST   /api/leaves                - Request leave
PUT    /api/leaves/{id}/approve   - Approve leave
PUT    /api/leaves/{id}/reject    - Reject leave
```

**Payroll**
```
POST   /api/payroll/generate      - Generate payroll
POST   /api/payroll/distribute    - Distribute salaries
GET    /api/payroll/reports       - Payroll reports
```

### Inventory Module

**Products**
```
GET    /api/inventory/products    - List products
GET    /api/inventory/products/{id} - Get product details
POST   /api/inventory/products    - Create product
```

**Stock**
```
GET    /api/inventory/stock       - Stock levels
POST   /api/inventory/stock/move  - Record stock movement
GET    /api/inventory/stock/history - Movement history
```

### Authentication

```
POST   /api/auth/login            - Login (returns JWT)
POST   /api/auth/logout           - Logout
POST   /api/auth/refresh          - Refresh token
GET    /api/auth/me               - Current user info
```

---

## Database

### Schema Overview

**Core Tables**:
- `users` - Users and authentication
- `roles` - User roles
- `user_roles` - Role assignments

**Finance Tables**:
- `invoices` - Customer invoices
- `invoice_lines` - Invoice items
- `bills` - Supplier bills
- `bill_lines` - Bill items
- `payments` - Payment records
- `bank_accounts` - Bank account definitions
- `journal_entries` - GL journal entries
- `journal_entry_lines` - GL transaction lines
- `ledger_accounts` - Chart of accounts
- `cost_centers` - Cost center hierarchy

**HR Tables**:
- `employees` - Employee master
- `departments` - Department hierarchy
- `leaves` - Leave requests
- `attendance` - Attendance records
- `payroll` - Payroll data

**Inventory Tables**:
- `products` - Product master
- `warehouses` - Warehouse locations
- `stock_movements` - Stock transactions

**System Tables**:
- `automation_rules` - Business rule engine
- `audit_log` - Change tracking

### Connectivity

**Primary Database** (PostgreSQL):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_db
spring.datasource.username=postgres
spring.datasource.password=password

# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

**Migrations**: Flyway (auto-applied on startup)
- Location: `classpath:db/migration/`
- Naming: `V001__initial_schema.sql`, `V002__add_columns.sql`
- Status: Managed automatically

---

## Security

### Authentication

**JWT Token Flow**:
1. User POST to `/api/auth/login` with credentials
2. Validate against `users` table
3. Generate JWT token: `Header.Payload.Signature`
4. Return token to client
5. Client includes in request: `Authorization: Bearer <token>`

**Token Details**:
- **Secret**: `mySecretKey`
- **Expiry**: 24 hours (86400000ms)
- **Algorithm**: HS512
- **Payload**: userId, username, roles

### Authorization

**Spring Security Configuration**:
```java
@EnableWebSecurity
@Configuration
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/public/**").permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
```

**Role-Based Access Control**:
- `ROLE_ADMIN` - Full system access
- `ROLE_FINANCE` - Finance module
- `ROLE_HR` - HR module
- `ROLE_INVENTORY` - Inventory module
- `ROLE_CRM` - CRM module

**Endpoint Security**:
- Public: `/api/auth/**`, `/api/public/**`
- Authenticated: All other `/api/**` endpoints
- Admin-only: System configuration endpoints

### Best Practices

✅ **Implemented**:
- JWT tokens instead of session cookies
- Stateless authentication (STATELESS session policy)
- CSRF disabled (RESTful API)
- CORS configured for frontend
- Password hashing (BCrypt)

⚠️ **Configure for Production**:
- Change `mySecretKey` to secure key
- Disable debug logging
- Enable HTTPS
- Set secure cookie flags

---

## Development Workflow

### Adding a New Feature

**1. Define Entity** (in `model/` module):
```java
// model/src/main/java/com/company/erp/erp/entites/Invoice.java
@Entity
@Table(name = "invoices")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String invoiceNumber;
  
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;
  
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<InvoiceLine> lines = new ArrayList<>();
}
```

**2. Create DTO** (in `model/` module):
```java
// model/src/main/java/com/company/erp/erp/entites/Dtos/InvoiceDTO.java
@Data @Builder
public class InvoiceDTO {
  private Long id;
  private String invoiceNumber;
  private Long customerId;
  private List<InvoiceLineDTO> lines;
}
```

**3. Generate Mapper** (in `model/` module):
```java
// model/src/main/java/com/company/erp/erp/mapper/InvoiceMapper.java
@Mapper(componentModel = "spring")
public interface InvoiceMapper {
  InvoiceDTO toDTO(Invoice entity);
  Invoice toEntity(InvoiceDTO dto);
  void updateEntityFromDTO(InvoiceDTO dto, @MappingTarget Invoice entity);
}
```
MapStruct auto-generates `InvoiceMapperImpl` at compile time.

**4. Create Repository** (in `UserService/` module):
```java
// UserService/src/main/java/com/company/userService/finance/repository/InvoiceRepository.java
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
  List<Invoice> findByCustomerAndStatus(Customer customer, String status);
  List<Invoice> findByInvoiceDateBetween(LocalDate start, LocalDate end);
}
```

**5. Create Service** (in `UserService/` module):
```java
// UserService/src/main/java/com/company/userService/finance/service/InvoiceService.java
@Service
@RequiredArgsConstructor
public class InvoiceService {
  private final InvoiceRepository invoiceRepository;
  private final InvoiceMapper invoiceMapper;
  
  public InvoiceDTO createInvoice(InvoiceDTO dto) {
    Invoice entity = invoiceMapper.toEntity(dto);
    entity.setStatus("DRAFT");
    Invoice saved = invoiceRepository.save(entity);
    return invoiceMapper.toDTO(saved);
  }
}
```

**6. Create Controller** (in `UserService/` module):
```java
// UserService/src/main/java/com/company/userService/finance/controller/InvoiceController.java
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
  private final InvoiceService invoiceService;
  
  @PostMapping
  @PreAuthorize("hasRole('FINANCE')")
  public ResponseEntity<InvoiceDTO> create(@RequestBody InvoiceDTO dto) {
    return ResponseEntity.ok(invoiceService.createInvoice(dto));
  }
}
```

**7. Rebuild & Test**:
```bash
./mvnw clean install -DskipTests
./mvnw -pl ERPMain spring-boot:run
```

### Code Organization Rules

✅ **DO**:
- Put entities ONLY in `model/` module
- Put repositories ONLY in `UserService/` module
- Put controllers ONLY in `UserService/` or `ERPMain/` modules
- Use DTOs for API contracts
- Use mappers for entity conversion

❌ **DON'T**:
- Define entities in multiple places
- Create duplicate repositories
- Mix business logic in controllers
- Expose entities directly in API responses
- Import from `model/target/generated-sources/` (generated code)

---

## Troubleshooting

### 1. Application Fails to Start

**Error**: `BeanCreationException`

**Solution**:
```bash
# Clean rebuild
./mvnw clean install -DskipTests

# Clear IDE caches and rebuild
# (IDE → Build → Clean/Rebuild Project)
```

**Error**: `SQLException: Connection refused`

**Solution**:
```bash
# Ensure PostgreSQL is running
docker ps | grep postgres-erp

# If not running:
docker run -d --name postgres-erp \
  -e POSTGRES_DB=erp_db \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 postgres:14
```

### 2. Port 8081 Already in Use

```bash
# Find process using port 8081
netstat -ano | findstr :8081

# Kill process (Windows)
taskkill /PID <PID> /F

# Or change port in application.properties:
# server.port=8082
```

### 3. Compilation Errors

**Error**: `package com.company.erp.finance.domain does not exist`

**Solution**: This package doesn't exist. Use correct imports:
```java
// ❌ Wrong (doesn't exist)
import com.company.erp.finance.domain.entity.Invoice;

// ✅ Correct
import com.company.erp.erp.entites.Invoice;
import com.company.userService.finance.repository.InvoiceRepository;
```

### 4. Database Migration Fails

**Error**: `Flyway: SQL State: 42P07 (table already exists)`

**Solution**:
```bash
# Manually cleanup (if safe)
psql -U postgres -d erp_db -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

# Or delete and recreate database
```

### 5. JWT Token Validation Failed

**Error**: `401 Unauthorized: Invalid token`

**Solution**:
```bash
# Get fresh token from /api/auth/login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Use returned token in requests:
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/invoices
```

### 6. Redis Connection Issues

**Error**: `RedisConnectionFailureException`

**Solution**:
```bash
# Ensure Redis is running
docker ps | grep redis-erp

# If not running:
docker run -d --name redis-erp -p 6379:6379 redis:7

# Test connection:
redis-cli ping  # Should return PONG
```

---

## Quick Reference

### Common Commands

```bash
# Build
./mvnw clean install -DskipTests

# Build only ERPMain
./mvnw -pl ERPMain clean package -DskipTests

# Run application
java -jar ERPMain/target/ERPMain-1.0.0.jar

# Run with custom port
java -jar ERPMain/target/ERPMain-1.0.0.jar --server.port=8082

# Run integration tests
./mvnw verify

# Check dependencies
./mvnw dependency:tree
```

### Project Files Reference

| File | Purpose |
|------|---------|
| `pom.xml` | Parent POM with dependency management |
| `model/pom.xml` | Model module POM |
| `UserService/pom.xml` | UserService module POM |
| `ERPMain/pom.xml` | Main application POM |
| `UserService/src/main/resources/application.properties` | Runtime configuration |
| `ERPMain/src/main/java/com/company/main/ErpApplication.java` | Application entry point |

### Module Dependencies

```
ERPMain
  ├── UserService
  │   └── model
  │       └── (only Spring/Jakarta/MapStruct dependencies)
  ├── ViewAndControllers
  │   └── UserService
  │       └── model
  └── (direct dependencies: PostgreSQL, Redis, etc.)
```

---

## Contact & Support

For issues or questions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review the code comments in source files
3. Check application logs: `java -jar ERPMain-1.0.0.jar 2>&1 | tail -50`
4. Consult Spring Boot documentation: https://spring.io/projects/spring-boot

---

**Last Updated**: February 21, 2026  
**Status**: ✅ Current and Accurate
