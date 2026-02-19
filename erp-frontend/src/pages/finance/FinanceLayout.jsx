import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'

const navSections = [
  {
    title: 'Overview',
    links: [
      { to: '/finance', label: 'Dashboard' },
    ],
  },
  {
    title: 'Accounting',
    links: [
      { to: '/finance/companies', label: 'Companies' },
      { to: '/finance/chart-of-accounts', label: 'Chart of Accounts' },
      { to: '/finance/cost-centers', label: 'Cost Centers' },
      { to: '/finance/journal-entries', label: 'Journal Entries' },
      { to: '/finance/journal-entry-lines', label: 'Journal Entry Lines' },
    ],
  },
  {
    title: 'Receivables',
    links: [
      { to: '/finance/customers', label: 'Customers' },
      { to: '/finance/invoices', label: 'Invoices' },
      { to: '/finance/customer-payments', label: 'Customer Payments' },
      { to: '/finance/ar-aging', label: 'AR Aging' },
    ],
  },
  {
    title: 'Payables',
    links: [
      { to: '/finance/suppliers', label: 'Suppliers' },
      { to: '/finance/bills', label: 'Bills' },
      { to: '/finance/bill-lines', label: 'Bill Line Items' },
      { to: '/finance/supplier-payments', label: 'Supplier Payments' },
      { to: '/finance/ap-aging', label: 'AP Aging' },
    ],
  },
  {
    title: 'Banking',
    links: [
      { to: '/finance/bank-accounts', label: 'Bank Accounts' },
      { to: '/finance/bank-transactions', label: 'Bank Transactions' },
    ],
  },
  {
    title: 'Reporting',
    links: [
      { to: '/finance/trial-balance', label: 'Trial Balance' },
      { to: '/finance/income-statement', label: 'Income Statement' },
      { to: '/finance/balance-sheet', label: 'Balance Sheet' },
      { to: '/finance/cash-flow', label: 'Cash Flow Statement' },
    ],
  },
  {
    title: 'Admin',
    links: [
      { to: '/finance/user-roles', label: 'Finance Roles' },
    ],
  },
]

export default function FinanceLayout() {
  const [collapsed, setCollapsed] = useState(false)

  return (
    <div className={`finance-layout ${collapsed ? 'sidebar-collapsed' : ''}`}>
      <aside className={`finance-sidebar ${collapsed ? 'collapsed' : ''}`}>
        <div className="finance-sidebar-header">
          <div>
            <h2 className="finance-title">Finance</h2>
            {!collapsed && (
              <p className="finance-subtitle">Move fast between ledgers, AR, and AP.</p>
            )}
          </div>
          <button
            type="button"
            className="finance-sidebar-toggle"
            onClick={() => setCollapsed((prev) => !prev)}
            aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
          >
            {collapsed ? '>' : '<'}
          </button>
        </div>

        <div className="finance-nav">
          {navSections.map((section) => (
            <div key={section.title} className="finance-nav-section">
              <p className="finance-nav-title">{section.title}</p>
              <div className="finance-nav-links">
                {section.links.map((link) => (
                  <NavLink
                    key={link.to}
                    to={link.to}
                    end
                    className={({ isActive }) =>
                      `finance-nav-link ${isActive ? 'finance-nav-link-active' : ''}`
                    }
                  >
                    {link.label}
                  </NavLink>
                ))}
              </div>
            </div>
          ))}
        </div>
      </aside>

      <section className="finance-content">
        <Outlet />
      </section>
    </div>
  )
}
