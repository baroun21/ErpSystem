# ERP System - AI Coding Assistant Instructions

## Architecture Overview

**Hybrid Microservices Architecture**: Java Spring Boot backend (erp-core-spring) handles core ERP, with a dedicated **Finance Department module** using Django + Django REST Framework (finance-service-django).

### Core Components

- **erp-core-spring**: Multi-module Maven parent project with 4 modules:
  - `model/`: Shared JPA entities, enums, exceptions, mappers. Central data definitions.
  - `UserService/`: Authentication (JWT), user management, Spring Security config. Runs on port 8081.
  - `ViewAndControllers/`: HR module with business logic controllers. Called "LookUps Module" in pom.xml.
  - `ERPMain/`: Aggregator module that wires all services via `@ComponentScan` and `@EntityScan`.

- **finance-service-django**: **Finance Department** microservice built with Django REST Framework. Handles accounting, invoicing, ledgers, and financial reports. Independent from Spring Boot core; communicates via REST API.

### Technology Stack

- **Backend**: Spring Boot 3.2.5, Java 21, Jakarta/Hibernate, MapStruct mappers, Lombok
- **DB**: Oracle (primary), SQLite (Django dev). Config: `c:\Users\Devoe\ErpSystem\erp-core-spring\UserService\src\main\resources\application.properties`
- **Security**: JWT token auth, Spring Security in UserService
- **Frontend/Django**: Django 6.0.1, DRF 3.16.1, PostgreSQL driver available

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
- **Django Finance service**: `cd finance-service-django && python manage.py runserver` (default port 8000; requires migrations: `python manage.py migrate`)

### Database

- **Oracle** (Spring Boot): Connection string is `jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1` (user: `userGrade`, password: `user1234`)
  - **DDL strategy**: `spring.jpa.hibernate.ddl-auto=validate` (entities must match schema, no auto-generation)
  - **Dialect**: `OracleDialect` with standard physical naming strategy
- **SQLite/PostgreSQL** (Django Finance): Config in `finance-service-django/config/settings.py`. Development uses SQLite; production-ready setup available for PostgreSQL via `psycopg2-binary` in requirements.txt

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
4. **Django Finance ↔ Spring Boot**: Planned REST API integration. Finance service will expose endpoints for invoicing, ledger queries, and reports that Spring Boot modules consume via HTTP calls. Not yet implemented.

## Common Tasks & Where Code Lives

| Task | Location |
|------|----------|
| Add user entity or auth logic | `UserService/src/main/java/com/company/userService/` |
| Add HR/business entity | `model/src/main/java/com/company/erp/erp/entites/` |
| Add DTO mapper | `model/src/main/java/com/company/erp/erp/mapper/` |
| Add controller endpoint | `ViewAndControllers/src/main/java/com/company/userService/HrModule/` |
| Configure security | `UserService/src/main/java/com/company/userService/securityConfig/` |
| Debug full flow | Start from `ERPMain/src/main/java/com/company/main/ErpApplication.java` |
| Add finance entity (Invoice, Ledger, etc.) | `finance-service-django/finance/models.py` |
| Add finance REST endpoint | `finance-service-django/finance/views.py` (use DRF ViewSets) |
| Add finance serializer (DTO) | `finance-service-django/finance/serializers.py` (create if needed) |
| Configure Django app | `finance-service-django/config/settings.py` |

## Gotchas & Non-Obvious Behavior

- **Module versions are mixed**: model is `0.0.1-SNAPSHOT`, others are `1.0.0`. Be careful with version strings in pom.xml.
- **ViewAndControllers pom has security commented out** — likely disabled for dev. Re-enable carefully with UserService security config.
- **Oracle DDL is locked** (`validate` mode) — schema changes require DBA scripts, not JPA auto-generation.
- **Django Finance is under development** — `models.py` and `views.py` are currently empty. This is where invoice, ledger, and accounting entities will be defined as Django models with DRF serializers/viewsets.
- **Separate ports**: Spring Boot runs on 8081, Django on 8000. When implementing service-to-service calls, use `http://localhost:8000/api/` for finance endpoints from Spring Boot.

## Environment & Dependencies

### Spring Boot / Java
- **Java**: 21 (set in parent pom.xml)
- **Maven**: 3.6+ (use ./mvnw wrapper)
- **Oracle JDBC**: ojdbc11 v23.3.0.23.09

### Django Finance Service
- **Python**: 3.10+ (required by Django 6.0.1)
- **Django**: 6.0.1 with DRF 3.16.1
- **Database drivers**: psycopg2-binary (PostgreSQL) available; SQLite used for dev
- **Setup**: `pip install -r requirements.txt && python manage.py migrate && python manage.py runserver`

---

**Last Updated**: February 2026  
**Maintained By**: Development Team  
For questions on architecture decisions, consult the root pom.xml and ErpApplication.java component scan configuration.
