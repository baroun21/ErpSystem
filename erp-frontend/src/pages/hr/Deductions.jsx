import { useEffect, useState } from 'react'
import { deductionService } from '../../services/api'
import AddDeductionForm from '../../components/forms/AddDeductionForm'

const FinanceTablePage = ({ title, subtitle, service, columns, emptyMessage, FormComponent }) => {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    let isMounted = true
    const loadData = async () => {
      try {
        setLoading(true)
        const response = await service.getAll()
        const payload = response.data || []
        const data = Array.isArray(payload) ? payload : payload.content || payload.data || payload.results || payload.items || []
        if (isMounted) {
          setItems(data)
          setError(null)
        }
      } catch (err) {
        if (isMounted) setError(`Failed to load ${title}.`)
      } finally {
        if (isMounted) setLoading(false)
      }
    }
    loadData()
    return () => { isMounted = false }
  }, [service, title, showForm])

  const handleFormSuccess = async () => {
    try {
      const response = await service.getAll()
      const payload = response.data || []
      const data = Array.isArray(payload) ? payload : payload.content || payload.data || payload.results || payload.items || []
      setItems(data)
    } catch (err) {
      console.error('Error reloading data:', err)
    }
  }

  const formatValue = (value) => value === null || value === undefined ? '—' : typeof value === 'boolean' ? value ? 'Yes' : 'No' : value
  const renderCellValue = (item, column) => formatValue(typeof column.accessor === 'function' ? column.accessor(item) : item[column.accessor])

  return (
    <div className="finance-page">
      <div className="finance-page-header">
        <div>
          <h2 className="finance-title">{title}</h2>
          {subtitle && <p className="finance-subtitle">{subtitle}</p>}
        </div>
        <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
          <span className="finance-meta">{items.length} records</span>
          {FormComponent && (
            <button className="finance-btn-primary" onClick={() => setShowForm(true)}>
              + Add {title.slice(0, -1)}
            </button>
          )}
        </div>
      </div>
      {error && <div className="finance-alert">{error}</div>}
      <div className="finance-table-card">
        {loading ? (
          <div style={{ padding: '24px', textAlign: 'center', color: 'var(--ink-500)' }}>Loading {title}...</div>
        ) : items.length === 0 ? (
          <div style={{ padding: '24px', textAlign: 'center', color: 'var(--ink-500)' }}>{emptyMessage || `No ${title.toLowerCase()} found.`}</div>
        ) : (
          <table className="finance-table">
            <thead>
              <tr>{columns.map((col, idx) => <th key={idx}>{col.header}</th>)}</tr>
            </thead>
            <tbody>
              {items.map((item) => <tr key={item.id || Math.random()}>{columns.map((col, idx) => <td key={idx}>{renderCellValue(item, col)}</td>)}</tr>)}
            </tbody>
          </table>
        )}
      </div>
      {FormComponent && (
        <FormComponent isOpen={showForm} onClose={() => setShowForm(false)} onSuccess={handleFormSuccess} />
      )}
    </div>
  )
}

export default function Deductions() {
  const columns = [
    { header: 'Employee', accessor: (item) => item.employee?.name || 'N/A' },
    { header: 'Type', accessor: 'deductionType' },
    { header: 'Amount', accessor: 'amount' },
    { header: 'Frequency', accessor: 'frequency' },
  ]
  return <FinanceTablePage title="Deductions" subtitle="Salary deductions" service={deductionService} columns={columns} FormComponent={AddDeductionForm} emptyMessage="No deductions found." />
}