# Frontend API Services Reference

This document lists all the backend services integrated into the frontend.

## Spring Boot Services (Port 8081)

### Finance Module Services (Spring Boot JPA)

All finance endpoints are implemented in Spring Boot and share the same database as the core ERP:

**Base URL**: `http://localhost:8081/api`
- **authService** - Login, logout, token refresh, password reset
  - `login(credentials)` - User login
  - `logout()` - User logout
  - `refreshToken(token)` - Refresh JWT token
  - `resetPassword(data)` - Reset user password

- **userService** - User CRUD operations
  - `getAll()` - Get all users
  - `getById(id)` - Get user by ID
  - `create(userData)` - Create new user
  - `update(id, userData)` - Update user
  - `delete(id)` - Delete user

- **roleService** - Role management
  - `getAll()` - Get all roles
  - `getById(id)` - Get role by ID
  - `create(roleData)` - Create new role
  - `update(id, roleData)` - Update role
  - `delete(id)` - Delete role

### HR Module Services

- **employeeService** - Employee management
  - `getAll()` - Get all employees
  - `getById(id)` - Get employee by ID
  - `create(employeeData)` - Create new employee
  - `update(id, employeeData)` - Update employee
  - `delete(id)` - Delete employee
  - **API Endpoint:** `/api/hr/employees`

- **departmentService** - Department management
  - `getAll()` - Get all departments
  - `getById(id)` - Get department by ID
  - `create(deptData)` - Create new department
  - `update(id, deptData)` - Update department
  - `delete(id)` - Delete department
  - **API Endpoint:** `/api/hr/department`

- **attendanceService** - Attendance tracking
  - `getAll()` - Get all attendance records
  - `getById(id)` - Get attendance by ID
  - `create(attendanceData)` - Create attendance record
  - `update(id, attendanceData)` - Update attendance
  - `delete(id)` - Delete attendance record
  - **API Endpoint:** `/api/hr/attendances`

- **payrollService** - Payroll management
  - `getAll()` - Get all payroll records
  - `getById(id)` - Get payroll by ID
  - `create(payrollData)` - Create payroll record
  - `update(id, payrollData)` - Update payroll
  - `delete(id)` - Delete payroll record
  - `generatePayroll(payrollId)` - Generate payroll
  - **API Endpoint:** `/api/hr/payrolls`

- **leaveRequestService** - Leave request management
  - `getAll()` - Get all leave requests
  - `getById(id)` - Get leave request by ID
  - `create(leaveData)` - Create leave request
  - `update(id, leaveData)` - Update leave request
  - `delete(id)` - Delete leave request
  - `approve(id)` - Approve leave request
  - `reject(id)` - Reject leave request
  - **API Endpoint:** `/api/leave-requests`

- **jobService** - Job position management
  - `getAll()` - Get all job positions
  - `getById(id)` - Get job position by ID
  - `create(jobData)` - Create job position
  - `update(id, jobData)` - Update job position
  - `delete(id)` - Delete job position
  - **API Endpoint:** `/api/hr/jobs`

- **bonusService** - Bonus management
  - `getAll()` - Get all bonuses
  - `getById(id)` - Get bonus by ID
  - `create(bonusData)` - Create bonus
  - `update(id, bonusData)` - Update bonus
  - `delete(id)` - Delete bonus
  - **API Endpoint:** `/api/hr/bonuses`

## Django Finance Services (Port 8000)

### Core Accounting

**DEPRECATED** - Finance module is now integrated into Spring Boot at port 8081. All endpoints below are now served by Spring Boot controllers.

**Old endpoints (for reference)**:

- **companyService** - Company management
  - `getAll()` - Get all companies
  - `getById(id)` - Get company by ID
  - `create(data)` - Create company
  - `update(id, data)` - Update company
  - `delete(id)` - Delete company
  - **API Endpoint:** `/api/companies/`

- **chartOfAccountsService** - Chart of accounts management
  - `getAll()` - Get all accounts
  - `getById(id)` - Get account by ID
  - `create(data)` - Create account
  - `update(id, data)` - Update account
  - `delete(id)` - Delete account
  - `getByType(type)` - Get accounts by type
  - **API Endpoint:** `/api/chart-of-accounts/`

- **costCenterService** - Cost center management
  - `getAll()` - Get all cost centers
  - `getById(id)` - Get cost center by ID
  - `create(data)` - Create cost center
  - `update(id, data)` - Update cost center
  - `delete(id)` - Delete cost center
  - **API Endpoint:** `/api/cost-centers/`

- **journalEntryService** - Journal entry management
  - `getAll()` - Get all journal entries
  - `getById(id)` - Get journal entry by ID
  - `create(data)` - Create journal entry
  - `update(id, data)` - Update journal entry
  - `delete(id)` - Delete journal entry
  - `post(id)` - Post journal entry
  - `reverse(id)` - Reverse journal entry
  - **API Endpoint:** `/api/journal-entries/`

- **journalEntryLineService** - Journal entry line items
  - `getAll()` - Get all journal entry lines
  - `getById(id)` - Get journal entry line by ID
  - `create(data)` - Create journal entry line
  - `update(id, data)` - Update journal entry line
  - `delete(id)` - Delete journal entry line
  - **API Endpoint:** `/api/journal-entry-lines/`

### Accounts Receivable (AR)

- **customerService** - Customer management
  - `getAll()` - Get all customers
  - `getById(id)` - Get customer by ID
  - `create(data)` - Create customer
  - `update(id, data)` - Update customer
  - `delete(id)` - Delete customer
  - `getARBalance(customerId)` - Get AR balance for customer
  - **API Endpoint:** `/api/customers/`

- **invoiceService** - Invoice management
  - `getAll()` - Get all invoices
  - `getById(id)` - Get invoice by ID
  - `create(data)` - Create invoice
  - `update(id, data)` - Update invoice
  - `delete(id)` - Delete invoice
  - `post(id)` - Post invoice
  - `reversePost(id)` - Reverse posted invoice
  - **API Endpoint:** `/api/invoices/`

- **customerPaymentService** - Customer payment tracking
  - `getAll()` - Get all customer payments
  - `getById(id)` - Get customer payment by ID
  - `create(data)` - Create payment record
  - `update(id, data)` - Update payment
  - `delete(id)` - Delete payment
  - **API Endpoint:** `/api/customer-payments/`

- **arAgingService** - AR aging reports
  - `getAll()` - Get AR aging data
  - `getById(id)` - Get specific aging record
  - **API Endpoint:** `/api/ar-aging/`

### Accounts Payable (AP)

- **supplierService** - Supplier management
  - `getAll()` - Get all suppliers
  - `getById(id)` - Get supplier by ID
  - `create(data)` - Create supplier
  - `update(id, data)` - Update supplier
  - `delete(id)` - Delete supplier
  - **API Endpoint:** `/api/suppliers/`

- **billService** - Bill management
  - `getAll()` - Get all bills
  - `getById(id)` - Get bill by ID
  - `create(data)` - Create bill
  - `update(id, data)` - Update bill
  - `delete(id)` - Delete bill
  - `post(id)` - Post bill
  - `reversePost(id)` - Reverse posted bill
  - **API Endpoint:** `/api/bills/`

- **supplierPaymentService** - Supplier payment tracking
  - `getAll()` - Get all supplier payments
  - `getById(id)` - Get supplier payment by ID
  - `create(data)` - Create payment record
  - `update(id, data)` - Update payment
  - `delete(id)` - Delete payment
  - **API Endpoint:** `/api/supplier-payments/`

### Banking

- **bankAccountService** - Bank account management
  - `getAll()` - Get all bank accounts
  - `getById(id)` - Get bank account by ID
  - `create(data)` - Create bank account
  - `update(id, data)` - Update bank account
  - `delete(id)` - Delete bank account
  - **API Endpoint:** `/api/bank-accounts/`

- **bankTransactionService** - Bank transaction tracking
  - `getAll()` - Get all bank transactions
  - `getById(id)` - Get bank transaction by ID
  - `create(data)` - Create transaction
  - `update(id, data)` - Update transaction
  - `delete(id)` - Delete transaction
  - **API Endpoint:** `/api/bank-transactions/`

### Financial Reports

- **trialBalanceService** - Trial balance reports
  - `getAll()` - Get all trial balances
  - `getById(id)` - Get trial balance by ID
  - `generate(data)` - Generate new trial balance
  - **API Endpoint:** `/api/trial-balance/`

### Permissions & Roles

- **financeUserRoleService** - User role management for finance module
  - `getAll()` - Get all user roles
  - `getById(id)` - Get user role by ID
  - `create(data)` - Create user role
  - `update(id, data)` - Update user role
  - `delete(id)` - Delete user role
  - **API Endpoint:** `/api/user-roles/`

## Health Check Service

- **healthService** - Backend health monitoring
  - `checkSpringBoot()` - Check if Spring Boot is running
  - `checkDjango()` - Check if Django Finance is running
  - `checkAll()` - Check all backend services at once

## Usage Examples

```javascript
// Importing services
import { 
  employeeService, 
  invoiceService, 
  healthService 
} from '../services/api'

// Getting all employees
const response = await employeeService.getAll()

// Creating a new employee
await employeeService.create({
  firstName: 'John',
  lastName: 'Doe',
  email: 'john@example.com'
})

// Getting all invoices
const invoices = await invoiceService.getAll()

// Posting an invoice
await invoiceService.post(invoiceId)

// Checking service health
const health = await healthService.checkAll()
```

## Notes

- Spring Boot runs on `http://localhost:8081`
- Django Finance runs on `http://localhost:8000`

**DEPRECATED** - Finance is now in Spring Boot on port 8081
- All services use proper error handling with try/catch
- API responses vary by endpoint - check the backend documentation
- Some endpoints may require authentication (JWT token)
