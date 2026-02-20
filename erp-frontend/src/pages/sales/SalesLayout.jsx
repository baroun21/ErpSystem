import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'

const navSections = [
  {
    title: 'Overview',
    links: [
      { to: '/sales', label: 'Dashboard' },
      { to: '/sales/analytics', label: 'Analytics' },
    ],
  },
  {
    title: 'CRM',
    links: [
      { to: '/sales/leads', label: 'Leads' },
      { to: '/sales/opportunities', label: 'Opportunities' },
      { to: '/sales/quotations', label: 'Quotations' },
    ],
  },
  {
    title: 'Orders',
    links: [
      { to: '/sales/orders', label: 'Sales Orders' },
    ],
  },
  {
    title: 'Risk',
    links: [
      { to: '/sales/risk-scores', label: 'Customer Risk' },
    ],
  },
]

export default function SalesLayout() {
  const [collapsed, setCollapsed] = useState(false)

  return (
    <div className={`finance-layout ${collapsed ? 'sidebar-collapsed' : ''}`}>
      <aside className={`finance-sidebar ${collapsed ? 'collapsed' : ''}`}>
        <div className="finance-sidebar-header">
          <div>
            <h2 className="finance-title">Sales</h2>
            {!collapsed && (
              <p className="finance-subtitle">Track pipeline, quotes, and revenue momentum.</p>
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
