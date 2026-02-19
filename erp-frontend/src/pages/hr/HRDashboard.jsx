import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { employeeService, departmentService, leaveRequestService, salaryService } from '../../services/api'

const emptyStats = {
  employees: 0,
  departments: 0,
  onLeave: 0,
  avgSalary: 0,
}

const normalizeList = (payload) => {
  if (!payload || Array.isArray(payload)) {
    return payload || []
  }
  return payload.content || payload.data || payload.results || payload.items || payload.employees || payload.departments || []
}

const formatMoney = (value) => {
  const amount = Number.parseFloat(value || 0)
  if (Number.isNaN(amount)) {
    return '$0.00'
  }
  return `$${amount.toFixed(2)}`
}

export default function HRDashboard() {
  const [stats, setStats] = useState(emptyStats)
  const [recentEmployees, setRecentEmployees] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let isMounted = true

    const loadData = async () => {
      try {
        setLoading(true)
        const [empRes, deptRes, leaveRes, salaryRes] = await Promise.all([
          employeeService.getAll().catch(() => ({ data: [] })),
          departmentService.getAll().catch(() => ({ data: [] })),
          leaveRequestService.getAll().catch(() => ({ data: [] })),
          salaryService.getAll().catch(() => ({ data: [] })),
        ])

        const employees = normalizeList(empRes.data)
        const departments = normalizeList(deptRes.data)
        const leaves = normalizeList(leaveRes.data)
        const salaries = normalizeList(salaryRes.data)

        if (!isMounted) {
          return
        }

        const avgSalary = salaries.length > 0
          ? salaries.reduce((sum, s) => sum + (Number(s.amount) || 0), 0) / salaries.length
          : 0

        setStats({
          employees: employees.length,
          departments: departments.length,
          onLeave: leaves.length,
          avgSalary,
        })

        setRecentEmployees(employees.slice(0, 5))
        setError(null)
      } catch (err) {
        if (isMounted) {
          setError('Unable to load HR data. Make sure Spring Boot is running on port 8081.')
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    loadData()

    return () => {
      isMounted = false
    }
  }, [])

  return (
    <div className="finance-page">
      <div className="finance-page-header">
        <div>
          <h2 className="finance-title">HR Dashboard</h2>
          <p className="finance-subtitle">Overview of employees, departments, and payroll.</p>
        </div>
        <div className="finance-header-actions">
          <Link to="/hr/employees" className="finance-cta">
            View Employees
          </Link>
        </div>
      </div>

      {error && (
        <div className="finance-alert">
          {error}
        </div>
      )}

      <div className="finance-stat-grid">
        <div className="finance-stat-card">
          <p>Total Employees</p>
          <h3>{stats.employees}</h3>
        </div>
        <div className="finance-stat-card">
          <p>Departments</p>
          <h3>{stats.departments}</h3>
        </div>
        <div className="finance-stat-card">
          <p>On Leave</p>
          <h3>{stats.onLeave}</h3>
        </div>
        <div className="finance-stat-card">
          <p>Avg Salary</p>
          <h3>{formatMoney(stats.avgSalary)}</h3>
        </div>
      </div>

      <div className="finance-grid">
        <div className="finance-card">
          <div className="finance-card-header">
            <h3>Recent Employees</h3>
          </div>
          <div className="finance-card-body">
            {loading ? (
              <p className="finance-muted">Loading employees...</p>
            ) : recentEmployees.length === 0 ? (
              <p className="finance-muted">No employees yet.</p>
            ) : (
              <ul className="finance-list">
                {recentEmployees.map((emp) => (
                  <li key={emp.id} className="finance-list-item">
                    <div>
                      <p className="finance-list-title">{emp.name || emp.firstName || 'Employee'}</p>
                      <p className="finance-list-meta">
                        {emp.department?.name || emp.position?.name || 'No department'} · {emp.joinDate || 'No date'}
                      </p>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
