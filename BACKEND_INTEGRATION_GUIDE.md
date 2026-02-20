# Finance Module Frontend - Backend Integration Guide (Spring Boot)

## 🎯 Overview

This guide outlines the API endpoints and data structures required to implement the backend support for all frontend pages in the Finance Module (Phases 1, 2 & 3) using Spring Boot JPA/Hibernate.

## 📋 Required API Endpoints

### 1. Bill Lines Management

#### List Bill Lines
```
GET /api/finance/bill-lines
```

**Query Parameters:**
- `page` (optional, default=1): pagination page number
- `limit` (optional, default=20): items per page
- `billId` (optional): filter by bill ID
- `search` (optional): search in description field

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "billId": "BILL-001",
      "lineNumber": 1,
      "description": "Office supplies",
      "quantity": 5.00,
      "unitPrice": 50.00,
      "taxAmount": 25.00,
      "lineTotal": 275.00,
      "costCenterId": "CC-001"
    }
  ],
  "total": 145,
  "page": 1,
  "limit": 20
}
```

#### Create Bill Line
```
POST /api/finance/bill-lines
```

**Request Body:**
```json
{
  "billId": "BILL-001",
  "lineNumber": 1,
  "description": "Office supplies",
  "quantity": 5.00,
  "unitPrice": 50.00,
  "taxAmount": 25.00,
  "costCenterId": "CC-001"
}
```

**Response:** (201 Created)
```json
{
  "id": 1,
  "billId": "BILL-001",
  "lineNumber": 1,
  "description": "Office supplies",
  "quantity": 5.00,
  "unitPrice": 50.00,
  "taxAmount": 25.00,
  "lineTotal": 275.00,
  "costCenterId": "CC-001"
}
```

#### Update Bill Line
```
PUT /api/finance/bill-lines/{id}
```

**Request Body:** Same as create

#### Delete Bill Line
```
DELETE /api/finance/bill-lines/{id}
```

---

### 2. Financial Reports

#### Income Statement
```
GET /api/finance/reports/income-statement
```

**Query Parameters:**
- `fiscalYear` (required): Year (e.g., 2024)
- `period` (optional, default=FULL_YEAR): FULL_YEAR | Q1 | Q2 | Q3 | Q4

**Response:**
```json
{
  "data": {
    "revenue": 500000.00,
    "begInventory": 50000.00,
    "purchases": 200000.00,
    "endInventory": 45000.00,
    "cogs": 205000.00,
    "grossProfit": 295000.00,
    "salariesWages": 100000.00,
    "depreciation": 15000.00,
    "rent": 24000.00,
    "otherOpex": 20000.00,
    "operatingIncome": 136000.00,
    "interestIncome": 2000.00,
    "interestExpense": 5000.00,
    "netIncome": 133000.00
  },
  "reportDate": "2024-12-31T00:00:00Z"
}
```

#### Balance Sheet
```
GET /api/finance/reports/balance-sheet
```

**Query Parameters:**
- `date` (required): Report date (YYYY-MM-DD format)

**Response:**
```json
{
  "data": {
    "cash": 50000.00,
    "accountsReceivable": 75000.00,
    "inventory": 85000.00,
    "otherCurrentAssets": 10000.00,
    "currentAssets": 220000.00,
    "ppe": 500000.00,
    "accumulatedDepreciation": 100000.00,
    "intangibleAssets": 50000.00,
    "goodwill": 30000.00,
    "fixedAssets": 480000.00,
    "accountsPayable": 60000.00,
    "shortTermDebt": 20000.00,
    "accruedExpenses": 15000.00,
    "deferredRevenue": 10000.00,
    "currentLiabilities": 105000.00,
    "longTermDebt": 200000.00,
    "deferredTaxLiabilities": 25000.00,
    "longTermLiabilities": 225000.00,
    "commonStock": 200000.00,
    "retainedEarnings": 170000.00
  },
  "reportDate": "2024-12-31"
}
```

#### Cash Flow Statement
```
GET /api/finance/reports/cash-flow
```

**Query Parameters:**
- `fiscalYear` (required): Year (e.g., 2024)
- `period` (optional, default=FULL_YEAR): FULL_YEAR | Q1 | Q2 | Q3 | Q4

**Response:**
```json
{
  "data": {
    "netIncome": 133000.00,
    "depreciation": 15000.00,
    "stockCompensation": 5000.00,
    "deferredTaxes": 2000.00,
    "changeAccountsReceivable": -10000.00,
    "changeInventory": 5000.00,
    "changeAccountsPayable": 8000.00,
    "changeAccruedExpenses": 2000.00,
    "operatingCashFlow": 160000.00,
    "capExpenditures": -45000.00,
    "saleOfAssets": 10000.00,
    "investmentAcquisitions": -20000.00,
    "investmentSales": 15000.00,
    "investingCashFlow": -40000.00,
    "debtProceeds": 50000.00,
    "debtRepayment": -30000.00,
    "equityProceeds": 25000.00,
    "dividends": -20000.00,
    "financingCashFlow": 25000.00,
    "beginningCash": 45000.00,
    "endingCash": 190000.00
  },
  "reportDate": "2024-12-31T00:00:00Z"
}
```

#### AP Aging Report
```
GET /api/finance/reports/ap-aging
```

**Query Parameters:**
- `date` (required): As of date (YYYY-MM-DD format)
- `vendorFilter` (optional): Vendor name search

**Response:**
```json
{
  "summary": {
    "total": 125000.00,
    "current": 50000.00,
    "days30": 35000.00,
    "days60": 25000.00,
    "days90": 10000.00,
    "days120plus": 5000.00
  },
  "details": [
    {
      "vendorId": "VEN-001",
      "vendorName": "Acme Supplies",
      "invoiceNumber": "INV-001",
      "invoiceDate": "2024-11-15",
      "dueDate": "2024-12-15",
      "daysOutstanding": 16,
      "amountDue": 5000.00
    },
    {
      "vendorId": "VEN-002",
      "vendorName": "Global Materials",
      "invoiceNumber": "INV-042",
      "invoiceDate": "2024-10-20",
      "dueDate": "2024-11-20",
      "daysOutstanding": 41,
      "amountDue": 12000.00
    }
  ],
  "reportDate": "2024-12-01"
}
```

---

## 🗂️ Data Structure Reference

### Bill Line Item (JPA Entity)
```java
@Entity
@Table(name = "bill_lines")
public class BillLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
    
    @Column(name = "line_number")
    private Integer lineNumber;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "quantity", precision = 10, scale = 4)
    private BigDecimal quantity;
    
    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;
    
    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Transient
    public BigDecimal getLineTotal() {
        return (quantity.multiply(unitPrice)).add(taxAmount);
    }
}
```

---

## 📊 Report Calculation Logic

### Income Statement
```
Revenue = Sum of all invoices (invoiced products/services)
COGS = Beginning Inventory + Purchases - Ending Inventory
Gross Profit = Revenue - COGS
Operating Expenses = Salaries + Depreciation + Rent + Other
Operating Income = Gross Profit - Operating Expenses
Net Income = Operating Income + Interest Income - Interest Expense
```

### Balance Sheet
```
Current Assets = Cash + AR + Inventory + Other Current
Fixed Assets = PP&E - Accumulated Depreciation + Intangibles + Goodwill
Total Assets = Current Assets + Fixed Assets

Current Liabilities = AP + Short-term Debt + Accrued + Deferred Revenue
Long-term Liabilities = Long-term Debt + Deferred Tax
Total Liabilities = Current + Long-term

Stockholders' Equity = Common Stock + Retained Earnings
Total Equity = Stockholders' Equity

Total Liabilities & Equity = Total Liabilities + Total Equity
(Must equal Total Assets for balance)
```

### Cash Flow Statement
```
Operating Cash Flow = Net Income + Depreciation + Adjustments + Working Capital Changes
Investing Cash Flow = -CapEx + Asset Sales + Investment Changes
Financing Cash Flow = Debt Proceeds - Debt Repayment + Equity - Dividends

Net Change in Cash = Operating + Investing + Financing
Ending Cash = Beginning Cash + Net Change
```

### AP Aging
```
For each unpaid invoice:
  Days Outstanding = Report Date - Invoice Date
  If Days <= 30: Current
  Else If Days <= 60: 31-60 days
  Else If Days <= 90: 61-90 days
  Else If Days <= 120: 91-120 days
  Else: Over 120 days

Summary totals by category
```

---

## 🔐 Security Considerations

1. **Authentication**: All endpoints require valid JWT token
2. **Authorization**: Users should only see data for their assigned cost centers/companies
3. **Date Range**: Enforce reasonable date ranges to prevent performance issues
4. **Pagination**: Always paginate list endpoints to prevent large data transfers

---

## ✅ Implementation Checklist

- [ ] Create JPA Entity classes for finance domain (Bill, Invoice, Customer, Supplier, etc.)
- [ ] Create Spring Data JPA repositories for all entities
- [ ] Create DTOs for API request/response payloads
- [ ] Create MapStruct mappers for Entity ↔ DTO conversion
- [ ] Create Spring REST controllers with endpoints
- [ ] Implement service layer with business logic
- [ ] Implement report calculation services
- [ ] Add request validation (@Valid, @NotNull, etc.)
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Add unit tests for services
- [ ] Add integration tests for controllers
- [ ] Performance optimization for large datasets
- [ ] Add caching for report data (optional)

---

## 🧪 Example Test Cases

### Bill Lines (Spring Boot / Mockito)
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class BillLineControllerTest {
    @Test
    public void testCreateBillLine() { ... }
    @Test
    public void testBillLineWithInvalidQuantity() { ... }
    @Test
    public void testDeleteBillLine() { ... }
    @Test
    public void testFilterBillLinesByBillId() { ... }
}
```

### Reports (Service Layer Tests)
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class FinanceReportServiceTest {
    @Test
    public void testIncomeStatementForFullYear() { ... }
    @Test
    public void testIncomeStatementForQ1() { ... }
    @Test
    public void testBalanceSheetOnSpecificDate() { ... }
    @Test
    public void testCashFlowStatement() { ... }
    @Test
    public void testAPAgingWithDifferentDateRanges() { ... }
}
```

---

## 📝 Notes

- All monetary values should use `BigDecimal` for accuracy (Spring Data JPA best practice)
- Dates should be `LocalDate` or `LocalDateTime` (Java Time API)
- All reports are read-only (GET endpoints only)
- Report generation may require caching for large datasets
- Use MapStruct for Entity → DTO conversion
- Add `@Transactional` to service methods for transaction management
- Use Spring Data JPA `@Query` for complex database operations
- Follow Spring Boot naming conventions for repositories and services

---

**Last Updated**: December 2024
**API Version**: 1.0
**Status**: Ready for Backend Implementation
