# Inventory Module API Documentation

**Version**: 1.0.0  
**Base URL**: `http://localhost:8081/api/inventory`  
**Authentication**: Bearer JWT Token (from UserService)  
**Date**: February 2026

---

## Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Common Headers & Parameters](#common-headers--parameters)
4. [Product Endpoints](#product-endpoints)
5. [Warehouse Endpoints](#warehouse-endpoints)
6. [Stock Movement Endpoints](#stock-movement-endpoints)
7. [Inventory Valuation Endpoints](#inventory-valuation-endpoints)
8. [Reorder Management Endpoints](#reorder-management-endpoints)
9. [Error Handling](#error-handling)
10. [Integration Examples](#integration-examples)

---

## Overview

The Inventory Module manages:
- **Product Master**: SKUs, variants, cost methods, reorder settings
- **Warehouse Management**: Multi-warehouse inventory tracking
- **Stock Movements**: Receipt, issue, and inter-warehouse transfers
- **Cost Accounting**: FIFO, LIFO, moving average, standard cost methods
- **Valuation Reporting**: Inventory value, aging, dead stock, ABC analysis
- **Reorder Automation**: EOQ calculation, safety stock, low-stock alerts

**Key Features**:
- Multi-tenancy support (company-based isolation)
- Automatic COGS journal entry creation (Finance module integration)
- Real-time inventory transaction logging
- Comprehensive valuation reporting and analytics
- Reorder rule automation with supplier integration

---

## Authentication

All endpoints require a valid JWT token in the `Authorization` header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Tokens are obtained from the UserService authentication endpoint and expire after 24 hours.

---

## Common Headers & Parameters

### Request Headers

```http
Content-Type: application/json
Authorization: Bearer {token}
```

### Common Query Parameters

- **companyId** (optional, string): Company identifier for multi-tenancy. Default: authenticated user's company.
- **limit** (optional, integer): Max results per request. Default: 100, Max: 1000.
- **offset** (optional, integer): Pagination offset. Default: 0.

### Response Format

All responses follow this structure:

**Success** (2xx):
```json
{
  "id": "uuid",
  "name": "Product Name",
  "sku": "SKU-001",
  ...otherFields
}
```

**Error** (4xx, 5xx):
```json
{
  "error": "Product not found",
  "timestamp": "2026-02-19T10:30:00Z",
  "status": 404
}
```

---

## Product Endpoints

### GET /products
List all active products.

**Query Parameters**:
- `companyId` (string, optional)
- `limit` (integer, optional)
- `offset` (integer, optional)

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "sku": "LAPT-001",
    "category": "Electronics",
    "costingMethod": "FIFO",
    "standardCost": "599.99",
    "reorderPoint": 50,
    "maxStockLevel": 200,
    "active": true,
    "createdAt": "2026-01-01T00:00:00Z"
  }
]
```

---

### GET /products/search
Search products by name or SKU.

**Query Parameters**:
- `term` (string, required): Search term
- `companyId` (string, optional)

**Example**: `GET /products/search?term=laptop`

**Response**: `200 OK` - Array of matching products

---

### GET /products/category/{category}
Filter products by category.

**Path Parameters**:
- `category` (string): Product category name

**Response**: `200 OK` - Array of products in category

---

### GET /products/sku/{sku}
Lookup product by SKU.

**Path Parameters**:
- `sku` (string): Stock keeping unit

**Response**: `200 OK` - Single product object or `404 Not Found`

---

### GET /products/{id}
Get product details by ID.

**Path Parameters**:
- `id` (integer): Product ID

**Response**: `200 OK` - Product object with all details

---

### POST /products
Create new product.

**Request Body**:
```json
{
  "name": "Monitor",
  "sku": "MON-001",
  "category": "Electronics",
  "description": "27-inch LED Monitor",
  "costingMethod": "MOVING_AVERAGE",
  "standardCost": "199.99",
  "reorderPoint": 25,
  "maxStockLevel": 100,
  "active": true
}
```

**Response**: `201 Created` - Created product with ID

---

### PUT /products/{id}
Update product details.

**Path Parameters**:
- `id` (integer): Product ID

**Request Body**: Same as POST

**Response**: `200 OK` - Updated product

---

### DELETE /products/{id}
Deactivate/delete product.

**Response**: `204 No Content`

---

### GET /products/{id}/on-hand
Get current on-hand quantity across all warehouses.

**Response**: `200 OK`
```json
{
  "productId": 1,
  "sku": "LAPT-001",
  "totalOnHand": 150,
  "byWarehouse": {
    "WH-01": 100,
    "WH-02": 50
  }
}
```

---

## Warehouse Endpoints

### GET /warehouses
List all active warehouses.

**Response**: `200 OK` - Array of warehouse objects

---

### GET /warehouses/all
List all warehouses including inactive.

**Query Parameters**:
- `companyId` (string, optional)

**Response**: `200 OK`

---

### GET /warehouses/receiving
List warehouses enabled for receiving.

**Response**: `200 OK` - Warehouses with `receivingEnabled: true`

---

### GET /warehouses/shipping
List warehouses enabled for shipping.

**Response**: `200 OK` - Warehouses with `shippingEnabled: true`

---

### GET /warehouses/search
Search warehouses by name and location.

**Query Parameters**:
- `term` (string): Search term (name or location)

**Response**: `200 OK` - Matching warehouses

---

### GET /warehouses/code/{code}
Lookup warehouse by location code.

**Path Parameters**:
- `code` (string): Warehouse location code

**Response**: `200 OK` - Warehouse object or `404 Not Found`

---

### GET /warehouses/{id}
Get warehouse details.

**Response**: `200 OK` - Warehouse object

---

### POST /warehouses
Create new warehouse.

**Request Body**:
```json
{
  "name": "Primary Warehouse",
  "code": "WH-01",
  "location": "New York",
  "address": "123 Main St",
  "receivingEnabled": true,
  "shippingEnabled": true,
  "active": true,
  "manager": "John Doe",
  "phone": "555-0001"
}
```

**Response**: `201 Created`

---

### PUT /warehouses/{id}
Update warehouse.

**Response**: `200 OK`

---

### DELETE /warehouses/{id}
Delete/deactivate warehouse.

**Response**: `204 No Content`

---

## Stock Movement Endpoints

### POST /stock-movements/receipt
Record inbound stock receipt.

**Request Body**:
```json
{
  "productId": 1,
  "warehouseId": 1,
  "quantity": 100,
  "cost": "59.99",
  "referenceNumber": "PO-2026-001",
  "notes": "Supplier delivery"
}
```

**Response**: `201 Created` - StockMovement with ID

**Behavior**:
- Creates StockMovement record
- Auto-creates corresponding InventoryTransaction
- Updates Product on-hand quantity
- No COGS entry (receipt decreases COGS)

---

### POST /stock-movements/issue
Record outbound stock issue.

**Request Body**:
```json
{
  "productId": 1,
  "warehouseId": 1,
  "quantity": 50,
  "referenceNumber": "SO-2026-001",
  "notes": "Sales order fulfillment"
}
```

**Response**: `201 Created` - StockMovement with ID

**Validation**:
- Check sufficient on-hand quantity
- Return `400 Bad Request` if insufficient stock

**Behavior**:
- Creates StockMovement record
- Creates InventoryTransaction
- Calls COGSIntegrationService to create COGS journal entry
- Updates on-hand quantity

---

### POST /stock-movements/transfer
Record inter-warehouse transfer.

**Request Body**:
```json
{
  "productId": 1,
  "fromWarehouseId": 1,
  "toWarehouseId": 2,
  "quantity": 25,
  "notes": "Inventory rebalancing"
}
```

**Response**: `201 Created`

**Behavior**:
- Creates StockMovement with status IN_TRANSIT
- Source warehouse: quantity reduced
- Target warehouse: quantity added after receiving
- No COGS impact (internal transfer)

---

### GET /stock-movements/{id}
Get movement details.

**Response**: `200 OK` - StockMovement object

---

### GET /stock-movements/product/{productId}
Get all movements for product.

**Query Parameters**:
- `limit`, `offset` for pagination

**Response**: `200 OK` - Array of StockMovement

---

### GET /stock-movements/warehouse/{warehouseId}
Get all movements for warehouse.

**Response**: `200 OK` - Array of StockMovement

---

### GET /stock-movements/pending-transfers
Get all pending inter-warehouse transfers.

**Response**: `200 OK` - Array with status IN_TRANSIT

---

### GET /stock-movements/in-transit
Get all in-transit items awaiting receiving.

**Response**: `200 OK` - Array of pending transfers

---

## Inventory Valuation Endpoints

### GET /valuations/total
Get total company inventory value.

**Query Parameters**:
- `companyId` (string, optional)

**Response**: `200 OK`
```json
{
  "totalValue": "125000.00",
  "currency": "USD",
  "lastUpdated": "2026-02-19T10:30:00Z"
}
```

---

### GET /valuations/by-warehouse
Get inventory value by warehouse.

**Response**: `200 OK`
```json
{
  "WH-01": "75000.00",
  "WH-02": "50000.00"
}
```

---

### GET /valuations/by-product
Get inventory value by product.

**Response**: `200 OK`
```json
{
  "1": {
    "sku": "LAPT-001",
    "name": "Laptop",
    "quantity": 150,
    "value": "89985.00"
  }
}
```

---

### GET /valuations/aging/{warehouseId}
Get inventory aging report by receipt date.

**Response**: `200 OK`
```json
{
  "0-30-days": "50000.00",
  "31-60-days": "30000.00",
  "61-90-days": "20000.00",
  "90-plus-days": "25000.00"
}
```

---

### GET /valuations/aging-buckets/{warehouseId}
Get detailed aging breakdown by bucket.

**Response**: `200 OK` - Detailed aging with product details

---

### GET /valuations/dead-stock
Identify dead stock (no movement in 180+ days).

**Query Parameters**:
- `daysThreshold` (integer, optional): Default 180

**Response**: `200 OK`
```json
{
  "totalDeadStockValue": "12000.00",
  "items": [
    {
      "productId": 5,
      "sku": "OLD-001",
      "quantity": 100,
      "value": "12000.00",
      "lastMovement": "2025-08-19"
    }
  ]
}
```

---

### GET /valuations/slow-moving
Identify slow-moving items.

**Query Parameters**:
- `minDaysSinceMovement` (integer): Default 90

**Response**: `200 OK` - Array of slow-moving products

---

### GET /valuations/variance
Cost variance analysis (standard vs actual).

**Response**: `200 OK`
```json
{
  "favorableVariance": "5000.00",
  "unfavorableVariance": "2000.00",
  "netVariance": "3000.00"
}
```

---

### GET /valuations/turnover
Inventory turnover ratio and metrics.

**Response**: `200 OK`
```json
{
  "annualTurnover": 4.5,
  "daysInventoryOutstanding": 81,
  "byCategory": {
    "Electronics": 5.2,
    "Accessories": 3.8
  }
}
```

---

### GET /valuations/abc-analysis
Pareto analysis (A/B/C classification).

**Response**: `200 OK`
```json
{
  "A": {
    "percentageOfValue": 80,
    "percentageOfItems": 20,
    "items": [...]
  },
  "B": {
    "percentageOfValue": 15,
    "percentageOfItems": 30,
    "items": [...]
  },
  "C": {
    "percentageOfValue": 5,
    "percentageOfItems": 50,
    "items": [...]
  }
}
```

---

### GET /valuations/obsolescence-risk
Risk scoring for obsolete inventory.

**Response**: `200 OK`
```json
{
  "high-risk": [
    {
      "productId": 3,
      "riskScore": 92,
      "reasons": ["No movement in 6+ months", "Outdated model"]
    }
  ],
  "medium-risk": [...],
  "low-risk": [...]
}
```

---

### GET /valuations/projected
Project future inventory needs.

**Query Parameters**:
- `daysAhead` (integer): Default 30

**Response**: `200 OK` - Projected inventory levels 30/60/90 days ahead

---

## Reorder Management Endpoints

### GET /reorder/needed/{warehouseId}
List products needing reorder in warehouse.

**Response**: `200 OK`
```json
[
  {
    "productId": 1,
    "sku": "LAPT-001",
    "currentQuantity": 40,
    "reorderPoint": 50,
    "suggestedQuantity": 110
  }
]
```

---

### GET /reorder/suggestions/{warehouseId}
Get reorder suggestions with ranking.

**Response**: `200 OK` - Prioritized list with EOQ and supplier info

---

### GET /reorder/low-stock/{warehouseId}
List approaching low stock (70-99% of reorder point).

**Response**: `200 OK` - Early warning items

---

### GET /reorder/overstock/{warehouseId}
List overstock items (above max level).

**Response**: `200 OK` - Items exceeding max stock

---

### GET /reorder/rules/{productId}/{warehouseId}
Get reorder rule for product in warehouse.

**Response**: `200 OK`
```json
{
  "id": 10,
  "productId": 1,
  "warehouseId": 1,
  "reorderPoint": 50,
  "maxStockLevel": 200,
  "economicOrderQuantity": 224,
  "leadTimeDays": 14,
  "active": true
}
```

---

### POST /reorder/rules
Create reorder rule.

**Request Body**:
```json
{
  "productId": 1,
  "warehouseId": 1,
  "reorderPoint": 50,
  "maxStockLevel": 200,
  "leadTimeDays": 14,
  "holdingCostPerYear": 2.00,
  "orderingCostPerOrder": 50.00
}
```

**Response**: `201 Created`

---

### PUT /reorder/rules/{id}
Update reorder rule.

**Response**: `200 OK`

---

### POST /reorder/eoq
Calculate Economic Order Quantity.

**Request Body**:
```json
{
  "annualDemandUnits": 1000,
  "orderingCostPerOrder": 50.00,
  "annualHoldingCostPerUnit": 2.00
}
```

**Response**: `200 OK`
```json
{
  "eoq": 224,
  "formula": "√(2DS/H) = √(2*1000*50/2) = √50000"
}
```

---

### GET /reorder/days-of-supply/{productId}/{warehouseId}
Calculate days of supply based on usage rate.

**Response**: `200 OK`
```json
{
  "daysOfSupply": 50,
  "currentQuantity": 500,
  "dailyAvgUsage": 10
}
```

---

### POST /reorder/generate-po
Generate purchase order for reorder.

**Request Body**:
```json
{
  "productId": 1,
  "warehouseId": 1,
  "vendorId": 5,
  "quantity": 224
}
```

**Response**: `201 Created`
```json
{
  "poId": "PO-2026-001",
  "productId": 1,
  "quantity": 224,
  "unitPrice": "59.99",
  "expectedDelivery": "2026-03-05",
  "status": "CREATED"
}
```

---

## Error Handling

### Common Error Codes

| Status | Error | Meaning |
|--------|-------|---------|
| 400 | Bad Request | Invalid input (validation failed) |
| 401 | Unauthorized | Missing/invalid JWT token |
| 403 | Forbidden | Insufficient permissions for company |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Business logic violation (e.g., insufficient stock) |
| 500 | Server Error | Unexpected backend error |

### Error Response Format

```json
{
  "error": "Insufficient stock: 50 requested but only 30 available",
  "timestamp": "2026-02-19T10:30:00Z",
  "status": 400,
  "path": "/api/inventory/stock-movements/issue"
}
```

---

## Integration Examples

### Example 1: Record Product Receipt

```bash
curl -X POST http://localhost:8081/api/inventory/stock-movements/receipt \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "warehouseId": 1,
    "quantity": 100,
    "cost": "59.99",
    "referenceNumber": "PO-2026-001"
  }'
```

---

### Example 2: Issue Stock with COGS Integration

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

**Automatic Actions**:
1. ✅ StockMovement created
2. ✅ InventoryTransaction logged
3. ✅ On-hand quantity reduced
4. ✅ COGSIntegrationService called
5. ✅ Journal entry created in Finance module (Debit COGS, Credit Inventory)

---

### Example 3: Get Inventory Value Report

```bash
curl -X GET http://localhost:8081/api/inventory/valuations/by-warehouse \
  -H "Authorization: Bearer $TOKEN"
```

**Response**: Inventory value breakdown by warehouse location

---

### Example 4: Reorder Analysis

```bash
curl -X GET "http://localhost:8081/api/inventory/reorder/needed/1" \
  -H "Authorization: Bearer $TOKEN"
```

**Response**: Products below reorder point with suggested quantities

---

## Deployment Notes

### Prerequisites

- Java 21 JDK
- Spring Boot 3.2.5
- Maven 3.6+
- Oracle Database (or configured relational DB)
- Finance module running on port 8000 or configured URL

### Configuration

Update `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:oracle:thin:@//host:1521/DB_NAME
spring.datasource.username=inventory_user
spring.datasource.password=password

# COGS Integration
inventory.finance.api.url=http://localhost:8000/api/finance
inventory.cogs.account.code=5100
inventory.asset.account.code=1300

# Multi-tenancy
inventory.default.company.id=DEFAULT
```

### Build & Run

```bash
# Build
mvn clean install -pl ERPMain package

# Run
java -jar ERPMain/target/ERPMain-1.0.0.jar
```

---

**Last Updated**: February 19, 2026  
**Maintained By**: Development Team
