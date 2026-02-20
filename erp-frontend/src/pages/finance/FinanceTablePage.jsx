import { useEffect, useState } from 'react'
import { normalizeList } from './financeUtils'

const renderCellValue = (item, column) => {
  if (column.render) {
    return column.render(item)
  }

  if (typeof column.accessor === 'function') {
    return column.accessor(item)
  }

  return item?.[column.accessor]
}

const formatValue = (value) => {
  if (value === undefined || value === null || value === '') {
    return '-'
  }

  if (typeof value === 'boolean') {
    return value ? 'Active' : 'Inactive'
  }

  return value
}

export default function FinanceTablePage({
  title,
  service,
  columns,
  emptyMessage,
  loadingMessage,
  errorMessage,
  FormComponent,
  itemName,
}) {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)

  const handleFormSuccess = async () => {
    const response = await service.getAll()
    setItems(normalizeList(response.data || []))
    setShowForm(false)
  }

  useEffect(() => {
    let isMounted = true

    const loadData = async () => {
      try {
        setLoading(true)
        const response = await service.getAll()
        const list = normalizeList(response.data)

        if (isMounted) {
          setItems(Array.isArray(list) ? list : [])
          setError(null)
        }
      } catch (err) {
        if (isMounted) {
          setItems([])
          setError(errorMessage || 'Unable to load finance data. Make sure Spring Boot is running on port 8081.')
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
  }, [service, errorMessage, showForm])

  return (
    <>
      <div className="finance-page">
          <div className="finance-page-header">
          <div className="table-header-flex">
            <span className="finance-title-wrapper"><h2 className="finance-title">{title}</h2><p className="finance-subtitle">Finance module data from Spring Boot.</p></span>
            {FormComponent && (
              <button className="finance-btn-primary" onClick={() => setShowForm(true)}>+ Add {itemName || 'Item'}</button>
            )}
          </div>
          <div className="finance-meta">{items.length} records</div>
        </div>

        {error && (
          <div className="finance-alert">
            {error}
          </div>
        )}

        {loading ? (
          <div className="finance-card finance-card-muted">
            {loadingMessage || 'Loading records...'}
          </div>
        ) : items.length === 0 ? (
          <div className="finance-card finance-card-muted">
            {emptyMessage || 'No records found.'}
          </div>
        ) : (
          <div className="finance-table-card">
            <table className="finance-table">
              <thead>
                <tr>
                  {columns.map((column) => (
                    <th key={column.header}>{column.header}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {items.map((item, index) => (
                  <tr key={item?.id || item?.uuid || index}>
                    {columns.map((column) => (
                      <td key={column.header}>{formatValue(renderCellValue(item, column))}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
      {FormComponent && <FormComponent isOpen={showForm} onClose={() => setShowForm(false)} onSuccess={handleFormSuccess} />}
    </>
  )
}
