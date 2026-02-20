import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'

const navSections = [
  {
    title: 'Automation',
    links: [
      { to: '/automation', label: 'Rules' },
      { to: '/automation/logs', label: 'Execution Logs' },
    ],
  },
]

export default function AutomationLayout() {
  const [collapsed, setCollapsed] = useState(false)

  return (
    <div className={`finance-layout ${collapsed ? 'sidebar-collapsed' : ''}`}>
      <aside className={`finance-sidebar ${collapsed ? 'collapsed' : ''}`}>
        <div className="finance-sidebar-header">
          <div>
            <h2 className="finance-title">Automation</h2>
            {!collapsed && (
              <p className="finance-subtitle">Trigger actions on events or schedules.</p>
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
