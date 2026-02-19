# Inventory Module - Integration & Configuration Guide

**Date**: February 19, 2026  
**Version**: 1.0.0  
**Status**: Complete

---

## Table of Contents

1. [Module Overview](#module-overview)
2. [Architecture Components](#architecture-components)
3. [Finance Module Integration](#finance-module-integration)
4. [Configuration](#configuration)
5. [Database Setup](#database-setup)
6. [Build & Deployment](#build--deployment)
7. [Testing & Validation](#testing--validation)
8. [Troubleshooting](#troubleshooting)
9. [Performance Tuning](#performance-tuning)
10. [Maintenance](#maintenance)

---

## Module Overview

The **Inventory Module** is a comprehensive inventory management system for the ERP backend. It handles:

### Core Functions
- **Product Management**: SKU definition, variants, categories, cost methods
- **Warehouse Management**: Multi-location inventory tracking
- **Stock Operations**: Receipt, issue (sale), inter-warehouse transfer
- **Cost Accounting**: FIFO, LIFO, moving average, standard cost methods
- **Valuation Reporting**: Total value, aging, dead stock, ABC analysis
- **Reorder Automation**: EOQ calculation, safety stock, supplier integration

### Integration Points
- **Finance Module**: Automatic COGS journal entry creation (REST API)
- **User Service**: JWT authentication, multi-tenancy via company ID
- **Procurement Module** (future): Purchase order generation from reorder suggestions

### Delivery Details
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.5
- **ORM**: Jakarta Persistence / Hibernate
- **Build Tool**: Maven (multi-module parent)
- **Database**: Oracle (primary), SQLite/PostgreSQL (Django Finance service)

---

## Architecture Components

### Package Structure

```
com.company.erp.inventory/
├── domain/                           # Domain layer (business rules)
│   ├── entity/                       # JPA entities
│   │   ├── Product.java
│   │   ├── ProductVariant.java
│   │   ├── Warehouse.java
│   │   ├── StockMovement.java
│   │   ├── InventoryTransaction.java
│   │   └── ReorderRule.java
│   ├── enums/                        # Business enumerations
│   │   ├── CostingMethod.java        # FIFO, LIFO, MOVING_AVERAGE, STANDARD
│   │   ├── MovementType.java         # RECEIPT, ISSUE, ADJUSTMEN, TRANSFER
│   │   └── MovementStatus.java       # IN_HAND, IN_TRANSIT, RECEIVED
│   └── dto/                          # Data transfer objects
│       ├── ProductDTO.java
│       ├── WarehouseDTO.java
│       ├── StockMovementDTO.java
│       ├── InventoryTransactionDTO.java
│       ├── ReorderRuleDTO.java
│       └── ValuationReportDTO.java
│
├── application/                      # Application layer (use cases)
│   └── service/
│       ├── StockService.java         # Stock receipt/issue/transfer
│       ├── CostingService.java       # Cost method calculations
│       ├── InventoryValuationService.java  # Reporting & analytics
│       ├── ReorderService.java       # Automation & EOQ
│       ├── COGSIntegrationService.java     # Finance module bridge (interface)
│       └── impl/
│           ├── StockServiceImpl.java
│           ├── CostingServiceImpl.java
│           ├── InventoryValuationServiceImpl.java
│           ├── ReorderServiceImpl.java
│           └── COGSIntegrationServiceImpl.java  # REST client to Finance API
│
├── infrastructure/                   # Infrastructure layer (persistence)
│   └── repository/
│       ├── ProductRepository.java
│       ├── WarehouseRepository.java
│       ├── StockMovementRepository.java
│       ├── InventoryTransactionRepository.java
│       └── ReorderRuleRepository.java
│
├── presentation/                     # Presentation layer (REST API)
│   └── controller/
│       ├── ProductController.java
│       ├── WarehouseController.java
│       ├── StockMovementController.java
│       ├── InventoryValuationController.java
│       └── ReorderController.java
│
└── mapper/                          # MapStruct mappers
    └── InventoryMappers.java
```

### Layer Responsibilities

| Layer | Responsibility | Examples |
|-------|-----------------|----------|
| **Domain** | Business rules, entities | Product entities, enums, DTOs |
| **Application** | Use cases, transactions | StockService.recordReceipt() |
| **Infrastructure** | Database access | ProductRepository queries |
| **Presentation** | HTTP endpoints | ProductController.getAll() |

### Data Flow Example: Stock Issue

```
HTTP POST /api/inventory/stock-movements/issue
    ↓
StockMovementController (REST endpoint)
    ↓
StockService.recordIssue() (business logic)
    • Validates sufficient stock
    • Creates StockMovement entity
    • Creates InventoryTransaction
    • Calls CostingService for cost calculation
    • Updates Product on-hand
    ↓
COGSIntegrationService.createCOGSJournalEntry()
    • REST call to Finance module
    • Creates general ledger entry
    • Debit: COGS expense account
    • Credit: Inventory asset account
    ↓
Repository persistence + REST response
```

---

## Finance Module Integration

### Overview

The Inventory module integrates with the Finance module to automatically create journal entries when stock is issued. This ensures **real-time cost accounting**.

### Integration Flow

```
Stock Issue Event
    ↓
StockService calculates cost (COGS)
    ↓
COGSIntegrationService.createCOGSJournalEntry()
    ↓
REST call to Finance API
    POST /api/finance/journal-entries
    ↓
Finance module creates entry
    • Debit: COGS Expense (e.g., 5100)
    • Credit: Inventory Asset (e.g., 1300)
    ↓
Journal entry saved in Finance DB
    ↓
Return to Inventory module (confirmation)
```

### API Contract

#### Finance Journal Entry API

**Endpoint**: `POST /api/finance/journal-entries`  
**Base URL**: Configured via `inventory.finance.api.url`

**Request**:
```json
{
  "companyId": "ACME-CORP",
  "description": "COGS Entry for SKU-001 - MOV-2026-001",
  "referenceType": "STOCK_ISSUE",
  "referenceNumber": "MOV-2026-001",
  "entryDate": "2026-02-19T10:30:00Z",
  "lineItems": [
    {
      "accountCode": "5100",
      "description": "COGS - Laptop",
      "debitAmount": 2999.95
    },
    {
      "accountCode": "1300",
      "description": "Inventory Reduction - Laptop",
      "creditAmount": 2999.95
    }
  ]
}
```

**Response** (201 Created):
```json
{
  "id": "JE-2026-0001",
  "companyId": "ACME-CORP",
  "createdAt": "2026-02-19T10:30:00Z",
  "status": "POSTED"
}
```

### Implementation Details

**Service**: `COGSIntegrationServiceImpl`

**Key Methods**:

1. **createCOGSJournalEntry()**
   - Called by `StockService.recordIssue()`
   - Calculates total cost via `CostingService`
   - Builds journal entry request
   - POSTs to Finance API
   - Returns journal entry ID

2. **Error Handling**
   - Timeout: 5 seconds max per Finance call
   - Retry: Queue failed entries for 3x retry with exponential backoff
   - Fallback: Log error and continue (non-blocking)
   - Alert: Admin notified if journal entry fails

3. **Account Code Management**
   - Default: COGS = 5100, Inventory = 1300
   - Override: Via company configuration (future enhancement)
   - Validation: `validateAccountConfiguration()` on startup

### Configuration

Add to `application.properties`:

```properties
# Finance Module Integration
inventory.finance.api.url=http://localhost:8081/api/finance
inventory.finance.timeout-seconds=5
inventory.finance.retry-attempts=3

# GL Account Codes (can be overridden per company)
inventory.cogs.account.code=5100
inventory.inventory.account.code=1300
inventory.variance.account.code=5200
inventory.adjustment.account.code=4200
```

### Testing Integration

**Unit Test**:
```java
@Test
void testCOGSJournalCreation() {
    // Mock Finance API
    when(restTemplate.postForEntity(...))
        .thenReturn(new ResponseEntity<>(journalEntryResponse, HttpStatus.CREATED));
    
    // Record stock issue
    StockMovement movement = stockService.recordIssue(...);
    
    // Verify journal entry called
    verify(cogsIntegrationService).createCOGSJournalEntry(...);
}
```

**Integration Test**:
```bash
# Terminal 1: Start Finance module
cd finance-service-django
python manage.py runserver 8000

# Terminal 2: Run Inventory tests
mvn test -Dtest=InventoryModuleIntegrationTest
```

### Troubleshooting

**Symptom**: COGS entries not being created  
**Causes**:
1. Finance module not running (check logs)
2. REST URL misconfigured
3. Account codes not valid

**Solution**:
1. Verify Finance module: `curl http://localhost:8000/api/finance/health`
2. Check logs: `grep "Finance API call failed" logs.txt`
3. Update account codes in configuration

---

## Configuration

### Application Properties

Create or update `erp-core-spring/ERPMain/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8081
server.servlet.context-path=/
spring.application.name=erp-inventory-service

# Database - Oracle
spring.datasource.url=jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1
spring.datasource.username=userGrade
spring.datasource.password=user1234
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true

# Inventory Configuration
inventory.default.company.id=DEFAULT_COMPANY
inventory.default.currency=USD

# Costing Methods (per company - can be customized)
inventory.default.costing.method=MOVING_AVERAGE
inventory.fifo.enabled=true
inventory.lifo.enabled=true
inventory.standard.cost.enabled=true

# Finance Module Integration
inventory.finance.api.url=http://localhost:8081/api/finance
inventory.finance.timeout-seconds=5
inventory.finance.retry-attempts=3

# GL Account Codes (defaults)
inventory.cogs.account.code=5100
inventory.inventory.account.code=1300
inventory.variance.account.code=5200

# Reorder Configuration
inventory.reorder.enabled=true
inventory.reorder.lead.time.default=14
inventory.reorder.safety.stock.multiplier=1.5

# Valuation Report Configuration
inventory.aging.threshold.days=180
inventory.dead.stock.threshold.days=180
inventory.slow.moving.threshold.days=90
inventory.slow.moving.min.movement.count=3

# Logging
logging.level.com.company.erp.inventory=INFO
logging.level.com.company.erp.inventory.application.service=DEBUG
logging.level.org.springframework.web=WARN
logging.file.name=logs/inventory-service.log

# Rest Template (for Finance API calls)
spring.config.import=optional:classpath:rest-template.properties
rest.template.connection.timeout=5000
rest.template.read.timeout=5000
```

### Environment Variables (Optional)

```bash
export INVENTORY_DB_URL=jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1
export INVENTORY_DB_USER=userGrade
export INVENTORY_DB_PASSWORD=user1234
export INVENTORY_FINANCE_API_URL=http://finance-service:8000/api/finance
```

---

## Database Setup

### Schema Creation

The schema is managed by Hibernate with `ddl-auto=validate`. Ensure the following tables exist in Oracle:

```sql
-- Run DDL scripts on Oracle instance
-- All entities in com.company.erp.inventory.domain.entity will map to tables

-- Key Tables:
CREATE TABLE products (
    id                      NUMBER(19) PRIMARY KEY,
    company_id              VARCHAR2(50),
    sku                     VARCHAR2(100),
    name                    VARCHAR2(255),
    category                VARCHAR2(100),
    description             CLOB,
    costing_method          VARCHAR2(50),
    standard_cost           NUMBER(19,2),
    reorder_point           NUMBER(10),
    max_stock_level         NUMBER(10),
    active                  CHAR(1),
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP,
    UNIQUE (company_id, sku)
);

CREATE TABLE warehouses (
    id                      NUMBER(19) PRIMARY KEY,
    company_id              VARCHAR2(50),
    name                    VARCHAR2(255),
    code                    VARCHAR2(50),
    location                VARCHAR2(255),
    address                 VARCHAR2(500),
    receiving_enabled       CHAR(1),
    shipping_enabled        CHAR(1),
    active                  CHAR(1),
    created_at              TIMESTAMP,
    manager                 VARCHAR2(100),
    phone                   VARCHAR2(20),
    UNIQUE (company_id, code)
);

CREATE TABLE stock_movements (
    id                      NUMBER(19) PRIMARY KEY,
    movement_number         VARCHAR2(100),
    company_id              VARCHAR2(50),
    product_id              NUMBER(19),
    movement_type           VARCHAR2(50),
    movement_status         VARCHAR2(50),
    sourced_warehouse_id    NUMBER(19),
    target_warehouse_id     NUMBER(19),
    quantity                NUMBER(10),
    cost                    NUMBER(19,2),
    reference_number        VARCHAR2(100),
    notes                   CLOB,
    movement_date           TIMESTAMP,
    created_at              TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (sourced_warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (target_warehouse_id) REFERENCES warehouses(id)
);

CREATE TABLE inventory_transactions (
    id                      NUMBER(19) PRIMARY KEY,
    company_id              VARCHAR2(50),
    product_id              NUMBER(19),
    warehouse_id            NUMBER(19),
    transaction_type        VARCHAR2(50),
    quantity                NUMBER(10),
    unit_cost               NUMBER(19,2),
    total_cost              NUMBER(19,2),
    transaction_date        TIMESTAMP,
    reference_number        VARCHAR2(100),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
);

CREATE TABLE reorder_rules (
    id                      NUMBER(19) PRIMARY KEY,
    company_id              VARCHAR2(50),
    product_id              NUMBER(19),
    warehouse_id            NUMBER(19),
    reorder_point           NUMBER(10),
    max_stock_level         NUMBER(10),
    economic_order_qty      NUMBER(10),
    lead_time_days          NUMBER(5),
    holding_cost_per_year   NUMBER(19,2),
    ordering_cost_per_order NUMBER(19,2),
    active                  CHAR(1),
    created_at              TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    UNIQUE (company_id, product_id, warehouse_id)
);

CREATE INDEX idx_products_company ON products(company_id);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_warehouses_company ON warehouses(company_id);
CREATE INDEX idx_stock_movements_product ON stock_movements(product_id);
CREATE INDEX idx_stock_movements_date ON stock_movements(movement_date);
CREATE INDEX idx_inventory_trans_date ON inventory_transactions(transaction_date);
```

### Data Seeding (Optional)

```sql
-- Insert test company
INSERT INTO companies (id, name, active) VALUES ('ACME-001', 'ACME Corporation', 'Y');

-- Insert test products
INSERT INTO products (id, company_id, sku, name, category, costing_method, reorder_point, active)
VALUES (1, 'ACME-001', 'LAPT-001', 'Laptop Pro', 'Electronics', 'MOVING_AVERAGE', 50, 'Y');

-- Insert test warehouse
INSERT INTO warehouses (id, company_id, name, code, location, receiving_enabled, shipping_enabled, active)
VALUES (1, 'ACME-001', 'Primary Warehouse', 'WH-01', 'New York', 'Y', 'Y', 'Y');

COMMIT;
```

---

## Build & Deployment

### Prerequisites

- Java 21 JDK
- Maven 3.6+ (use `./mvnw` wrapper)
- Oracle 19c+ or compatible
- Finance module (Django service)

### Build Steps

```bash
# Navigate to root
cd c:\Users\Devoe\ErpSystem\erp-core-spring

# Full clean build (all modules)
./mvnw clean install -DskipTests

# Build only ERPMain (includes dependency modules)
./mvnw clean package -pl ERPMain -DskipTests

# With tests
./mvnw clean verify -pl ERPMain
```

### Run Application

**Option 1: Run JAR**
```bash
java -jar ERPMain/target/ERPMain-1.0.0.jar \
  --spring.datasource.url=jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1 \
  --spring.datasource.username=userGrade \
  --spring.datasource.password=user1234
```

**Option 2: IDE Run**
- Open `ErpApplication.java` in ERPMain module
- Click "Run" or press F5
- Application starts on port 8081

**Option 3: PowerShell Script**
```bash
.\run-app.ps1
```

### Verification

```bash
# Check health endpoint
curl -X GET http://localhost:8081/health

# Check Inventory endpoints
curl -X GET http://localhost:8081/api/inventory/products \
  -H "Authorization: Bearer $TOKEN"

# Check Finance integration
curl -X GET http://localhost:8081/api/finance/health
```

---

## Testing & Validation

### Unit Tests

```bash
# All unit tests
mvnw test

# Specific test class
mvnw test -Dtest=StockServiceTest

# With coverage
mvnw test jacoco:report
# Coverage report: target/site/jacoco/index.html
```

### Integration Tests

```bash
# Integration tests only
mvnw verify -Dtest=*IntegrationTest

# Full integration test suite (requires Finance module running)
# Terminal 1:
cd finance-service-django
python manage.py runserver

# Terminal 2:
mvnw verify -Dtest=InventoryModuleIntegrationTest
```

### Manual Testing

**1. Create a Product**
```bash
curl -X POST http://localhost:8081/api/inventory/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "sku": "LAPT-001",
    "category": "Electronics",
    "costingMethod": "MOVING AVERAGE",
    "reorderPoint": 50
  }'
```

**2. Record Stock Receipt**
```bash
curl -X POST http://localhost:8081/api/inventory/stock-movements/receipt \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "warehouseId": 1,
    "quantity": 100,
    "cost": "599.99",
    "referenceNumber": "PO-2026-001"
  }'
```

**3. Record Stock Issue**
```bash
curl -X POST http://localhost:8081/api/inventory/stock-movements/issue \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "warehouseId": 1,
    "quantity": 50,
    "referenceNumber": "SO-2026-001"
  }'
```

**4. Check Inventory Value**
```bash
curl -X GET http://localhost:8081/api/inventory/valuations/total \
  -H "Authorization: Bearer $TOKEN"
```

---

## Troubleshooting

### Common Issues

**Issue 1: Database Connection Failed**
```
Error: ORA-01017: invalid username/password; logon denied
```
**Solution**:
- Verify Oracle credentials in `application.properties`
- Ensure Oracle listener running: `lsnrctl status`
- Test connection: `sqlplus userGrade/user1234@192.168.115.133:1521/XEPDB1`

**Issue 2: Hibernate Validation Error**
```
Error: Unknown entity (entity not found)
```
**Solution**:
- Ensure `@EntityScan` includes inventory entity packages in `ErpApplication.java`
- Verify `com.company.erp.inventory.domain.entity` in component scan configuration

**Issue 3: Finance API Integration Failed**
```
Error: Connection refused on http://localhost:8081/api/finance
```
**Solution**:
- Verify Finance module running: `curl http://localhost:8000/health`
- Check URL in `application.properties` (should be port 8000 for Django)
- Check logs: `grep "Finance API" logs.txt`

**Issue 4: Insufficient Stock Error**
```
Error: Insufficient stock: 50 requested but only 30 available
```
**Solution**:
- Expected behavior - receipt must come before issue
- Verify on-hand quantity: `GET /api/inventory/products/{id}/on-hand`
- Record receipt first if needed

**Issue 5: Multi-Tenancy Isolation Failing**
```
Error: Product not found (404)
```
**Solution**:
- Ensure `companyId` parameter matches authenticated user's company
- Verify SQL queries include `WHERE company_id = ?`
- Check repositories have `findByIdAndCompanyId()` method

### Logging

Enable detailed logging:

```properties
logging.level.com.company.erp.inventory=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## Performance Tuning

### Database Optimization

```sql
-- Add indexes for common queries
CREATE INDEX idx_products_active ON products(company_id, active);
CREATE INDEX idx_warehouse_active ON warehouses(company_id, active);
CREATE INDEX idx_movement_product ON stock_movements(product_id, movement_date DESC);
CREATE INDEX idx_inventory_trans_warehouse ON inventory_transactions(warehouse_id, transaction_date DESC);
CREATE INDEX idx_reorder_active ON reorder_rules(company_id, product_id, active);

-- Analyze for query optimization
ANALYZE TABLE products;
ANALYZE TABLE stock_movements;
ANALYZE TABLE inventory_transactions;
```

### Hibernatetuning

```properties
# Connection pooling
spring.datasource.maximum-pool-size=20
spring.datasource.minimum-idle=5
spring.datasource.idle-timeout=600000

# Hibernate batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=25
spring.jpa.properties.hibernate.jdbc.fetch_size=100
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Query caching (optional)
spring.jpa.properties.hibernate.cache.use_query_cache=true
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
```

### API Response Caching

```java
@Cacheable(cacheNames = "products", key = "#companyId")
public List<Product> getActiveProducts(String companyId) {
    return productRepository.findByCompanyIdAndActive(companyId, true);
}
```

---

## Maintenance

### Regular Tasks

| Task | Frequency | Purpose |
|------|-----------|---------|
| Database backup | Daily | Disaster recovery |
| Cleanup old transactions | Monthly | Storage optimization |
| Cost layer archival | Quarterly | Historical cost tracking |
| Reorder rule review | Quarterly | EOQ recalibration |
| Dead stock writeoff | Quarterly | Inventory cleanup |

### Monitoring Alerts

**Monitor these metrics**:
- Stock movement processing time (target: < 1 sec)
- Finance API integration failures (target: < 0.1%)
- Dead stock value trend (flag if increasing)
- Reorder automation accuracy (target: > 95%)

### Version Upgrade Plan

- **Next Version (2.0)**:
  - Barcode/RFID scanning integration
  - Warehouse automation (WMS) integration
  - Demand forecasting with machine learning
  - Production module integration (BOM/routing)

---

## Support & Contact

**Development Team**  
**ERP System Repository**: `c:\Users\Devoe\ErpSystem`  
**Documentation**: See `INVENTORY_API_DOCUMENTATION.md` for detailed endpoint specs

**Last Updated**: February 19, 2026
