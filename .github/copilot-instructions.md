# ERP System - AI Coding Assistant Instructions

## Architecture Overview

**Single Backend Architecture**: Java Spring Boot backend (erp-core-spring) handles core ERP and the Finance Department module. React provides the frontend UI.

### Core Components

- **erp-core-spring**: Multi-module Maven parent project with 4 modules:
  - `model/`: Shared JPA entities, enums, exceptions, mappers. Central data definitions.
  - `UserService/`: Authentication (JWT), user management, Spring Security config. Runs on port 8081.
  - `ViewAndControllers/`: HR module with business logic controllers. Called "LookUps Module" in pom.xml.
  - `ERPMain/`: Aggregator module that wires all services via `@ComponentScan` and `@EntityScan`.

- **Finance module**: Implemented in Spring Boot under `UserService` and shared model module. Handles accounting, invoicing, ledgers, and financial reports.

### Technology Stack

- **Backend**: Spring Boot 3.2.5, Java 21, Jakarta/Hibernate, MapStruct mappers, Lombok
- **DB**: Oracle (primary). Config: `c:\Users\Devoe\ErpSystem\erp-core-spring\UserService\src\main\resources\application.properties`
- **Security**: JWT token auth, Spring Security in UserService
- **Frontend**: React 18

## Key Architectural Decisions

- **Model module is not a Spring Boot app** — it's a library jar containing only entities/mappers/enums. Dependencies only: spring-context, jakarta.persistence, mapstruct, lombok.
- **ViewAndControllers has no security** — security is delegated to UserService. Configure Spring Security filters if needed.
- **ERPMain is the orchestrator** — it imports UserService, ViewAndControllers, and model modules and scans all packages. Run/debug from ERPMain.
- **Component scans are explicit** — packages are hardcoded in `ErpApplication.java` to avoid classpath scanning issues.

## Critical Developer Workflows

### Building

```bash
cd erp-core-spring
./mvnw clean install  # Full build all modules
./mvnw -pl ERPMain clean package  # Build only ERPMain (includes dependencies)
```

### Running

- **Spring services**: `java -jar ERPMain/target/ERPMain-1.0.0.jar` or run `ErpApplication.main()` in IDE (port 8081)
- **Finance module**: Runs within Spring Boot (ERPMain) on port 8081

### Database

- **Oracle** (Spring Boot): Connection string is `jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1` (user: `userGrade`, password: `user1234`)
  - **DDL strategy**: `spring.jpa.hibernate.ddl-auto=validate` (entities must match schema, no auto-generation)
  - **Dialect**: `OracleDialect` with standard physical naming strategy


### Testing & Debugging

- IDE breakpoints work best in Spring modules; Maven Surefire tests via `./mvnw test`
- JWT tokens expire in 86400000ms (24 hours); secret is `mySecretKey`
- Mail service configured with iCloud SMTP (see application.properties); don't log credentials

## Project-Specific Patterns & Conventions

### MapStruct Mapper Usage

The `model` module defines mappers for DTO ↔ Entity conversion. Example pattern:
```java
// Entities live in com.company.erp.erp.entites
// Mappers defined in com.company.erp.erp.mapper
// UserService and ViewAndControllers inject and use them
```

### Package Structure

- `com.company.erp.erp.*`: Core model (entities, enums, exceptions, mappers)
- `com.company.userService.*`: User, auth, security
- `com.company.userService.HrModule.*`: HR/business domain
- `com.company.main`: Orchestrator (ERPMain)

### Lombok & Build

All modules use Lombok (v1.18.30). Ensure IDE has Lombok plugin installed. Annotation processor included in pom.xml.

## Cross-Module Integration Points

1. **UserService → model**: Depends on shared entities and mappers
2. **ViewAndControllers → UserService + model**: Imports both for controllers and entities
3. **ERPMain**: Wires UserService repositories (`@EnableJpaRepositories` on two packages), entity scans model package, and component-scans all 4 base packages
4. **Finance module ↔ core ERP**: Finance endpoints and services live inside Spring Boot and share the model module.

## Common Tasks & Where Code Lives

| Task | Location |
|------|----------|
| Add user entity or auth logic | `UserService/src/main/java/com/company/userService/` |
| Add HR/business entity | `model/src/main/java/com/company/erp/erp/entites/` |
| Add DTO mapper | `model/src/main/java/com/company/erp/erp/mapper/` |
| Add controller endpoint | `ViewAndControllers/src/main/java/com/company/userService/HrModule/` |
| Configure security | `UserService/src/main/java/com/company/userService/securityConfig/` |
| Debug full flow | Start from `ERPMain/src/main/java/com/company/main/ErpApplication.java` |
| Add finance entity (Invoice, Ledger, etc.) | `model/src/main/java/com/company/erp/erp/entites/` |
| Add finance REST endpoint | `UserService/src/main/java/com/company/userService/finance/` |
| Add finance DTOs | `UserService/src/main/java/com/company/userService/finance/dto/` |

## Gotchas & Non-Obvious Behavior

- **Module versions are mixed**: model is `0.0.1-SNAPSHOT`, others are `1.0.0`. Be careful with version strings in pom.xml.
- **ViewAndControllers pom has security commented out** — likely disabled for dev. Re-enable carefully with UserService security config.
- **Oracle DDL is locked** (`validate` mode) — schema changes require DBA scripts, not JPA auto-generation.
- **Finance endpoints are part of Spring Boot** — no separate service or port is used for finance.

## Environment & Dependencies

### Spring Boot / Java
- **Java**: 21 (set in parent pom.xml)
- **Maven**: 3.6+ (use ./mvnw wrapper)
- **Oracle JDBC**: ojdbc11 v23.3.0.23.09

### Finance Module
- **Runs in Spring Boot**: No separate runtime, CLI, or database setup required beyond ERPMain

---

**Last Updated**: February 2026  
**Maintained By**: Development Team  
For questions on architecture decisions, consult the root pom.xml and ErpApplication.java component scan configuration.
