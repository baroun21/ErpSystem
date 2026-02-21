# ERP System Backend - Developer's Guide

**For**: Developers working on erp-core-spring  
**Version**: 3.5.5  
**Last Updated**: February 21, 2026

---

## 🎯 Quick Start for New Developers

### Step 1: Clone & Setup
```bash
# Clone repository
git clone <repo-url>
cd ErpSystem/erp-core-spring

# Install dependencies and build
./mvnw clean install -DskipTests

# Ensure PostgreSQL and Redis are running
docker run -d --name postgres-erp -e POSTGRES_DB=erp_db -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:14
docker run -d --name redis-erp -p 6379:6379 redis:7
```

### Step 2: Run the Application
```bash
# Option A: Run JAR
cd ERPMain/target
java -jar ERPMain-1.0.0.jar

# Option B: Run from IDE
# Open ERPApplication.java and run main()

# Option C: Run with Maven
./mvnw -pl ERPMain spring-boot:run
```

### Step 3: Verify Everything Works
```bash
# Check application is running
curl http://localhost:8081/actuator/health

# Login and get token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Test API with token
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/invoices
```

✅ **If all above work, you're ready to develop!**

---

## 📂 Core Concepts

### Module Responsibilities

**model/** - Data Definition Layer
- Where: Entity classes live here ONLY
- How: `@Entity` annotations + JPA
- Pattern: One entity = one .java file
- Compile to: Shared JAR for other modules

**UserService/** - Business Logic Layer
- Where: Repositories, Services, Controllers
- How: Spring annotations (@Service, @Repository, @RestController)
- Pattern: Service calls Repository; Controller calls Service
- Compile to: JAR with application logic

**ERPMain/** - Application Bootstrap
- Where: ErpApplication.java with component scanning
- How: @SpringBootApplication + @ComponentScan
- Pattern: Wires all modules together
- Compile to: Fat JAR (includes all dependencies)

### Key Principles

1. **Entities are Sacred** - Never duplicate, never move
2. **Repositories Follow Entities** - If entity in model, repository in UserService
3. **Mappers Auto-Generate** - Write interface, MapStruct generates implementation
4. **One Direction** - Controllers call Services call Repositories
5. **DTOs for APIs** - Never expose raw entities in REST responses

---

## 🔧 Common Development Tasks

### Task 1: Create a New Domain Entity

**Example**: Creating a "CRM Lead" entity

**Step 1**: Define the Entity in `model/`
```java
// File: model/src/main/java/com/company/erp/erp/entites/Lead.java
package com.company.erp.erp.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lead {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long leadId;
  
  @Column(nullable = false, length = 100)
  private String companyName;
  
  @Column(length = 100)
  private String contactName;
  
  @Column(length = 100)
  private String email;
  
  @Column(length = 20)
  private String phone;
  
  @Column(length = 1000)
  private String description;
  
  @Column(nullable = false, length = 20)
  private String status; // INQUIRY, QUALIFIED, PROPOSAL, NEGOTIATION, WON, LOST
  
  @Column(length = 20)
  private String source; // WEB, REFERRAL, TRADE_SHOW, COLD_CALL, INBOUND
  
  @Transient // Not persisted in DB
  private Double estimatedValue;
  
  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
  
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime updatedAt;
}
```

**Step 2**: Create the DTO in `model/`
```java
// File: model/src/main/java/com/company/erp/erp/entites/Dtos/LeadDTO.java
package com.company.erp.erp.entites.Dtos;

import lombok.*;

@Data
@Builder
public class LeadDTO {
  private Long leadId;
  private String companyName;
  private String contactName;
  private String email;
  private String phone;
  private String description;
  private String status;
  private String source;
}
```

**Step 3**: Create the Mapper Interface in `model/`
```java
// File: model/src/main/java/com/company/erp/erp/mapper/LeadMapper.java
package com.company.erp.erp.mapper;

import com.company.erp.erp.entites.Lead;
import com.company.erp.erp.entites.Dtos.LeadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LeadMapper {
  LeadDTO toDTO(Lead entity);
  Lead toEntity(LeadDTO dto);
  void updateEntityFromDTO(LeadDTO dto, @MappingTarget Lead entity);
}
```

**Step 4**: Create Repository in `UserService/`
```java
// File: UserService/src/main/java/com/company/userService/finance/repository/LeadRepository.java
package com.company.userService.finance.repository;

import com.company.erp.erp.entites.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
  List<Lead> findByStatus(String status);
  List<Lead> findBySourceAndStatus(String source, String status);
  List<Lead> findByCompanyNameContainingIgnoreCase(String name);
}
```

**Step 5**: Create Service in `UserService/`
```java
// File: UserService/src/main/java/com/company/userService/finance/service/LeadService.java
package com.company.userService.finance.service;

import com.company.erp.erp.entites.Lead;
import com.company.erp.erp.entites.Dtos.LeadDTO;
import com.company.erp.erp.mapper.LeadMapper;
import com.company.userService.finance.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadService {
  private final LeadRepository leadRepository;
  private final LeadMapper leadMapper;
  
  @Transactional
  public LeadDTO createLead(LeadDTO dto) {
    log.info("Creating new lead for company: {}", dto.getCompanyName());
    Lead entity = leadMapper.toEntity(dto);
    entity.setStatus("INQUIRY");
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());
    
    Lead saved = leadRepository.save(entity);
    log.info("Lead created with ID: {}", saved.getLeadId());
    return leadMapper.toDTO(saved);
  }
  
  @Transactional
  public LeadDTO updateLead(Long leadId, LeadDTO dto) {
    Lead entity = leadRepository.findById(leadId)
      .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
    
    leadMapper.updateEntityFromDTO(dto, entity);
    entity.setUpdatedAt(LocalDateTime.now());
    
    Lead saved = leadRepository.save(entity);
    return leadMapper.toDTO(saved);
  }
  
  @Transactional(readOnly = true)
  public LeadDTO getLead(Long leadId) {
    return leadRepository.findById(leadId)
      .map(leadMapper::toDTO)
      .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
  }
  
  @Transactional(readOnly = true)
  public List<LeadDTO> getLeadsByStatus(String status) {
    return leadRepository.findByStatus(status).stream()
      .map(leadMapper::toDTO)
      .collect(Collectors.toList());
  }
}
```

**Step 6**: Create Controller in `UserService/`
```java
// File: UserService/src/main/java/com/company/userService/finance/controller/LeadController.java
package com.company.userService.finance.controller;

import com.company.erp.erp.entites.Dtos.LeadDTO;
import com.company.userService.finance.service.LeadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Slf4j
public class LeadController {
  private final LeadService leadService;
  
  @PostMapping
  @PreAuthorize("hasRole('CRM')")
  public ResponseEntity<LeadDTO> createLead(@RequestBody LeadDTO dto) {
    log.info("POST /api/leads - Creating lead: {}", dto.getCompanyName());
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(leadService.createLead(dto));
  }
  
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('CRM')")
  public ResponseEntity<LeadDTO> getLead(@PathVariable Long id) {
    return ResponseEntity.ok(leadService.getLead(id));
  }
  
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('CRM')")
  public ResponseEntity<LeadDTO> updateLead(@PathVariable Long id, @RequestBody LeadDTO dto) {
    return ResponseEntity.ok(leadService.updateLead(id, dto));
  }
  
  @GetMapping
  @PreAuthorize("hasRole('CRM')")
  public ResponseEntity<List<LeadDTO>> getLeadsByStatus(
    @RequestParam(required = false) String status) {
    if (status != null) {
      return ResponseEntity.ok(leadService.getLeadsByStatus(status));
    }
    return ResponseEntity.ok(List.of());
  }
}
```

**Step 7**: Build and Run
```bash
# Build model module (entity/mapper will auto-generate)
./mvnw -pl model clean compile

# Build and run ERPMain
./mvnw -pl ERPMain clean package -DskipTests
java -jar ERPMain/target/ERPMain-1.0.0.jar
```

**Step 8**: Test the API
```bash
# Create a lead
curl -X POST http://localhost:8081/api/leads \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "companyName": "Acme Corp",
    "contactName": "John Doe",
    "email": "john@acme.com",
    "phone": "+1234567890",
    "source": "WEB",
    "status": "INQUIRY"
  }'

# Get the lead
curl -H "Authorization: Bearer <your-jwt-token>" \
  http://localhost:8081/api/leads/1
```

---

### Task 2: Modify an Existing Entity

**Example**: Add a field to the Invoice entity

**Step 1**: Update Entity in `model/`
```java
// Add to Lead class:
@Column(name = "probability_percentage")
private Integer probabilityPercentage; // Sales pipeline probability

@Column(name = "expected_close_date")
@Temporal(TemporalType.DATE)
private LocalDate expectedCloseDate;
```

**Step 2**: Update DTO
```java
// Add to LeadDTO:
private Integer probabilityPercentage;
private LocalDate expectedCloseDate;
```

**Step 3**: Create Database Migration
```sql
-- File: UserService/src/main/resources/db/migration/V003__add_lead_probability.sql
ALTER TABLE leads ADD COLUMN probability_percentage INTEGER DEFAULT 0;
ALTER TABLE leads ADD COLUMN expected_close_date DATE;
```

**Step 4**: Rebuild
```bash
./mvnw clean install -DskipTests
```

✅ **Mapper will auto-generate the new fields in LeadMapperImpl**

---

### Task 3: Add a Query Method to Repository

**Example**: Find leads by probability range

```java
// Add to LeadRepository:
@Query("SELECT l FROM Lead l WHERE l.probabilityPercentage >= ?1 AND l.probabilityPercentage <= ?2 ORDER BY l.probabilityPercentage DESC")
List<Lead> findByProbabilityRange(Integer minProbability, Integer maxProbability);

// Or simpler:
List<Lead> findByProbabilityPercentageGreaterThanEqual(Integer probability);
```

**Then use in Service**:
```java
public List<LeadDTO> getHighProbabilityLeads(Integer threshold) {
  return leadRepository.findByProbabilityPercentageGreaterThanEqual(threshold)
    .stream()
    .map(leadMapper::toDTO)
    .collect(Collectors.toList());
}
```

---

## 🧪 Testing

### Unit Testing Pattern

```java
// File: UserService/src/test/java/com/company/userService/finance/service/LeadServiceTest.java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.InjectMocks;

class LeadServiceTest {
  @Mock
  private LeadRepository leadRepository;
  
  @Mock
  private LeadMapper leadMapper;
  
  @InjectMocks
  private LeadService leadService;
  
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }
  
  @Test
  void testCreateLead() {
    // Arrange
    LeadDTO input = LeadDTO.builder()
      .companyName("Test Corp")
      .contactName("Test User")
      .build();
    
    Lead entity = Lead.builder()
      .leadId(1L)
      .companyName("Test Corp")
      .status("INQUIRY")
      .build();
    
    when(leadRepository.save(any(Lead.class))).thenReturn(entity);
    when(leadMapper.toEntity(input)).thenReturn(entity);
    when(leadMapper.toDTO(entity)).thenReturn(input);
    
    // Act
    LeadDTO result = leadService.createLead(input);
    
    // Assert
    assertNotNull(result);
    assertEquals("Test Corp", result.getCompanyName());
    verify(leadRepository).save(any(Lead.class));
  }
}
```

### Integration Testing Pattern

```java
// File: UserService/src/test/java/com/company/userService/finance/controller/LeadControllerIT.java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LeadControllerIT {
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private LeadRepository leadRepository;
  
  @Test
  void testCreateLeadEndpoint() throws Exception {
    String payload = """
      {
        "companyName": "Test Corp",
        "contactName": "John Doe",
        "email": "john@test.com",
        "phone": "+1234567890",
        "source": "WEB"
      }
      """;
    
    mockMvc.perform(post("/api/leads")
      .contentType(MediaType.APPLICATION_JSON)
      .content(payload)
      .header("Authorization", "Bearer " + getTestToken()))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.companyName").value("Test Corp"));
  }
}
```

---

## 🐛 Debugging Guide

### Enable Debug Logging

**File**: `UserService/src/main/resources/application.properties`
```properties
# Enable SQL logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Enable Spring Security logging
logging.level.org.springframework.security=DEBUG

# Enable all Spring Framework logging
logging.level.org.springframework=DEBUG

# Specific package logging
logging.level.com.company=DEBUG
```

### Remote Debugging

**Start application with debugging enabled**:
```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
  -jar ERPMain/target/ERPMain-1.0.0.jar
```

**In IDE** (VS Code, IntelliJ):
1. Create a "Remote" debug configuration
2. Connect to localhost:5005
3. Set breakpoints and debug

### View SQL Queries

**Option 1**: Enable Hibernate SQL logging (see above)

**Option 2**: Use H2 Console
```properties
# In application.properties
spring.h2.console.enabled=true
logging.level.org.hibernate.SQL=DEBUG
```

Then visit: http://localhost:8081/h2-console

---

## 📦 Understanding Build System

### Maven Module Hierarchy

```
erp-core-spring (parent pom)
├── Dependencies (managed here)
├── Plugins (managed here)
└── Modules:
    ├── model (pom.xml)
    ├── UserService (pom.xml)
    ├── ViewAndControllers (pom.xml)
    └── ERPMain (pom.xml)
```

### Build Commands Explained

```bash
# Build ALL modules in correct order
./mvnw clean install
# Clean → Compile → Test → Package → Install to local repo

# Build ONLY one module and its dependencies
./mvnw -pl model clean compile
./mvnw -pl UserService clean package

# Skip tests (faster for development)
./mvnw clean install -DskipTests

# Generate MapStruct implementations
./mvnw -pl model compile

# Run specific test
./mvnw -pl UserService -Dtest=LeadServiceTest test

# View dependency tree
./mvnw dependency:tree
```

### Understanding Compile Output

```
model/target/
├── model-0.0.1-SNAPSHOT.jar          ← Shared library JAR
├── classes/                           ← Compiled .class files
└── generated-sources/                 ← MapStruct generates here
    └── annotations/
        └── com/company/erp/erp/mapper/
            └── LeadMapperImpl.java     ← Auto-generated mapper impl

UserService/target/
├── UserService-1.0.0.jar
├── classes/
└── (no generated sources, just compiles MapStruct)

ERPMain/target/
├── ERPMain-1.0.0.jar                 ← FAT JAR (all dependencies included)
├── ERPMain-1.0.0.jar.original        ← Original compiled JAR (unused)
└── lib/                               ← All dependency JARs inside

```

---

## 🔐 Security Best Practices

### Token Management

**Getting a Token**:
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
# Returns: {"token":"eyJhbGciOiJIUzUxMiIsInR5..."}
```

**Using Token**:
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5..." \
  http://localhost:8081/api/invoices
```

**Token Expiry**:
- Default: 24 hours
- To extend: Call `/api/auth/refresh` with current token
- To logout: Call `/api/auth/logout`

### Role-Based Access Control

**Securing Endpoints**:
```java
@GetMapping("/{id}")
@PreAuthorize("hasRole('FINANCE')")  // Only FINANCE role
public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) { ... }

@PostMapping
@PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")  // Multiple roles
public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO dto) { ... }

@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")  // Admin only
public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) { ... }
```

**Available Roles**:
- `ROLE_ADMIN` - Full access
- `ROLE_FINANCE` - Finance module
- `ROLE_HR` - HR module
- `ROLE_INVENTORY` - Inventory module
- `ROLE_CRM` - CRM module

---

## 📝 Code Style & Conventions

### Naming Conventions

```java
// Classes
public class InvoiceService { }        // PascalCase
public class InvoiceDTO { }            // PascalCase

// Methods
public void createInvoice() { }        // camelCase
public List<Invoice> getByStatus() { } // camelCase

// Constants
private static final int MAX_RETRIES = 3;  // UPPER_CASE

// Tables
@Table(name = "invoices")              // lowercase_plural

// Columns
@Column(name = "invoice_number")       // lowercase_underscore
```

### Lombok Annotations

```java
@Data               // @Getter @Setter @ToString @EqualsAndHashCode
@Builder            // Builder pattern
@NoArgsConstructor  // Default constructor
@AllArgsConstructor // Constructor with all fields
@RequiredArgsConstructor  // Constructor for @NonNull fields
@Slf4j              // Logger logger = LoggerFactory.getLogger(...)
```

### Entity Annotations

```java
@Entity             // Mark as JPA entity
@Table(name="...")  // Specify table name
@Id                 // Primary key
@GeneratedValue     // Auto-generate ID
@Column             // Column properties
@Temporal           // Date/Time handling
@Transient          // Don't persist to DB
@ManyToOne          // Relationship
@OneToMany          // Relationship
@JoinColumn         // Foreign key
```

---

## 🚀 Performance Tips

### Query Optimization

```java
// ❌ BAD: N+1 queries (lazy loading)
List<Invoice> invoices = invoiceRepository.findAll();
for (Invoice inv : invoices) {
  System.out.println(inv.getCustomer().getName());  // Extra query per row!
}

// ✅ GOOD: Eager loading with JOIN FETCH
@Query("SELECT i FROM Invoice i JOIN FETCH i.customer WHERE i.status = ?1")
List<Invoice> findByStatusWithCustomer(String status);
```

### Pagination

```java
// ❌ BAD: Load all 1000000 records
List<Invoice> all = invoiceRepository.findAll();

// ✅ GOOD: Use pagination
Pageable page = PageRequest.of(0, 50);  // First 50 records
Page<Invoice> invoices = invoiceRepository.findAll(page);
invoices.forEach(inv -> System.out.println(inv.getInvoiceNumber()));
invoices.getTotalPages();  // Total number of pages
```

### Caching

```java
// Cache method results
@Service
public class InvoiceService {
  @Cacheable(value = "invoices", key = "#id")
  public Invoice getInvoice(Long id) {
    return invoiceRepository.findById(id).orElse(null);
  }
  
  // Invalidate cache on update
  @CacheEvict(value = "invoices", key = "#id")
  public Invoice updateInvoice(Long id, InvoiceDTO dto) {
    // ...
  }
}
```

---

## 🎓 Learning Resources

### Key Files to Review

1. **`ERPMain/src/main/java/com/company/main/ErpApplication.java`**
   - Learn how modules are wired together
   - Understand @ComponentScan and @EntityScan

2. **Any existing Entity** (e.g., `Invoice.java`)
   - Study JPA annotations
   - Understand relationships

3. **Any existing Service** (e.g., `InvoiceService.java`)
   - See business logic patterns
   - Learn transaction management

4. **Any existing Controller** (e.g., `InvoiceController.java`)
   - See REST endpoint patterns
   - Learn request/response handling

5. **Any existing Mapper** (e.g., `InvoiceMapper.java`)
   - Understand MapStruct interface
   - How auto-generation works

### External Resources

- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MapStruct Documentation](https://mapstruct.org/)
- [Lombok Features](https://projectlombok.org/features/)
- [JWT in Spring](https://www.baeldung.com/spring-security-authentication-with-jwt)
- [REST API Best Practices](https://restfulapi.net/)

---

## ✅ Pre-Commit Checklist

Before pushing code:

- [ ] Code compiles: `./mvnw clean compile`
- [ ] All tests pass: `./mvnw test`
- [ ] No console errors: Check console output
- [ ] Entities only in `model/` module
- [ ] Repositories only in `UserService/` module
- [ ] Controllers only in `UserService/` module
- [ ] All imports are correct
- [ ] No unused imports
- [ ] No hardcoded passwords/secrets
- [ ] Proper error handling
- [ ] Javadoc for public methods
- [ ] Logs use appropriate levels (DEBUG, INFO, WARN)

---

**Happy Coding! 🎉**
