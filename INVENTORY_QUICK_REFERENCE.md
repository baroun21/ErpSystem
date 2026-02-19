# Inventory Module - Quick Reference Guide

## Overview
Complete inventory management system for multi-warehouse operations with 6 entities, 4 services, and comprehensive reporting.

---

## Core Entities Quick Reference

| Entity | Purpose | Key Method | Table |
|--------|---------|-----------|-------|
| **Product** | Master product definition | `getOnHandQuantity()` | products |
| **ProductVariant** | Size/color/weight variants | `findByProductId()` | product_variants |
| **Warehouse** | Physical locations | `findByWarehouseCode()` | warehouses |
| **StockMovement** | Movement tracking | `recordReceipt()` | stock_movements |
| **InventoryTransaction** | Audit trail | `getTotalReceivedQuantity()` | inventory_transactions |
| **ReorderRule** | Auto-reorder policies | `findByProductAndWarehouse()` | reorder_rules |

---

## Service & Method Quick Reference

### StockService
**Real-time inventory operations**
```java
stockService.getOnHandQuantity(product, warehouse, companyId);
stockService.recordReceipt(product, warehouse, qty, cost, "PO", "PO-123", companyId);
stockService.recordIssue(product, warehouse, qty, cost, "SO", "SO-456", companyId);
stockService.transferBetweenWarehouses(product, fromWh, toWh, qty, companyId);
```

### CostingService
**Costing calculations**
```java
costingService.calculateIssueCost(product, warehouse, qty, companyId);
costingService.calculateFIFOCost(product, warehouse, qty, companyId);
costingService.calculateWeightedAverageCost(product, warehouse, companyId);
costingService.getCostVariance(product, warehouse, companyId);
```

### InventoryValuationService
**Valuation & reporting**
```java
valuationService.getTotalInventoryValue(companyId);
valuationService.getInventoryValueByWarehouse(companyId);
valuationService.identifyDeadStock(90, companyId); // No movement in 90 days
valuationService.getStockAgingBuckets(warehouse, companyId);
valuationService.getABCAnalysis(companyId); // Pareto analysis
```

### ReorderService
**Automatic reorder management**
```java
reorderService.shouldReorder(product, warehouse, companyId);
reorderService.getProductsNeedingReorder(warehouse, companyId);
reorderService.calculateEconomicOrderQuantity(product, warehouse, demand, orderCost, holdingCost);
reorderService.getLowStockItems(warehouseId, companyId);
```

---

## Common Use Cases

### 1. Create Stock Receipt
```java
StockMovement receipt = stockService.recordReceipt(
    product,
    warehouse,
    100,                          // quantity
    new BigDecimal("50.00"),      // unit cost
    "PO",
    "PO-2026-001",
    "COMPANY-001"
);
// ✅ Automatically creates InventoryTransaction for audit trail
```

### 2. Issue Stock & Verify Sufficient
```java
if (stockService.hasSufficientStock(product, warehouse, 50, companyId)) {
    StockMovement issue = stockService.recordIssue(
        product, warehouse, 50, unitCost, "SO", "SO-2026-001", companyId
    );
} else {
    // Insufficient stock error
}
```

### 3. Check On-Hand Quantity
```java
Integer onHand = stockService.getOnHandQuantity(product, warehouse, companyId);
Integer available = stockService.getAvailableStock(product, warehouse, companyId);
```

### 4. Calculate Issue Cost (FIFO Method)
```java
BigDecimal costFifo = costingService.calculateFIFOCost(
    product, warehouse, 50, companyId
);
BigDecimal totalCost = costingService.calculateTotalIssueCost(
    product, warehouse, 50, companyId
);
```

### 5. Get Reorder Recommendations
```java
List<Map<String, Object>> suggestions = reorderService.generateReorderSuggestions(
    warehouseId, companyId
);
// Returns: productId, sku, onHand, shortage, suggestedOrderQuantity, priority
```

### 6. Inventory Valuation Report
```java
BigDecimal totalValue = valuationService.getTotalInventoryValue(companyId);
Map<Long, BigDecimal> byWarehouse = valuationService.getInventoryValueByWarehouse(companyId);
Map<Long, BigDecimal> byProduct = valuationService.getInventoryValueByProduct(companyId);
```

### 7. Dead Stock Report
```java
List<Map<String, Object>> deadStock = valuationService.identifyDeadStock(
    90, // days without movement
    companyId
);
// Returns: productId, sku, name, daysSinceMovement
```

### 8. ABC Analysis (Pareto)
```java
Map<Long, String> classification = valuationService.getABCAnalysis(companyId);
// 'A' = top 80% by value, 'B' = 80-95%, 'C' = 95-100% (low value)
```

---

## Costing Methods Comparison

| Method | Calculation | Use Case | COGS Impact |
|--------|------------|----------|-------------|
| **FIFO** | Oldest receipts first | Stable/rising prices | Lower COGS when prices rising |
| **Weighted Avg** | Average of all stock | Most common | Smooths price volatility |
| **Standard Cost** | Predefined per product | Planned costs | Management control |
| **LIFO** | Newest receipts first | Rising prices preferred | Higher COGS when prices rising |

---

## Entity Relationships Diagram

```
Product
  ├── ProductVariant (1:Many)
  ├── StockMovement (1:Many)
  ├── InventoryTransaction (1:Many)
  └── ReorderRule (1:Many)

Warehouse
  ├── StockMovement (1:Many)
  ├── InventoryTransaction (1:Many)
  └── ReorderRule (1:Many)

StockMovement
  ├── Product (Many:1)
  └── Warehouse (Many:1)

InventoryTransaction
  ├── Product (Many:1)
  └── Warehouse (Many:1)

ReorderRule
  ├── Product (Many:1)
  └── Warehouse (Many:1)
```

---

## Status Codes & Meanings

### MovementStatus
- **PENDING**: Awaiting receipt confirmation
- **IN_TRANSIT**: En route (for transfers)
- **RECEIVED**: Received & counted
- **REJECTED**: Rejected upon receipt
- **CANCELLED**: Cancelled operation

### MovementType
- **RECEIPT**: Inbound stock (PO, transfer in)
- **ISSUE**: Outbound stock (SO, usage)
- **RETURN**: Returned stock
- **ADJUSTMENT**: Physical count adjustment
- **INTER_WAREHOUSE_TRANSFER**: Transfer between warehouses

---

## Repository Query Examples

### Find Products Needing Reorder
```java
Optional<ReorderRule> rule = reorderRuleRepository.findByProductAndWarehouse(
    productId, warehouseId, companyId
);
List<Product> products = productRepository.findProductsForReorderCheck(companyId);
```

### Get Stock Movement History
```java
List<StockMovement> movements = stockMovementRepository.findByProductId(
    productId, companyId
);
List<StockMovement> byWarehouse = stockMovementRepository.findByWarehouseId(
    warehouseId, companyId
);
List<StockMovement> byType = stockMovementRepository.findByMovementType(
    MovementType.RECEIPT, companyId
);
```

### Inventory Transaction Audit Trail
```java
List<InventoryTransaction> receipts = inventoryTransactionRepository.findReceiptsForCosting(
    productId, warehouseId, companyId // For FIFO/LIFO calculations
);
Integer totalReceived = inventoryTransactionRepository.getTotalReceivedQuantity(
    productId, warehouseId, companyId
);
Integer totalIssued = inventoryTransactionRepository.getTotalIssuedQuantity(
    productId, warehouseId, companyId
);
```

---

## Data Types & Precision

| Field | Type | Precision | Notes |
|-------|------|-----------|-------|
| quantity | Integer | Whole units | No decimals |
| cost/price | BigDecimal | 4 decimals | DECIMAL(15,4) |
| capacity | BigDecimal | 2 decimals | DECIMAL(15,2) |
| dates | LocalDateTime | Seconds | Stored as TIMESTAMP |
| company | String | 50 chars | Multi-tenant isolation |

---

## Performance Tips

1. **Always filter by companyId** - Multi-tenancy is enforced
2. **Use findByIdAndCompanyId()** - Ensures data isolation
3. **Lazy load relationships** - All entities use FetchType.LAZY
4. **Batch movements** - Process multiple receipts in single transaction
5. **Archive old transactions** - Keep current year active, archive rest
6. **Cache on-hand quantities** - High-frequency reads, use Redis for hot items

---

## Error Handling

### Common Exceptions
```java
// Insufficient stock
try {
    stockService.recordIssue(...);
} catch (IllegalArgumentException e) {
    // Handle: "Insufficient stock available"
}

// Warehouse not found
try {
    warehouseRepository.findByIdAndCompanyId(id, companyId)
        .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
} catch (IllegalArgumentException e) {
    // Handle: warehouse doesn't exist
}

// Invalid costing configuration
if (!costingService.validateCostingConfiguration(product)) {
    // Handle: costing method not set or standard cost missing
}
```

---

## Logging Levels

The inventory module uses SLF4J logging:
- **DEBUG**: Quantity calculations, valuation details, query results
- **INFO**: Receipt/issue recordings, reorder suggestions, system operations
- **WARN**: Insufficient stock, dead stock identified, cost overrides
- **ERROR**: Configuration issues, data validation failures

Enable DEBUG logging:
```properties
logging.level.com.company.erp.inventory=DEBUG
```

---

## API Endpoint Preview (Once Controllers Created)

```
# Stock Operations
POST   /api/inventory/stock-movements/receipt
POST   /api/inventory/stock-movements/issue
POST   /api/inventory/stock-movements/transfer
GET    /api/inventory/products/{id}/on-hand

# Costing
GET    /api/inventory/costing/{productId}/{warehouseId}/cost
GET    /api/inventory/costing/variance

# Valuation & Reports
GET    /api/inventory/valuations/total
GET    /api/inventory/valuations/by-warehouse
GET    /api/inventory/reports/dead-stock
GET    /api/inventory/reports/aging
GET    /api/inventory/reports/abc-analysis

# Reorder Management
GET    /api/inventory/reorder/suggestions/{warehouseId}
GET    /api/inventory/reorder/low-stock/{warehouseId}
POST   /api/inventory/reorder-rules/{id}
```

---

## Database Cleanup

### Archive Old Transactions
```sql
-- Archive transactions older than 1 year
INSERT INTO inventory_transactions_archive
SELECT * FROM inventory_transactions
WHERE transaction_date < DATE_SUB(NOW(), INTERVAL 1 YEAR);

DELETE FROM inventory_transactions
WHERE transaction_date < DATE_SUB(NOW(), INTERVAL 1 YEAR);
```

### Reconcile on-hand
```sql
-- Verify current on-hand matches transactions
SELECT p.id, p.sku,
       SUM(CASE WHEN mt.movement_type IN ('RECEIPT', 'RETURN') THEN it.quantity ELSE 0 END) -
       SUM(CASE WHEN mt.movement_type IN ('ISSUE', 'ADJUSTMENT') THEN it.quantity ELSE 0 END) as calculated_qty
FROM inventory_transactions it
JOIN products p ON it.product_id = p.id
GROUP BY p.id, p.sku;
```

---

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| On-hand negative | Data corruption | Run reconciliation, check transactions |
| High variance % | Wrong costing method | Verify product.costingMethod set |
| Reorder not triggering | Rule not created | Create ReorderRule for product-warehouse |
| Dead stock not showing | No movement records | Check transaction date ranges |
| ABC categories wrong | Value calculation off | Verify unitPrice/standardCost set |

---

## Configuration Properties

```properties
# Logging
logging.level.com.company.erp.inventory=INFO

# Transaction Management
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Connection Pool (for high-frequency queries)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

---

## Learning Path

1. **Start**: Read entity relationships diagram
2. **Next**: Try common use cases (create receipt, issue stock)
3. **Then**: Explore costing methods (FIFO vs Weighted Average)
4. **Advanced**: Valuation reports and ABC analysis
5. **Expert**: Custom queries, performance optimization

---

## Support & Questions

- **Architecture**: See `INVENTORY_MODULE_COMPLETION.md`
- **API**: Pending (once controllers created)
- **Database**: Check `application.properties` for connection
- **Issues**: Check logs at `logging.level.com.company.erp.inventory=DEBUG`
