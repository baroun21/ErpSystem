# NEXORA ERP - Architecture Blueprint

**Status:** Design Phase | **Version:** 2.0 | **Date:** February 19, 2026

Complete architectural redesign from Oracle-based system to PostgreSQL modular monolith with domain-driven design, event-driven architecture, and multi-tenancy.

---

## 🏗️ Architecture Overview

### Current State → Target State

**Current:**
- Multi-module Maven project (model, UserService, ViewAndControllers, ERPMain)
- Oracle database with validate mode (no auto-generation)
- Single-tenant (implicit)
- Synchronous processing
- No event bus
- No async support

**Target:**
```
NEXORA ERP (Modular Monolith)
├── Domain Layer (DDD)
│   ├── HR Domain
│   ├── Finance Domain
│   ├── Procurement Domain
│   ├── Inventory Domain
│   └── Shared Domain (Common)
├── Application Layer
│   ├── Application Services
│   ├── DTOs / Commands / Queries
│   └── Mappers
├── Infrastructure Layer
│   ├── Event Bus (Spring Events)
│   ├── Async Processing
│   ├── Database Migration (Flyway)
│   ├── Redis Cache & Sessions
│   └── Multi-Tenant Context
├── API Layer (REST Controllers)
├── Security Layer (JWT + Spring Security)
└── Application Configuration
```

---

## 📦 Module Structure (Modular Monolith)

After refactoring, single executable JAR with clear domain boundaries:

```
erp-core-spring/
├── pom.xml (parent)
├── src/main/java/com/company/erp/
│   ├── shared/          (Common cross-domain)
│   │   ├── domain/
│   │   │   ├── BaseEntity.java
│   │   │   ├── TenantId.java
│   │   │   ├── ValueObject.java
│   │   │   └── DomainEvent.java
│   │   ├── infrastructure/
│   │   │   ├── TenantContext.java
│   │   │   ├── EventPublisher.java
│   │   │   ├── AsyncTaskQueue.java
│   │   │   └── CacheConfig.java
│   │   └── utils/
│   │
│   ├── hr/              (HR Domain - Bounded Context)
│   │   ├── domain/
│   │   │   ├── entity/   (JPA entities)
│   │   │   ├── service/  (Domain services)
│   │   │   ├── event/    (Domain events)
│   │   │   ├── vo/       (Value objects)
│   │   │   └── repository/ (Interfaces)
│   │   ├── application/
│   │   │   ├── service/  (App services)
│   │   │   ├── dto/
│   │   │   ├── command/  (CQRS commands)
│   │   │   └── query/    (CQRS queries)
│   │   ├── infrastructure/
│   │   │   ├── repository/ (JPA implementations)
│   │   │   ├── event/      (Event listeners)
│   │   │   └── async/      (Async handlers)
│   │   └── api/          (REST endpoints)
│   │
│   ├── finance/         (Finance Domain)
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── api/
│   │
│   ├── procurement/    (Procurement Domain)
│   ├── inventory/      (Inventory Domain)
│   │
│   ├── security/       (Cross-cutting)
│   │   ├── auth/
│   │   ├── jwt/
│   │   └── config/
│   │
│   └── Application.java (Bootstrap)
│
├── src/main/resources/
│   ├── application.yml
│   ├── application-postgres.yml
│   ├── db/migration/
│   │   ├── V1__Init_Schema.sql
│   │   ├── V2__Add_MultiTenant.sql
│   │   ├── V3__HR_Tables.sql
│   │   └── V4__Finance_Tables.sql
│   └── redis.conf
└── pom.xml
```

---

## 🎯 Domain-Driven Design

### Bounded Contexts

1. **HR Domain**
   - Employees, Departments, Positions
   - Attendance, Leaves, Reviews, Goals
   - Payroll, Deductions, Salary
   - Role-based access

2. **Finance Domain**
   - Companies, Chart of Accounts, Cost Centers
   - Customers, Suppliers, Vendors
   - Invoices, Bills, Payments
   - Bank Accounts, Transactions
   - Journal Entries, Trial Balance
   - AR/AP Aging

3. **Procurement Domain**
   - Purchase Requisitions
   - Purchase Orders
   - Vendor Management
   - RFQ (Request for Quote)

4. **Inventory Domain**
   - Products, Stock Levels
   - Warehouses, Bins
   - Stock Movements
   - ABC Analysis

5. **Shared Domain**
   - User Management
   - Security/Authentication
   - Multi-tenancy
   - Common Value Objects
   - Domain Event Infrastructure

---

## 🔄 Event-Driven Architecture

### Internal Event Bus (Publish-Subscribe)

```
Domain Event Lifecycle:

1. Domain Action Occurs (Employee hired)
   ↓
2. Domain Event Published (EmployeeHiredEvent)
   ↓
3. Event Stored in Event Log (Audit trail)
   ↓
4. Spring ApplicationEvent Published
   ↓
5. @EventListener Methods Invoked
   ├─→ Update Denormalized Views
   ├─→ Send Notifications
   ├─→ Trigger Async Jobs
   └─→ Update Related Aggregates
```

### Event Examples

**EmployeeHiredEvent**
```java
public class EmployeeHiredEvent extends DomainEvent {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private Long companyId;
    private LocalDate hireDate;
}
```

**Listeners**
- `PayrollEventListener` → Create initial salary
- `AccessEventListener` → Provision user account
- `AuditEventListener` → Log to audit table
- `NotificationEventListener` → Send email

---

## 👥 Multi-Tenancy Architecture

### Tenant Context Propagation

```
HTTP Request
    ↓
TenantInterceptor (Extract from header + JWT)
    ↓
TenantContext.setCurrentTenant(tenantId)
    ↓
Transaction Scope {
    TenantAwareJpaRepository
    ↓
    Query WHERE company_id = ? (Added automatically)
    ↓
    Redis Cache [tenant:key]
}
    ↓
TenantContext.clear()
```

### Multi-Tenant Implementation

All core JPA entities inherit from `BaseEntity`:

```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "company_id", nullable = false)
    private Long companyId;  // Tenant identifier
    
    @ManyToOne
    @JoinColumn(name = "legal_entity_id")
    private LegalEntity legalEntity;  // Multi-entity support
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### Automatic Multi-Tenant Filtering

```java
@Repository
public class TenantAwareEmployeeRepository extends JpaRepository<Employee, Long> {
    
    @Override
    public List<Employee> findAll() {
        Long tenantId = TenantContext.getCurrentTenant();
        return queryBuilder
            .where("company_id = ?", tenantId)
            .build();
    }
}
```

---

## ⚙️ Async Processing

### Processing Models

1. **@Async Annotation**
   - Fire-and-forget tasks
   - Long-running operations
   - Background jobs

2. **Event Listeners**
   - Automatic via @EventListener
   - Spring-managed thread pool

3. **Message Queue Ready**
   - RabbitMQ/Kafka integration points
   - Event publishing to external systems

### Examples

```java
// Async Service
@Service
public class PayrollService {
    @Async
    public void processMonthlyPayroll(Long companyId) {
        // Long-running: calculate salaries, taxes, deductions
    }
}

// Event Listener (Async)
@Component
public class EmployeeEventListener {
    @EventListener
    @Async
    public void onEmployeeHired(EmployeeHiredEvent event) {
        // Async: send welcome email, create access, notify HR
    }
}
```

---

## 🗄️ Database Architecture

### PostgreSQL Schema Strategy

- **Versioned Migrations** using Flyway
- **Base Schema** (shared)
- **Domain-Specific Tables**
- **Audit Tables** (event sourcing)
- **Tenant Isolation** (company_id filtering)

### Key Tables

```sql
-- Base Tables
companies (id, name, registration_number, country, created_at)
legal_entities (id, company_id, name, jurisdiction, created_at)
users (id, company_id, email, password_hash, created_at)

-- HR Domain
employees (id, company_id, legal_entity_id, first_name, last_name, ...)
departments (id, company_id, name, manager_id, ...)
positions (id, company_id, title, level, ...)

-- Finance Domain
chart_of_accounts (id, company_id, code, name, type, ...)
customers (id, company_id, name, email, ...)
invoices (id, company_id, customer_id, invoice_number, ...)

-- Audit / Events
domain_events (id, company_id, event_type, aggregate_id, payload, created_at)
audit_log (id, company_id, entity_type, action, old_value, new_value, ...)
```

---

## 🔐 Security & Multi-Tenant Context

### JWT Token Structure

```json
{
  "sub": "user@company.com",
  "tenantId": 123,
  "legalEntityId": null,
  "roles": ["HR_MANAGER", "EMPLOYEE"],
  "permissions": ["read:employees", "write:payroll"],
  "iat": 1708353600,
  "exp": 1708440000
}
```

### TenantContext Management

```java
@Component
public class TenantContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        String tenantId = extractFromJWT(request);
        TenantContext.setCurrentTenant(Long.parseLong(tenantId));
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
```

---

## 💾 Caching Strategy (Redis)

### Cache Layers

1. **Session Cache**
   - User sessions (30 minutes)
   - JWT token blacklist

2. **Dashboard Cache**
   - Counts, totals, summaries
   - 5-minute TTL
   - Invalidate on data change

3. **Reference Data Cache**
   - Departments, Positions, Currencies
   - 1-hour TTL
   - Manual invalidation

4. **Tenant Config Cache**
   - Company settings, fiscal year
   - Per-tenant keys: `tenant:123:setting:key`
   - Long TTL (invalidate on update)

### Cache Key Pattern

```
tenant:{tenantId}:{entity}:{id}:{field}
tenant:123:employee:456:name
tenant:123:dashboard:monthly-payroll:2026-02
```

---

## 🚀 Migration Plan

### Phase 1: Foundation (Week 1-2)
- [ ] Update pom.xml (PostgreSQL driver, Flyway, Redis, async)
- [ ] Create new package structure
- [ ] Implement TenantContext & multi-tenant filtering
- [ ] Set up Flyway migrations
- [ ] Configure PostgreSQL connection
- [ ] Create Redis configuration
- [ ] Add @Async configuration

### Phase 2: Domain Layer Refactoring (Week 3)
- [ ] Refactor HR Domain (entities, services, events)
- [ ] Refactor Finance Domain
- [ ] Create domain event classes
- [ ] Implement event publishing

### Phase 3: Event & Async Infrastructure (Week 4)
- [ ] Set up ApplicationEventPublisher
- [ ] Create domain event listeners
- [ ] Implement async task handlers
- [ ] Add audit event logging

### Phase 4: Cache & Session Management (Week 5)
- [ ] Redis session store
- [ ] Cache decorators on repositories
- [ ] Dashboard cache strategy
- [ ] Cache invalidation handlers

### Phase 5: Tenant Isolation Testing (Week 6)
- [ ] Multi-tenant repository tests
- [ ] Tenant context propagation tests
- [ ] Cross-tenant access prevention
- [ ] Legal entity support tests

---

## 📊 Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Framework** | Spring Boot 3.2.5 | Application framework |
| **Language** | Java 21 | Language runtime |
| **Database** | PostgreSQL 15+ | Persistent storage |
| **ORM** | Hibernate 6.x | Object-relational mapping |
| **Migration** | Flyway 9.x | Database versioning |
| **Cache** | Redis 7.x | Session & cache store |
| **Async** | Spring @Async | Background processing |
| **Events** | Spring Events | Internal event bus |
| **Security** | Spring Security 6.x | Authentication/authorization |
| **JWT** | JWT (jjwt) | Token-based auth |
| **Testing** | JUnit 5, Testcontainers | Testing framework |

---

## 🔍 Key Principles

1. **Domain-Driven Design**
   - Each domain has clear boundaries
   - Entities and services encapsulate business logic
   - Repositories hide data access details

2. **Event Sourcing Ready**
   - All changes logged as domain events
   - Audit trail built-in
   - Ready for CQRS pattern

3. **Multi-Tenancy by Default**
   - Every query filters by company_id
   - No accidental cross-tenant access
   - Tenant context enforced at boundaries

4. **Async-First**
   - Long operations run asynchronously
   - Non-blocking request handling
   - Event-driven inter-domain communication

5. **Modular Monolith**
   - Single deployment unit
   - Clear module boundaries
   - Minimal cross-module coupling
   - Ready to extract into microservices

---

## 📈 Benefits

✅ **Scalability** - Async processing, caching, multi-tenant
✅ **Maintainability** - Clear domain boundaries, DDD
✅ **Auditability** - Event logging, audit tables
✅ **Flexibility** - Event-driven, loosely coupled
✅ **Testability** - Domain layers easy to unit test
✅ **Cloud-Ready** - Stateless, externalized state (Redis)

---

## 🎯 Success Criteria

- [ ] PostgreSQL migration complete
- [ ] All HR entities in HR domain
- [ ] All Finance entities in Finance domain
- [ ] Multi-tenant filtering working
- [ ] Domain events published and handled
- [ ] Async processing functional
- [ ] Redis caching reducing database load
- [ ] Audit log capturing all changes
- [ ] Zero cross-tenant data leakage
- [ ] 50%+ reduction in response times (via caching)

