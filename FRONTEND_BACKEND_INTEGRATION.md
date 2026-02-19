# NEXORA Frontend-Backend Integration Guide

## Overview
The NEXORA ERP frontend (React + Vite) is fully integrated with the Spring Boot backend running on port 8081. All API communications go through centralized services in `api.js`.

## Integration Status

### ✅ Architecture
- **Frontend**: React 18+ with React Router, Vite build tool
- **Backend**: Spring Boot 3.2.5, Java 21
- **Communication**: REST API via Axios
- **CORS**: Enabled for localhost development
- **API Base URL**: `http://localhost:8081/api`

### ✅ Centralized API Services
All frontend components import services from `src/services/api.js`:

**HR Services** (with `/api/hr` prefix):
- `employeeService` → `/api/hr/employees`
- `departmentService` → `/api/hr/department`
- `jobService` (Positions) → `/api/hr/jobs`
- `locationService` → `/api/hr/locations`
- `attendanceService` → `/api/hr/attendances`
- `leaveRequestService` (Leaves) → `/api/hr/leave-requests`
- `holidayService` → `/api/hr/holidays`
- `salaryService` → `/api/hr/salary`
- `payrollService` → `/api/hr/payroll`
- `deductionService` → `/api/hr/deductions`
- `reviewService` → `/api/hr/reviews`
- `goalService` → `/api/hr/goals`
- `hrUserRoleService` (HR Roles) → `/api/hr/roles`

**Finance Services** (with `/api` prefix):
- `companyService` → `/api/companies`
- `chartOfAccountsService` → `/api/chart-of-accounts`
- `costCenterService` → `/api/cost-centers`
- `journalEntryService` → `/api/journal-entries`
- `journalEntryLineService` → `/api/journal-entry-lines`
- `customerService` → `/api/customers`
- `invoiceService` → `/api/invoices`
- `customerPaymentService` → `/api/customer-payments`
- `arAgingService` → `/api/ar-aging`
- `supplierService` → `/api/suppliers`
- `billService` → `/api/bills`
- `supplierPaymentService` → `/api/supplier-payments`
- `bankAccountService` → `/api/bank-accounts`
- `bankTransactionService` → `/api/bank-transactions`
- `trialBalanceService` → `/api/trial-balance`
- `financeUserRoleService` (Finance Roles) → `/api/user-roles`

**Auth Services**:
- `authService` → `/api/auth/*`
- `userService` → `/api/users`
- `roleService` → `/api/roles`

### ✅ CORS Configuration
Backend CORS enables requests from:
- `http://localhost:4200` (Angular dev server)
- `http://localhost:5173` (Vite dev server - PRIMARY)
- `http://localhost:5174`
- `http://localhost:5175`

Allowed methods: GET, POST, PUT, DELETE, OPTIONS
Allowed headers: * (all)
Credentials: Enabled

---

## Component Integration

### HR Module Components
All HR components now use `api.js` services:

| Component | Service | Endpoint |
|----------|---------|----------|
| HRDashboard | employeeService, departmentService, leaveRequestService, salaryService | Multiple |
| Employees | employeeService | `/api/hr/employees` |
| Departments | departmentService | `/api/hr/department` |
| Positions | jobService | `/api/hr/jobs` |
| Locations | locationService | `/api/hr/locations` |
| Attendance | attendanceService | `/api/hr/attendances` |
| Leaves | leaveRequestService | `/api/hr/leave-requests` |
| Holidays | holidayService | `/api/hr/holidays` |
| Salary | salaryService | `/api/hr/salary` |
| Payroll | payrollService | `/api/hr/payroll` |
| Deductions | deductionService | `/api/hr/deductions` |
| Reviews | reviewService | `/api/hr/reviews` |
| Goals | goalService | `/api/hr/goals` |
| HRUserRoles | hrUserRoleService | `/api/hr/roles` |

### Finance Module Components
All Finance components use `api.js` services:

| Component | Service | Endpoint |
|----------|---------|----------|
| FinanceDashboard | invoiceService, billService, customerService, supplierService | Multiple |
| Invoices | invoiceService | `/api/invoices` |
| Companies | companyService | `/api/companies` |
| ChartOfAccounts | chartOfAccountsService | `/api/chart-of-accounts` |
| CostCenters | costCenterService | `/api/cost-centers` |
| JournalEntries | journalEntryService | `/api/journal-entries` |
| JournalEntryLines | journalEntryLineService | `/api/journal-entry-lines` |
| Customers | customerService | `/api/customers` |
| CustomerPayments | customerPaymentService | `/api/customer-payments` |
| ARAging | arAgingService | `/api/ar-aging` |
| Suppliers | supplierService | `/api/suppliers` |
| Bills | billService | `/api/bills` |
| SupplierPayments | supplierPaymentService | `/api/supplier-payments` |
| BankAccounts | bankAccountService | `/api/bank-accounts` |
| BankTransactions | bankTransactionService | `/api/bank-transactions` |
| TrialBalance | trialBalanceService | `/api/trial-balance` |
| FinanceUserRoles | financeUserRoleService | `/api/user-roles` |

---

## Running the Full Stack

### 1. Start Spring Boot Backend
```powershell
cd c:\Users\Devoe\ErpSystem\erp-core-spring
# Optional: set environment variables for custom database
# $env:SPRING_APPLICATION_JSON='{"spring":{"datasource":{"url":"...","username":"...","password":"..."}}}'
java -jar ERPMain\target\ERPMain-1.0.0.jar
```
Expected output: `Started ErpApplication in ... seconds`

### 2. Start Frontend Dev Server
```powershell
cd c:\Users\Devoe\ErpSystem\erp-frontend
npm install  # Only if dependencies not installed
npm run dev
```
Expected output: `> Local: http://localhost:5173/`

### 3. Access Application
- Open browser to `http://localhost:5173`
- App shows NEXORA header with navigation
- Click "HR" or "Finance" to access modules
- Data loads from backend at `http://localhost:8081/api`

---

## API Response Normalization

Frontend components handle flexible API response formats:

```javascript
// Component normalizes various response structures:
const normalizeList = (payload) => {
  if (!payload || Array.isArray(payload)) return payload || []
  // Tries multiple field names:
  return payload.content || payload.data || payload.results || payload.items || []
}
```

This allows the frontend to work with different JPA repository response structures.

---

## Error Handling

All services use Axios with error catching. Components display:
- "Loading..." while fetching
- Error message if request fails
- Empty state if no data returned
- Table/list of records when successful

Example:
```javascript
try {
  const response = await employeeService.getAll()
  const data = normalizeList(response.data)
  setItems(data)
} catch (error) {
  setError(`Failed to load Employees`)
}
```

---

## Health Checks

The frontend includes a health check service:

```javascript
// Check if backend is running
const health = await healthService.checkSpringBoot()
// Returns: { healthy: true|false, service: 'Spring Boot' }
```

---

## Database Configuration

### Spring Boot (Default Oracle)
File: `erp-core-spring/UserService/src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:oracle:thin:@//192.168.115.133:1521/XEPDB1
spring.datasource.username=userGrade
spring.datasource.password=user1234
```

For PostgreSQL override at runtime:
```powershell
$env:SPRING_APPLICATION_JSON='{"spring":{"datasource":{"url":"jdbc:postgresql://localhost:5433/erp_db","username":"postgres","password":"123","driver-class-name":"org.postgresql.Driver"},"jpa":{"hibernate":{"ddl-auto":"update"}}}}'
```

---

## Troubleshooting

### "Cannot GET /api/..." (404)
- ✓ Backend is running on port 8081
- ✓ Controller exists with correct @RequestMapping
- ✓ Check endpoint URL in api.js matches controller path

### "CORS error" or "Access denied"
- ✓ WebConfig.java has CORS mapping for /api/**
- ✓ Request origin is in allowedOrigins list
- ✓ Browser console shows OPTIONS preflight request

### "No data appears in UI"
- ✓ Check browser Network tab for API calls
- ✓ Verify response format (should be JSON array or object)
- ✓ Check normalizeList() handles response structure

### Backend won't start
- ✓ Ensure Java 21 is installed: `java -version`
- ✓ Check Oracle/database connection: see SPRINGBOOT_SETUP.md
- ✓ Verify ERPMain jar was built: `ls erp-core-spring/ERPMain/target/`

---

## File Locations

- **API Services**: `erp-frontend/src/services/api.js`
- **HR Components**: `erp-frontend/src/pages/hr/*.jsx`
- **Finance Components**: `erp-frontend/src/pages/finance/*.jsx`
- **Frontend Router**: `erp-frontend/src/App.jsx`
- **Backend CORS Config**: `erp-core-spring/ViewAndControllers/src/main/java/.../WebConfig.java`
- **Backend Controllers**: `erp-core-spring/ViewAndControllers/src/main/java/com/company/userService/**/controller/*.java`

---

## Next Steps

1. ✅ Frontend-Backend API integration complete
2. Run both services and test data flow
3. Create sample data in database for testing
4. Implement authentication (JWT) for protected endpoints
5. Add error handling dialogs for failed requests
6. Implement pagination for large datasets
7. Add form components for create/update operations

