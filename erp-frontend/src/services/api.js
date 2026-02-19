import axios from 'axios';

const SPRING_BOOT_API = 'http://localhost:8081/api';

// Create axios instance for the backend
const springApi = axios.create({
  baseURL: SPRING_BOOT_API,
  headers: {
    'Content-Type': 'application/json',
  }
});

// Spring Boot is now the single backend for all modules.

// ============ SPRING BOOT SERVICES (Auth/Users) ============

export const authService = {
  login: (credentials) => springApi.post('/auth/login', credentials),
  logout: () => springApi.post('/auth/logout'),
  refreshToken: (token) => springApi.post('/auth/refresh', { token }),
  resetPassword: (data) => springApi.post('/auth/reset-password', data),
};

export const userService = {
  getAll: () => springApi.get('/users'),
  getById: (id) => springApi.get(`/users/${id}`),
  create: (userData) => springApi.post('/users', userData),
  update: (id, userData) => springApi.put(`/users/${id}`, userData),
  delete: (id) => springApi.delete(`/users/${id}`),
};

export const roleService = {
  getAll: () => springApi.get('/roles'),
  getById: (id) => springApi.get(`/roles/${id}`),
  create: (roleData) => springApi.post('/roles', roleData),
  update: (id, roleData) => springApi.put(`/roles/${id}`, roleData),
  delete: (id) => springApi.delete(`/roles/${id}`),
};

// ============ SPRING BOOT HR SERVICES ============

export const employeeService = {
  getAll: () => springApi.get('/hr/employees'),
  getById: (id) => springApi.get(`/hr/employees/${id}`),
  create: (employeeData) => springApi.post('/hr/employees', employeeData),
  update: (id, employeeData) => springApi.put(`/hr/employees/${id}`, employeeData),
  delete: (id) => springApi.delete(`/hr/employees/${id}`),
};

export const departmentService = {
  getAll: () => springApi.get('/hr/department'),
  getById: (id) => springApi.get(`/hr/department/${id}`),
  create: (deptData) => springApi.post('/hr/department', deptData),
  update: (id, deptData) => springApi.put(`/hr/department/${id}`, deptData),
  delete: (id) => springApi.delete(`/hr/department/${id}`),
};

export const attendanceService = {
  getAll: () => springApi.get('/hr/attendances'),
  getById: (id) => springApi.get(`/hr/attendances/${id}`),
  create: (attendanceData) => springApi.post('/hr/attendances', attendanceData),
  update: (id, attendanceData) => springApi.put(`/hr/attendances/${id}`, attendanceData),
  delete: (id) => springApi.delete(`/hr/attendances/${id}`),
};

export const payrollService = {
  getAll: () => springApi.get('/hr/payrolls'),
  getById: (id) => springApi.get(`/hr/payrolls/${id}`),
  create: (payrollData) => springApi.post('/hr/payrolls', payrollData),
  update: (id, payrollData) => springApi.put(`/hr/payrolls/${id}`, payrollData),
  delete: (id) => springApi.delete(`/hr/payrolls/${id}`),
  generatePayroll: (payrollId) => springApi.post(`/hr/payrolls/${payrollId}/generate`),
};

export const leaveRequestService = {
  getAll: () => springApi.get('/leave-requests'),
  getById: (id) => springApi.get(`/leave-requests/${id}`),
  create: (leaveData) => springApi.post('/leave-requests', leaveData),
  update: (id, leaveData) => springApi.put(`/leave-requests/${id}`, leaveData),
  delete: (id) => springApi.delete(`/leave-requests/${id}`),
  approve: (id) => springApi.post(`/leave-requests/${id}/approve`),
  reject: (id) => springApi.post(`/leave-requests/${id}/reject`),
};

export const jobService = {
  getAll: () => springApi.get('/hr/jobs'),
  getById: (id) => springApi.get(`/hr/jobs/${id}`),
  create: (jobData) => springApi.post('/hr/jobs', jobData),
  update: (id, jobData) => springApi.put(`/hr/jobs/${id}`, jobData),
  delete: (id) => springApi.delete(`/hr/jobs/${id}`),
};

export const bonusService = {
  getAll: () => springApi.get('/hr/bonuses'),
  getById: (id) => springApi.get(`/hr/bonuses/${id}`),
  create: (bonusData) => springApi.post('/hr/bonuses', bonusData),
  update: (id, bonusData) => springApi.put(`/hr/bonuses/${id}`, bonusData),
  delete: (id) => springApi.delete(`/hr/bonuses/${id}`),
};

export const locationService = {
  getAll: () => springApi.get('/hr/locations'),
  getById: (id) => springApi.get(`/hr/locations/${id}`),
  create: (locationData) => springApi.post('/hr/locations', locationData),
  update: (id, locationData) => springApi.put(`/hr/locations/${id}`, locationData),
  delete: (id) => springApi.delete(`/hr/locations/${id}`),
};

export const salaryService = {
  getAll: () => springApi.get('/hr/salary'),
  getById: (id) => springApi.get(`/hr/salary/${id}`),
  create: (salaryData) => springApi.post('/hr/salary', salaryData),
  update: (id, salaryData) => springApi.put(`/hr/salary/${id}`, salaryData),
  delete: (id) => springApi.delete(`/hr/salary/${id}`),
};

export const deductionService = {
  getAll: () => springApi.get('/hr/deductions'),
  getById: (id) => springApi.get(`/hr/deductions/${id}`),
  create: (deductionData) => springApi.post('/hr/deductions', deductionData),
  update: (id, deductionData) => springApi.put(`/hr/deductions/${id}`, deductionData),
  delete: (id) => springApi.delete(`/hr/deductions/${id}`),
};

export const reviewService = {
  getAll: () => springApi.get('/hr/reviews'),
  getById: (id) => springApi.get(`/hr/reviews/${id}`),
  create: (reviewData) => springApi.post('/hr/reviews', reviewData),
  update: (id, reviewData) => springApi.put(`/hr/reviews/${id}`, reviewData),
  delete: (id) => springApi.delete(`/hr/reviews/${id}`),
};

export const goalService = {
  getAll: () => springApi.get('/hr/goals'),
  getById: (id) => springApi.get(`/hr/goals/${id}`),
  create: (goalData) => springApi.post('/hr/goals', goalData),
  update: (id, goalData) => springApi.put(`/hr/goals/${id}`, goalData),
  delete: (id) => springApi.delete(`/hr/goals/${id}`),
};

export const holidayService = {
  getAll: () => springApi.get('/hr/holidays'),
  getById: (id) => springApi.get(`/hr/holidays/${id}`),
  create: (holidayData) => springApi.post('/hr/holidays', holidayData),
  update: (id, holidayData) => springApi.put(`/hr/holidays/${id}`, holidayData),
  delete: (id) => springApi.delete(`/hr/holidays/${id}`),
};

export const hrUserRoleService = {
  getAll: () => springApi.get('/hr/roles'),
  getById: (id) => springApi.get(`/hr/roles/${id}`),
  create: (roleData) => springApi.post('/hr/roles', roleData),
  update: (id, roleData) => springApi.put(`/hr/roles/${id}`, roleData),
  delete: (id) => springApi.delete(`/hr/roles/${id}`),
};

// ============ FINANCE SERVICES ============

// Core Accounting
export const companyService = {
  getAll: () => springApi.get('/companies'),
  getById: (id) => springApi.get(`/companies/${id}`),
  create: (data) => springApi.post('/companies', data),
  update: (id, data) => springApi.put(`/companies/${id}`, data),
  delete: (id) => springApi.delete(`/companies/${id}`),
};

export const chartOfAccountsService = {
  getAll: () => springApi.get('/chart-of-accounts'),
  getById: (id) => springApi.get(`/chart-of-accounts/${id}`),
  create: (data) => springApi.post('/chart-of-accounts', data),
  update: (id, data) => springApi.put(`/chart-of-accounts/${id}`, data),
  delete: (id) => springApi.delete(`/chart-of-accounts/${id}`),
  getByType: (type) => springApi.get('/chart-of-accounts/by_type', { params: { type } }),
};

export const costCenterService = {
  getAll: () => springApi.get('/cost-centers'),
  getById: (id) => springApi.get(`/cost-centers/${id}`),
  create: (data) => springApi.post('/cost-centers', data),
  update: (id, data) => springApi.put(`/cost-centers/${id}`, data),
  delete: (id) => springApi.delete(`/cost-centers/${id}`),
};

export const journalEntryService = {
  getAll: () => springApi.get('/journal-entries'),
  getById: (id) => springApi.get(`/journal-entries/${id}`),
  create: (data) => springApi.post('/journal-entries', data),
  update: (id, data) => springApi.put(`/journal-entries/${id}`, data),
  delete: (id) => springApi.delete(`/journal-entries/${id}`),
  post: (id) => springApi.post(`/journal-entries/${id}/post_entry`),
  reverse: (id) => springApi.post(`/journal-entries/${id}/reverse_entry`),
};

export const journalEntryLineService = {
  getAll: () => springApi.get('/journal-entry-lines'),
  getById: (id) => springApi.get(`/journal-entry-lines/${id}`),
  create: (data) => springApi.post('/journal-entry-lines', data),
  update: (id, data) => springApi.put(`/journal-entry-lines/${id}`, data),
  delete: (id) => springApi.delete(`/journal-entry-lines/${id}`),
};

// AR (Accounts Receivable)
export const customerService = {
  getAll: () => springApi.get('/customers'),
  getById: (id) => springApi.get(`/customers/${id}`),
  create: (data) => springApi.post('/customers', data),
  update: (id, data) => springApi.put(`/customers/${id}`, data),
  delete: (id) => springApi.delete(`/customers/${id}`),
  getARBalance: (customerId) => springApi.get(`/customers/${customerId}/ar_balance`),
};

export const invoiceService = {
  getAll: () => springApi.get('/invoices'),
  getById: (id) => springApi.get(`/invoices/${id}`),
  create: (data) => springApi.post('/invoices', data),
  update: (id, data) => springApi.put(`/invoices/${id}`, data),
  delete: (id) => springApi.delete(`/invoices/${id}`),
  post: (id) => springApi.post(`/invoices/${id}/post_invoice`),
};

export const customerPaymentService = {
  getAll: () => springApi.get('/customer-payments'),
  getById: (id) => springApi.get(`/customer-payments/${id}`),
  create: (data) => springApi.post('/customer-payments', data),
  update: (id, data) => springApi.put(`/customer-payments/${id}`, data),
  delete: (id) => springApi.delete(`/customer-payments/${id}`),
};

export const arAgingService = {
  getAll: () => springApi.get('/ar-aging'),
  getById: (id) => springApi.get(`/ar-aging/${id}`),
  calculate: (data) => springApi.post('/ar-aging/calculate', data),
};

// AP (Accounts Payable)
export const supplierService = {
  getAll: () => springApi.get('/suppliers'),
  getById: (id) => springApi.get(`/suppliers/${id}`),
  create: (data) => springApi.post('/suppliers', data),
  update: (id, data) => springApi.put(`/suppliers/${id}`, data),
  delete: (id) => springApi.delete(`/suppliers/${id}`),
  getAPBalance: (supplierId) => springApi.get(`/suppliers/${supplierId}/ap_balance`),
};

export const billService = {
  getAll: () => springApi.get('/bills'),
  getById: (id) => springApi.get(`/bills/${id}`),
  create: (data) => springApi.post('/bills', data),
  update: (id, data) => springApi.put(`/bills/${id}`, data),
  delete: (id) => springApi.delete(`/bills/${id}`),
  post: (id) => springApi.post(`/bills/${id}/post_bill`),
};

export const supplierPaymentService = {
  getAll: () => springApi.get('/supplier-payments'),
  getById: (id) => springApi.get(`/supplier-payments/${id}`),
  create: (data) => springApi.post('/supplier-payments', data),
  update: (id, data) => springApi.put(`/supplier-payments/${id}`, data),
  delete: (id) => springApi.delete(`/supplier-payments/${id}`),
};

// Banking
export const bankAccountService = {
  getAll: () => springApi.get('/bank-accounts'),
  getById: (id) => springApi.get(`/bank-accounts/${id}`),
  create: (data) => springApi.post('/bank-accounts', data),
  update: (id, data) => springApi.put(`/bank-accounts/${id}`, data),
  delete: (id) => springApi.delete(`/bank-accounts/${id}`),
};

export const bankTransactionService = {
  getAll: () => springApi.get('/bank-transactions'),
  getById: (id) => springApi.get(`/bank-transactions/${id}`),
  create: (data) => springApi.post('/bank-transactions', data),
  update: (id, data) => springApi.put(`/bank-transactions/${id}`, data),
  delete: (id) => springApi.delete(`/bank-transactions/${id}`),
};

// Financial Reports
export const trialBalanceService = {
  getAll: () => springApi.get('/trial-balance'),
  getById: (id) => springApi.get(`/trial-balance/${id}`),
  generate: (data) => springApi.post('/trial-balance', data),
};

export const incomeStatementService = {
  getAll: () => springApi.get('/income-statements'),
  getById: (id) => springApi.get(`/income-statements/${id}`),
  generate: (data) => springApi.post('/income-statements/generate', data),
};

export const balanceSheetService = {
  getAll: () => springApi.get('/balance-sheets'),
  getById: (id) => springApi.get(`/balance-sheets/${id}`),
  generate: (data) => springApi.post('/balance-sheets/generate', data),
};

export const cashFlowService = {
  getAll: () => springApi.get('/cash-flow'),
  getById: (id) => springApi.get(`/cash-flow/${id}`),
  generate: (data) => springApi.post('/cash-flow/generate', data),
};

// Unified Payments
export const paymentService = {
  getAll: () => springApi.get('/payments'),
  getById: (id) => springApi.get(`/payments/${id}`),
  create: (data) => springApi.post('/payments', data),
  update: (id, data) => springApi.put(`/payments/${id}`, data),
  delete: (id) => springApi.delete(`/payments/${id}`),
  clear: (id) => springApi.post(`/payments/${id}/clear`),
  apply: (id, data) => springApi.post(`/payments/${id}/apply`, data),
};

// Multi-Currency
export const currencyService = {
  getAll: () => springApi.get('/currencies'),
  getById: (id) => springApi.get(`/currencies/${id}`),
  create: (data) => springApi.post('/currencies', data),
  update: (id, data) => springApi.put(`/currencies/${id}`, data),
  delete: (id) => springApi.delete(`/currencies/${id}`),
  getBaseCurrency: () => springApi.get('/currencies/base'),
};

export const exchangeRateService = {
  getAll: () => springApi.get('/exchange-rates'),
  getById: (id) => springApi.get(`/exchange-rates/${id}`),
  create: (data) => springApi.post('/exchange-rates', data),
  update: (id, data) => springApi.put(`/exchange-rates/${id}`, data),
  delete: (id) => springApi.delete(`/exchange-rates/${id}`),
  getRate: (from, to, date) => springApi.get(`/exchange-rates/${from}/${to}/${date}`),
};

// Invoice/Bill Line Items
export const invoiceLineService = {
  getAll: () => springApi.get('/invoice-lines'),
  getById: (id) => springApi.get(`/invoice-lines/${id}`),
  create: (data) => springApi.post('/invoice-lines', data),
  update: (id, data) => springApi.put(`/invoice-lines/${id}`, data),
  delete: (id) => springApi.delete(`/invoice-lines/${id}`),
  getByInvoice: (invoiceId) => springApi.get(`/invoice-lines/invoice/${invoiceId}`),
};

export const billLineService = {
  getAll: () => springApi.get('/bill-lines'),
  getById: (id) => springApi.get(`/bill-lines/${id}`),
  create: (data) => springApi.post('/bill-lines', data),
  update: (id, data) => springApi.put(`/bill-lines/${id}`, data),
  delete: (id) => springApi.delete(`/bill-lines/${id}`),
  getByBill: (billId) => springApi.get(`/bill-lines/bill/${billId}`),
};

// AP Aging
export const apAgingService = {
  getAll: () => springApi.get('/ap-aging'),
  getById: (id) => springApi.get(`/ap-aging/${id}`),
  calculate: (data) => springApi.post('/ap-aging/calculate', data),
};

// Ledger & Balance Services
export const ledgerService = {
  getAccountBalance: (accountId, asOfDate) => 
    springApi.get(`/ledger/${accountId}/balance`, { params: { asOfDate } }),
  getAccountBalanceByPeriod: (accountId, fromDate, toDate) => 
    springApi.get(`/ledger/${accountId}/balance-period`, { params: { fromDate, toDate } }),
};

// Bank Reconciliation
export const reconciliationService = {
  reconcile: (bankAccountId, data) => 
    springApi.post(`/reconciliation/${bankAccountId}/reconcile`, data),
  getOutstanding: (bankAccountId, asOfDate) => 
    springApi.get(`/reconciliation/${bankAccountId}/outstanding`, { params: { asOfDate } }),
  matchPayments: (paymentId, transactionId) => 
    springApi.post(`/reconciliation/match`, { paymentId, transactionId }),
};

// Permissions
export const financeUserRoleService = {
  getAll: () => springApi.get('/user-roles'),
  getById: (id) => springApi.get(`/user-roles/${id}`),
  create: (data) => springApi.post('/user-roles', data),
  update: (id, data) => springApi.put(`/user-roles/${id}`, data),
  delete: (id) => springApi.delete(`/user-roles/${id}`),
};

// ============ HEALTH CHECKS ============

export const healthService = {
  checkSpringBoot: async () => {
    try {
      const response = await axios.get('http://localhost:8081/actuator/health', {
        validateStatus: () => true,
      });
      return { healthy: response.status < 500, service: 'Spring Boot' };
    } catch {
      return { healthy: false, service: 'Spring Boot' };
    }
  },

  checkAll: async () => {
    return [await healthService.checkSpringBoot()];
  }
};

export default {
  authService,
  userService,
  roleService,
  employeeService,
  departmentService,
  attendanceService,
  payrollService,
  leaveRequestService,
  jobService,
  bonusService,
  locationService,
  salaryService,
  deductionService,
  reviewService,
  goalService,
  holidayService,
  hrUserRoleService,
  companyService,
  chartOfAccountsService,
  costCenterService,
  journalEntryService,
  journalEntryLineService,
  customerService,
  invoiceService,
  invoiceLineService,
  customerPaymentService,
  arAgingService,
  supplierService,
  billService,
  billLineService,
  supplierPaymentService,
  bankAccountService,
  bankTransactionService,
  paymentService,
  currencyService,
  exchangeRateService,
  trialBalanceService,
  incomeStatementService,
  balanceSheetService,
  cashFlowService,
  apAgingService,
  ledgerService,
  reconciliationService,
  financeUserRoleService,
  healthService,
};
