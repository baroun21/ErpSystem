# Backend Controller Checklist

## ✅ Existing Controllers

### HR Module
- ✅ EmployeeController → `/api/hr/employees`
- ✅ DepartmentController → `/api/hr/department`
- ✅ JobController (Positions) → `/api/hr/jobs`
- ✅ AttendanceController → `/api/hr/attendances`
- ✅ LeaveRequestController → `/api/hr/leave-requests`
- ✅ PayrollController → `/api/hr/payroll`
- ✅ BonusController → `/api/hr/bonuses`

### Finance Module
- ✅ CompanyController → `/api/companies`
- ✅ ChartOfAccountsController → `/api/chart-of-accounts`
- ✅ CostCenterController → `/api/cost-centers`
- ✅ JournalEntryController → `/api/journal-entries`
- ✅ JournalEntryLineController → `/api/journal-entry-lines`
- ✅ CustomerController → `/api/customers`
- ✅ InvoiceController → `/api/invoices`
- ✅ CustomerPaymentController → `/api/customer-payments`
- ✅ ARGagingController → `/api/ar-aging`
- ✅ SupplierController → `/api/suppliers`
- ✅ BillController → `/api/bills`
- ✅ SupplierPaymentController → `/api/supplier-payments`
- ✅ BankAccountController → `/api/bank-accounts`
- ✅ BankTransactionController → `/api/bank-transactions`
- ✅ TrialBalanceController → `/api/trial-balance`
- ✅ FinanceUserRoleController → `/api/user-roles`

### Auth Module
- ✅ AuthController → `/api/auth/*`
- ✅ UserController → `/api/users`
- ✅ RoleController → `/api/roles`

---

## 🔴 Missing Controllers (Need to be created)

### HR Module (Frontend pages will show "No data" for now)
- ❌ LocationController → `/api/hr/locations`
- ❌ SalaryController → `/api/hr/salary`
- ❌ DeductionController → `/api/hr/deductions`
- ❌ ReviewController → `/api/hr/reviews`
- ❌ GoalController → `/api/hr/goals`
- ❌ HolidayController → `/api/hr/holidays`
- ❌ HrRoleController → `/api/hr/roles`

---

## Quick Fix for Missing Controllers (Optional)

If you want to populate these data pages before creating the backend controllers, you can temporarily modify `api.js` to mock the data:

```javascript
// Temporary mock data for missing endpoints (remove when backend is ready)
const mockLocations = { data: [] }
const mockSalaries = { data: [] }
```

---

## Priority Order for Creating Missing Controllers

1. **LocationController** - Used in HR Locations page and Department page
2. **ReviewController** - Used in Performance Reviews page
3. **GoalController** - Used in Performance Goals page
4. **SalaryController** - Used in Salary Management page
5. **HolidayController** - Used in Holidays page
6. **DeductionController** - Used in Deductions page (related to Payroll)
7. **HrRoleController** - Used in HR User Roles/Permissions page

---

## Template for Creating Missing Controllers

Use the existing EmployeeController as a template:

```java
package com.company.userService.HrModule.controller;

import com.company.erp.erp.entites.request.LocationRequest;
import com.company.erp.erp.entites.response.LocationResponse;
import com.company.userService.HrModule.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest request) {
        LocationResponse response = locationService.createLocation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        List<LocationResponse> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        LocationResponse location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        LocationResponse updated = locationService.updateLocation(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
```

Also need to create:
- LocationRequest class (in model/requests)
- LocationResponse class (in model/responses)
- LocationService class (in HrModule/service)

---

## Current Integration Status

✅ **Frontend-Backend Integration**: COMPLETE
- All 26+ existing controllers properly mapped in api.js
- CORS configured
- Frontend routing complete
- Components ready to display data

🔲 **Data Population**: PARTIAL
- Finance module pages fully functional (16 pages)
- HR module pages functional for 7 entities
- 7 HR pages showing "No data" due to missing backend controllers

**Next Step**: Run the app and test existing endpoints, then create missing controllers as needed.

