import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'

const navSections = [
  {
    title: 'Overview',
    links: [
      { to: '/hr', label: 'Dashboard' },
    ],
  },
  {
    title: 'Organization',
    links: [
      { to: '/hr/employees', label: 'Employees' },
      { to: '/hr/departments', label: 'Departments' },
      { to: '/hr/positions', label: 'Positions' },
      { to: '/hr/locations', label: 'Locations' },
    ],
  },
  {
    title: 'Attendance',
    links: [
      { to: '/hr/attendance', label: 'Attendance' },
      { to: '/hr/leaves', label: 'Leaves' },
      { to: '/hr/holidays', label: 'Holidays' },
    ],
  },
  {
    title: 'Payroll',
    links: [
      { to: '/hr/salary', label: 'Salary' },
      { to: '/hr/payroll', label: 'Payroll' },
      { to: '/hr/deductions', label: 'Deductions' },
    ],
  },
  {
    title: 'Performance',
    links: [
      { to: '/hr/reviews', label: 'Reviews' },
      { to: '/hr/goals', label: 'Goals' },
    ],
  },
  {
    title: 'Admin',
    links: [
      { to: '/hr/user-roles', label: 'HR Roles' },
    ],
  },
]

export default function HRLayout() {
  const [collapsed, setCollapsed] = useState(false)

  return (
    <div className={`finance-layout ${collapsed ? 'sidebar-collapsed' : ''}`}>
      <aside className={`finance-sidebar ${collapsed ? 'collapsed' : ''}`}>
        <div className="finance-sidebar-header">
          <div>
            <h2 className="finance-title">Human Resources</h2>
            {!collapsed && (
              <p className="finance-subtitle">Manage employees and payroll.</p>
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
