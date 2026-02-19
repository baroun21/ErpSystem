import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { billService, customerService, invoiceService, supplierService } from '../../services/api'

const emptyStats = {
  invoices: 0,
  bills: 0,
  customers: 0,
  suppliers: 0,
}

const normalizeList = (payload) => {
  if (!payload || Array.isArray(payload)) {
    return payload || []
  }
  return payload.content || payload.data || payload.results || payload.items || []
}

const formatMoney = (value) => {
  const amount = Number.parseFloat(value || 0)
  if (Number.isNaN(amount)) {
    return '$0.00'
  }
  return `$${amount.toFixed(2)}`
}

export default function FinanceDashboard() {
  const [stats, setStats] = useState(emptyStats)
  const [recentInvoices, setRecentInvoices] = useState([])
  const [recentBills, setRecentBills] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let isMounted = true

    const loadData = async () => {
      try {
        setLoading(true)
        const [invoicesRes, billsRes, customersRes, suppliersRes] = await Promise.all([
          invoiceService.getAll(),
          billService.getAll(),
          customerService.getAll(),
          supplierService.getAll(),
        ])

        const invoices = normalizeList(invoicesRes.data)
        const bills = normalizeList(billsRes.data)
        const customers = normalizeList(customersRes.data)
        const suppliers = normalizeList(suppliersRes.data)

        if (!isMounted) {
          return
        }

        setStats({
          invoices: invoices.length,
          bills: bills.length,
          customers: customers.length,
          suppliers: suppliers.length,
        })

        const sortedInvoices = [...invoices].sort((a, b) => {
          const aDate = a.issueDate ? new Date(a.issueDate).getTime() : 0
          const bDate = b.issueDate ? new Date(b.issueDate).getTime() : 0
          return bDate - aDate
        })

        const sortedBills = [...bills].sort((a, b) => {
          const aDate = a.issueDate ? new Date(a.issueDate).getTime() : 0
          const bDate = b.issueDate ? new Date(b.issueDate).getTime() : 0
          return bDate - aDate
        })

        setRecentInvoices(sortedInvoices.slice(0, 5))
        setRecentBills(sortedBills.slice(0, 5))
        setError(null)
      } catch (err) {
        if (isMounted) {
          setError('Unable to load finance data. Make sure Spring Boot is running on port 8081.')
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
          <h2 className="finance-title">Finance Overview</h2>
          <p className="finance-subtitle">Track invoices, payables, and ledger activity.</p>
        </div>
        <div className="finance-header-actions">
          <Link to="/finance/invoices" className="finance-cta">
            View Invoices
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
          <p>Total Invoices</p>
          <h3>{stats.invoices}</h3>
        </div>
        <div className="finance-stat-card">
          <p>Total Bills</p>
          <h3>{stats.bills}</h3>
        </div>
        <div className="finance-stat-card">
          <p>Customers</p>
          <h3>{stats.customers}</h3>
        </div>
        <div className="finance-stat-card">
          <p>Suppliers</p>
          <h3>{stats.suppliers}</h3>
        </div>
      </div>

      <div className="finance-grid">
        <div className="finance-card">
          <div className="finance-card-header">
            <h3>Recent Invoices</h3>
          </div>
          <div className="finance-card-body">
            {loading ? (
              <p className="finance-muted">Loading invoices...</p>
            ) : recentInvoices.length === 0 ? (
              <p className="finance-muted">No invoices yet.</p>
            ) : (
              <ul className="finance-list">
                {recentInvoices.map((invoice) => (
                  <li key={invoice.id} className="finance-list-item">
                    <div>
                      <p className="finance-list-title">{invoice.invoiceNumber || 'Invoice'}</p>
                      <p className="finance-list-meta">
                        {invoice.customer?.name || 'Unknown customer'} · {invoice.issueDate || 'No date'}
                      </p>
                    </div>
                    <span className="finance-list-value">
                      {formatMoney(invoice.totalAmount)}
                    </span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        <div className="finance-card">
          <div className="finance-card-header">
            <h3>Recent Bills</h3>
          </div>
          <div className="finance-card-body">
            {loading ? (
              <p className="finance-muted">Loading bills...</p>
            ) : recentBills.length === 0 ? (
              <p className="finance-muted">No bills yet.</p>
            ) : (
              <ul className="finance-list">
                {recentBills.map((bill) => (
                  <li key={bill.id} className="finance-list-item">
                    <div>
                      <p className="finance-list-title">{bill.billNumber || 'Bill'}</p>
                      <p className="finance-list-meta">
                        {bill.supplier?.name || 'Unknown supplier'} · {bill.issueDate || 'No date'}
                      </p>
                    </div>
                    <span className="finance-list-value">
                      {formatMoney(bill.totalAmount)}
                    </span>
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
