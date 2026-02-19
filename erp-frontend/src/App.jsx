import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import { useState, useEffect } from 'react'
import Dashboard from './pages/Dashboard'
import FinanceLayout from './pages/finance/FinanceLayout'
import FinanceDashboard from './pages/finance/FinanceDashboard'
import HRLayout from './pages/hr/HRLayout'
import HRDashboard from './pages/hr/HRDashboard'
import Employees from './pages/hr/Employees'
import Departments from './pages/hr/Departments'
import Positions from './pages/hr/Positions'
import Locations from './pages/hr/Locations'
import Attendance from './pages/hr/Attendance'
import Leaves from './pages/hr/Leaves'
import Holidays from './pages/hr/Holidays'
import Salary from './pages/hr/Salary'
import Payroll from './pages/hr/Payroll'
import Deductions from './pages/hr/Deductions'
import Reviews from './pages/hr/Reviews'
import Goals from './pages/hr/Goals'
import HRUserRoles from './pages/hr/HRUserRoles'
import FinanceInvoiceList from './pages/finance/InvoiceList'
import Companies from './pages/finance/Companies'
import ChartOfAccounts from './pages/finance/ChartOfAccounts'
import CostCenters from './pages/finance/CostCenters'
import JournalEntries from './pages/finance/JournalEntries'
import JournalEntryLines from './pages/finance/JournalEntryLines'
import Customers from './pages/finance/Customers'
import CustomerPayments from './pages/finance/CustomerPayments'
import ARAging from './pages/finance/ARAging'
import Suppliers from './pages/finance/Suppliers'
import Bills from './pages/finance/Bills'
import BillLines from './pages/finance/BillLines'
import SupplierPayments from './pages/finance/SupplierPayments'
import BankAccounts from './pages/finance/BankAccounts'
import BankTransactions from './pages/finance/BankTransactions'
import TrialBalance from './pages/finance/TrialBalance'
import IncomeStatement from './pages/finance/IncomeStatement'
import BalanceSheet from './pages/finance/BalanceSheet'
import CashFlow from './pages/finance/CashFlow'
import APAging from './pages/finance/APAging'
import FinanceUserRoles from './pages/finance/FinanceUserRoles'
import './App.css'

function App() {
  const [user, setUser] = useState(null)

  return (
    <BrowserRouter>
      <div className="app-shell">
        {/* Navigation */}
        <nav className="topbar">
          <div className="topbar-inner">
            <div className="brand">
              <h1 className="brand-title">NEXORA</h1>
            </div>
            <div className="nav-links">
              <Link to="/" className="nav-link">
                Home
              </Link>
              <Link to="/hr" className="nav-link">
                HR
              </Link>
              <Link to="/finance" className="nav-link">
                Finance
              </Link>
            </div>
            <div className="user-pill">
              <span className="user-dot" />
              {user ? user : 'Guest User'}
            </div>
          </div>
        </nav>

        {/* Page Content */}
        <main className="page-frame">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/hr" element={<HRLayout />}>
              <Route index element={<HRDashboard />} />
              <Route path="employees" element={<Employees />} />
              <Route path="departments" element={<Departments />} />
              <Route path="positions" element={<Positions />} />
              <Route path="locations" element={<Locations />} />
              <Route path="attendance" element={<Attendance />} />
              <Route path="leaves" element={<Leaves />} />
              <Route path="holidays" element={<Holidays />} />
              <Route path="salary" element={<Salary />} />
              <Route path="payroll" element={<Payroll />} />
              <Route path="deductions" element={<Deductions />} />
              <Route path="reviews" element={<Reviews />} />
              <Route path="goals" element={<Goals />} />
              <Route path="user-roles" element={<HRUserRoles />} />
            </Route>
            <Route path="/finance" element={<FinanceLayout />}>
              <Route index element={<FinanceDashboard />} />
              <Route path="companies" element={<Companies />} />
              <Route path="chart-of-accounts" element={<ChartOfAccounts />} />
              <Route path="cost-centers" element={<CostCenters />} />
              <Route path="journal-entries" element={<JournalEntries />} />
              <Route path="journal-entry-lines" element={<JournalEntryLines />} />
              <Route path="customers" element={<Customers />} />
              <Route path="invoices" element={<FinanceInvoiceList />} />
              <Route path="customer-payments" element={<CustomerPayments />} />
              <Route path="ar-aging" element={<ARAging />} />
              <Route path="suppliers" element={<Suppliers />} />
              <Route path="bills" element={<Bills />} />
              <Route path="bill-lines" element={<BillLines />} />
              <Route path="supplier-payments" element={<SupplierPayments />} />
              <Route path="ap-aging" element={<APAging />} />
              <Route path="bank-accounts" element={<BankAccounts />} />
              <Route path="bank-transactions" element={<BankTransactions />} />
              <Route path="trial-balance" element={<TrialBalance />} />
              <Route path="income-statement" element={<IncomeStatement />} />
              <Route path="balance-sheet" element={<BalanceSheet />} />
              <Route path="cash-flow" element={<CashFlow />} />
              <Route path="user-roles" element={<FinanceUserRoles />} />
            </Route>
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  )
}

export default App
