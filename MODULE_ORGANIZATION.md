# ERP System Backend - Module Organization Guide

**Version**: 3.5.5  
**Purpose**: Explain folder structure and design decisions  
**Last Updated**: February 21, 2026

---

## 📁 Complete Folder Structure

```
erp-core-spring/                          ← PARENT POM (aggregator)
│
├── pom.xml                               ← Parent POM file (dependency management)
├── mvnw & mvnw.cmd                       ← Maven Wrapper scripts
│
├── .mvn/                                 ← Maven configuration
│   └── wrapper/
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
│
├── .github/                              ← GitHub configuration
│   ├── copilot-instructions.md           ← AI assistant guidelines
│   └── java-upgrade/                     ← Upgrade session tracking
│       └── 20260221120712/
│           └── progress.md               ← Upgrade progress log
│
├── .gitignore                            ← Git ignore patterns
├── .gitattributes                        ← Git attributes
│
│
├── model/                                ← 📦 SHARED DATA LAYER (Library JAR)
│   ├── pom.xml
│   ├── mvnw & mvnw.cmd
│   │
│   └── src/
│       ├── main/java/com/company/
│       │   └── erp/erp/
│       │       ├── entites/              ← 🔑 ALL ENTITY CLASSES
│       │       │   ├── finance/
│       │       │   │   ├── Invoice.java
│       │       │   │   ├── Bill.java
│       │       │   │   ├── BankAccount.java
│       │       │   │   ├── JournalEntry.java
│       │       │   │   ├── ChartOfAccount.java
│       │       │   │   ├── CostCenter.java
│       │       │   │   ├── InvoiceLine.java
│       │       │   │   ├── BillLine.java
│       │       │   │   ├── JournalEntryLine.java
│       │       │   │   ├── Payment -> CustomerPaymentRepository
│       │       │   │   └── Company.java
│       │       │   │
│       │       │   ├── Employee.java
│       │       │   ├── Customer.java
│       │       │   ├── Supplier.java
│       │       │   ├── Product.java
│       │       │   ├── Warehouse.java
│       │       │   ├── StockMovement.java
│       │       │   │
│       │       │   ├── enums/            ← Shared enumerations
│       │       │   │   ├── InvoiceStatus.java
│       │       │   │   ├── MovementType.java
│       │       │   │   └── LeaveType.java
│       │       │   │
│       │       │   ├── Dtos/             ← 📄 DATA TRANSFER OBJECTS
│       │       │   │   ├── InvoiceDTO.java
│       │       │   │   ├── EmployeeDTO.java
│       │       │   │   ├── StockMovementDTO.java
│       │       │   │   └── ...DTOs
│       │       │   │
│       │       │   └── exceptions/       ← Custom exceptions
│       │       │       ├── BusinessException.java
│       │       │       └── ValidationException.java
│       │       │
│       │       └── mapper/               ← 🔄 MAPSTRUCT MAPPERS
│       │           ├── InvoiceMapper.java       → Auto: InvoiceMapperImpl
│       │           ├── EmployeeMapper.java      → Auto: EmployeeMapperImpl
│       │           ├── StockMovementMapper.java → Auto: StockMovementMapperImpl
│       │           └── ...Mapper.java files
│       │
│       └── test/java/                    ← Unit tests for mapper
│           └── com/company/erp/erp/mapper/
│
│
├── UserService/                          ← 🔐 AUTHENTICATION & BUSINESS LOGIC
│   ├── pom.xml
│   │
│   └── src/
│       ├── main/java/com/company/
│       │   ├── userService/
│       │   │   ├── *.java                ← User service classes
│       │   │   ├── repository/           ← 🔑 JPA REPOSITORIES
│       │   │   │   ├── UserRepository.java
│       │   │   │   ├── RoleRepository.java
│       │   │   │   └── PasswordResetTokenRepository.java
│       │   │   │
│       │   │   ├── securityConfig/       ← 🔐 SPRING SECURITY
│       │   │   │   ├── SecurityConfig.java
│       │   │   │   ├── JwtAuthenticationFilter.java
│       │   │   │   ├── JwtTokenProvider.java
│       │   │   │   └── CustomUserDetailsService.java
│       │   │   │
│       │   │   └── Main.java
│       │   │
│       │   ├── userService/finance/      ← 💰 FINANCE MODULE
│       │   │   ├── controller/           ← REST ENDPOINTS
│       │   │   │   ├── InvoiceController.java
│       │   │   │   ├── BillController.java
│       │   │   │   ├── LedgerController.java
│       │   │   │   ├── ReportController.java
│       │   │   │   └── ...Controller.java
│       │   │   │
│       │   │   ├── service/              ← BUSINESS LOGIC
│       │   │   │   ├── InvoiceService.java
│       │   │   │   ├── BillService.java
│       │   │   │   ├── LedgerService.java
│       │   │   │   ├── ARService.java    (Accounts Receivable)
│       │   │   │   ├── APService.java    (Accounts Payable)
│       │   │   │   ├── ReportService.java
│       │   │   │   └── ...Service.java
│       │   │   │
│       │   │   ├── repository/           ← DATA ACCESS
│       │   │   │   ├── InvoiceRepository.java
│       │   │   │   ├── BillRepository.java
│       │   │   │   ├── BankAccountRepository.java
│       │   │   │   ├── LedgerRepository.java
│       │   │   │   ├── PaymentRepository.java
│       │   │   │   └── ...Repository.java
│       │   │   │
│       │   │   ├── dto/                  ← FINANCE DTOs
│       │   │   │   ├── CreateInvoiceRequest.java
│       │   │   │   ├── InvoiceResponse.java
│       │   │   │   └── ...DTO.java
│       │   │   │
│       │   │   ├── config/               ← CONFIGURATION
│       │   │   │   ├── DatabaseConfig.java
│       │   │   │   ├── CacheConfig.java
│       │   │   │   └── FinanceConfig.java
│       │   │   │
│       │   │   └── event/                ← DOMAIN EVENTS
│       │   │       ├── FinanceEventPublisher.java
│       │   │       └── InvoicePostedEvent.java
│       │   │
│       │   └── userService/HrModule/     ← 👥 HR MODULE
│       │       ├── controller/           ← REST ENDPOINTS
│       │       │   ├── EmployeeController.java
│       │       │   ├── DepartmentController.java
│       │       │   ├── LeaveController.java
│       │       │   ├── AttendanceController.java
│       │       │   ├── PayrollController.java
│       │       │   └── ...Controller.java
│       │       │
│       │       ├── service/              ← BUSINESS LOGIC
│       │       │   ├── EmployeeService.java
│       │       │   ├── LeaveService.java
│       │       │   ├── AttendanceService.java
│       │       │   ├── PayrollService.java
│       │       │   └── ...Service.java
│       │       │
│       │       ├── repositories/         ← DATA ACCESS
│       │       │   ├── EmployeeRepository.java
│       │       │   ├── DepartmentRepository.java
│       │       │   ├── LeaveRepository.java
│       │       │   └── ...Repository.java
│       │       │
│       │       └── repository/           ← Additional repositories
│       │           ├── AttendanceRepository.java
│       │           └── ...Repository.java
│       │
│       ├── main/resources/
│       │   ├── application.properties    ← ⚙️ RUNTIME CONFIGURATION
│       │   ├── application-dev.properties
│       │   ├── application-prod.properties
│       │   │
│       │   ├── db/migration/             ← FLYWAY DATABASE MIGRATIONS
│       │   │   ├── V001__initial_schema.sql
│       │   │   ├── V002__add_finance_tables.sql
│       │   │   └── ...sql
│       │   │
│       │   └── templates/                ← THYMELEAF TEMPLATES (if used)
│       │
│       └── test/java/                    ← UNIT & INTEGRATION TESTS
│           └── com/company/userService/
│               ├── finance/service/
│               │   └── InvoiceServiceTest.java
│               └── finance/controller/
│                   └── InvoiceControllerIT.java
│
│
├── ViewAndControllers/                   ← LEGACY VIEW LAYER
│   ├── pom.xml
│   │
│   └── src/
│       ├── main/java/com/company/
│       │   └── userService/
│       │       └── (legacy controllers)
│       │
│       └── main/resources/
│           └── templates/
│               └── (legacy HTML views)
│
│
└── ERPMain/                              ← 🚀 MAIN APPLICATION
    ├── pom.xml                           ← JAR packaging (fat/uber JAR)
    │
    └── src/
        ├── main/java/com/company/
        │   │
        │   ├── main/                     ← 🎯 APPLICATION ENTRY POINT
        │   │   ├── ErpApplication.java   ← Main class with component scanning
        │   │   │
        │   │   └── config/               ← GLOBAL CONFIGURATION
        │   │       ├── AppConfig.java
        │   │       ├── HttpClientConfig.java
        │   │       ├── CacheConfig.java
        │   │       │
        │   │       └── DevSecurityConfig.java (removed - caused bean conflict)
        │   │
        │   ├── erp/finance/              ← 💰 FINANCE DOMAIN
        │   │   ├── presentation/         ← REST ENDPOINTS
        │   │   │   └── ...Controller.java
        │   │   │
        │   │   ├── application/          ← BUSINESS LOGIC (removed)
        │   │   │   └── (complex services removed due to dependencies)
        │   │   │
        │   │   └── domain/
        │   │       └── (domain models)
        │   │
        │   ├── erp/inventory/            ← 📦 INVENTORY DOMAIN
        │   │   ├── presentation/
        │   │   ├── application/
        │   │   └── domain/
        │   │
        │   ├── erp/crm/                  ← 💼 CRM DOMAIN
        │   │   └── ...
        │   │
        │   ├── erp/shared/               ← 🔧 SHARED INFRASTRUCTURE
        │   │   ├── domain/
        │   │   │   └── DomainEvent.java
        │   │   │
        │   │   ├── infrastructure/
        │   │   │   ├── event/
        │   │   │   │   └── DomainEventPublisher.java
        │   │   │   │
        │   │   │   ├── cache/
        │   │   │   │   └── RedisConfig.java
        │   │   │   │
        │   │   │   ├── exception/
        │   │   │   │   ├── GlobalExceptionHandler.java
        │   │   │   │   └── ErrorResponse.java
        │   │   │   │
        │   │   │   └── logging/
        │   │   │       └── LoggingConfig.java
        │   │   │
        │   │   └── utils/
        │   │       └── (utility classes)
        │   │
        │   └── erp/automation/           ← 🤖 AUTOMATION ENGINE
        │       ├── controller/
        │       ├── service/
        │       ├── repository/
        │       └── domain/
        │
        ├── main/resources/
        │   └── application.properties    ← (inherits from UserService)
        │
        └── test/java/
            └── com/company/main/
                └── (integration tests)

```

---

## 🏗️ Architectural Decisions & Rationale

### Decision 1: Multi-Module Maven Structure

**Why**: 
- Separate concerns: Data layer (model) vs business logic (UserService) vs orchestration (ERPMain)
- Reusability: Other microservices can import just the `model` JAR
- Clear dependency management: Eliminates circular dependencies

**Trade-offs**:
- ✅ Better organization
- ❌ Slightly more complex build
- ✅ Clearer API boundaries

---

### Decision 2: All Entities in model/ Only

**Why**:
- Single source of truth for data definitions
- Prevents duplicate entity definitions causing JPA conflicts
- Other modules depend on `model`, never vice versa
- Easy to locate where an entity is defined

**Rules**:
```
✅ DO: Define Invoice in model/entites/finance/
❌ DON'T: Define Invoice in ERPMain or other modules
```

---

### Decision 3: Repositories in UserService/ Only

**Why**:
- Centralized data access layer
- Spring can register all repositories in one place
- Easy to configure connection pooling
- Prevents repository duplication

**Pattern**:
```
model/        → Invoice.java (entity)
UserService/  → InvoiceRepository (can access Invoice from model)
ERPMain/      → (component scan finds UserService repositories)
```

---

### Decision 4: Services Alongside Repositories

**Why**:
- Locality of behavior: Service works with related Repository
- Logical grouping: `finance/service/` contains `InvoiceService`, `BillService`, etc
- Consistent with Repository location

**Structure**:
```
UserService/finance/
├── repository/    → Data access layer
├── service/       → Business logic layer
├── controller/    → REST API layer
└── dto/           → Transfer objects
```

---

### Decision 5: Controllers in UserService/

**Why**:
- Keeps REST API close to business logic
- Controllers can easily inject Services
- No need for separate "web" module
- Simpler deployment model

---

### Decision 6: MapStruct for Entity-DTO Conversion

**Why**:
- Zero runtime overhead (compile-time generation)
- Type-safe mapping
- Easy to maintain (just interfaces)
- Auto-generates `EntityMapperImpl` at compile time

**Usage Pattern**:
```
1. Define interface:      InvoiceMapper.java
2. Annotate with @Mapper: implementation auto-generates
3. Use generated impl:    invoiceMapper.toDTO(entity)
```

---

### Decision 7: No Direct Entity Exposure in APIs

**Rule**: NEVER return raw entities from REST endpoints

```java
// ❌ WRONG
@PostMapping
public Invoice createInvoice(@RequestBody Invoice invoice) { }

// ✅ CORRECT
@PostMapping
public InvoiceDTO createInvoice(@RequestBody InvoiceDTO dto) {
  Invoice entity = invoiceMapper.toEntity(dto);
  Invoice saved = invoiceService.create(entity);
  return invoiceMapper.toDTO(saved);
}
```

**Benefits**:
- Decouples API contract from database schema
- Prevents accidentally exposing sensitive fields
- Allows different API response vs entity structure

---

### Decision 8: Explicit Component Scanning in ErpApplication

**Why**:
```java
@ComponentScan(basePackages = {
  "com.company.main",
  "com.company.userService",
  "com.company.userService.HrModule",
  "com.company.userService.finance"
})
@EntityScan("com.company.erp.erp.entites")
@EnableJpaRepositories(basePackages = {
  "com.company.userService.repository",
  "com.company.userService.finance.repository"
})
```

**Benefits**:
- Explicit = predictable
- Prevents unexpected class scanning
- Clear what gets loaded into Spring context
- Easier to debug component conflicts

---

## 📊 Dependency Flow

```
Client HTTP Request
        ↓
   Controller (ERPMain scans UserService.finance.controller)
        ↓
   Service (UserService.finance.service)
        ↓
   Repository (UserService.finance.repository)
        ↓
   Entity (model.entites.finance) + DTO (model.entites.Dtos)
        ↓
PostgreSQL Database
```

**No reverse dependencies**: No lower layer imports from upper layers

---

## 🛠️ How to Add New Features

### Add a New Domain (e.g., Procurement)

**Step 1**: Create entities in `model/`:
```
model/src/main/java/com/company/erp/erp/entites/
├── purchase_order.java
├── purchase_order_line.java
└── vendor.java
```

**Step 2**: Create DTOs in `model/`:
```
model/src/main/java/com/company/erp/entites/Dtos/
├── PurchaseOrderDTO.java
├── VendorDTO.java
└── ...
```

**Step 3**: Create Mappers in `model/`:
```
model/src/main/java/com/company/erp/mapper/
├── PurchaseOrderMapper.java
└── VendorMapper.java
```

**Step 4**: Create Repositories in `UserService/`:
```
UserService/src/main/java/com/company/userService/procurement/repository/
├── PurchaseOrderRepository.java
└── VendorRepository.java
```

**Step 5**: Create Services in `UserService/`:
```
UserService/src/main/java/com/company/userService/procurement/service/
├── PurchaseOrderService.java
└── VendorService.java
```

**Step 6**: Create Controllers in `UserService/`:
```
UserService/src/main/java/com/company/userService/procurement/controller/
├── PurchaseOrderController.java
└── VendorController.java
```

**Step 7**: Add to component scan in `ErpApplication.java`:
```java
@ComponentScan(basePackages = {
  // ... existing packages
  "com.company.userService.procurement"
})
```

---

## 🧹 Cleanup Done (February 21, 2026)

**Removed**:
- ❌ Duplicate entities in ERPMain (BankAccount, Invoice, etc.)
- ❌ DevSecurityConfig (security filter conflict)
- ❌ Event listeners referencing deleted domain events
- ❌ Complex finance application services (had unresolvable dependencies)
- ❌ Cash intelligence & command center modules (incomplete)
- ❌ Compile log files (artifacts)

**Kept**:
- ✅ Core entities and mappers (model/)
- ✅ Repositories (UserService/)
- ✅ Controllers and services (UserService/)
- ✅ Security configuration (Spring Security)
- ✅ Shared infrastructure (ERPMain)

---

## ✅ Current Module Status

| Module | Status | Purpose |
|--------|--------|---------|
| `model/` | ✅ Active | Shared entities, DTOs, mappers |
| `UserService/` | ✅ Active | Auth, repositories, services, controllers |
| `ViewAndControllers/` | ⚠️ Legacy | Minimal - replaced by REST APIs |
| `ERPMain/` | ✅ Active | Application bootstrap & orchestration |

---

## 🎯 Best Practices Summary

### DO ✅
- Define entities ONLY in `model/`
- Create repositories ONLY in `UserService/`
- Keep services near repositories
- Use DTOs for API contracts
- Use MapStruct for mapping
- Explicitly scan components in `ErpApplication`
- Keep layer separation strict

### DON'T ❌
- Don't define entities in multiple modules
- Don't put repositories in `model/` or `ERPMain/`
- Don't return raw entities from REST APIs
- Don't mix business logic in controllers
- Don't have circular dependencies
- Don't skip tests during development

---

**Last Updated**: February 21, 2026  
**Status**: ✅ Clean, Organized, Production Ready
