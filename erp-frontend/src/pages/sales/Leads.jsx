import { useEffect, useState } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { formatMoney, formatDate } from '../finance/financeUtils'
import { salesLeadService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'

const statusOptions = ['NEW', 'QUALIFIED', 'IN_PROGRESS', 'CONVERTED', 'LOST']

export default function Leads() {
  const [status, setStatus] = useState('NEW')
  const [service, setService] = useState({ getAll: () => salesLeadService.getByStatus(getCompanyId(), 'NEW') })

  useEffect(() => {
    const companyId = getCompanyId()
    setService({
      getAll: () => salesLeadService.getByStatus(companyId, status),
    })
  }, [status])

  const columns = [
    {
      header: 'Lead',
      accessor: (item) => `${item.firstName || ''} ${item.lastName || ''}`.trim() || item.companyName,
    },
    { header: 'Company', accessor: 'companyName' },
    { header: 'Email', accessor: 'email' },
    { header: 'Status', accessor: 'status' },
    { header: 'Value', accessor: (item) => formatMoney(item.estimatedValue) },
    { header: 'Assigned', accessor: 'assignedTo' },
    { header: 'Created', accessor: (item) => formatDate(item.createdAt) },
  ]

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Leads</h2>
          <p className="finance-subtitle">Track lead intake across the pipeline.</p>
        </div>
        <div className="finance-filter">
          <label className="finance-meta" htmlFor="lead-status">Status</label>
          <select
            id="lead-status"
            className="finance-select"
            value={status}
            onChange={(event) => setStatus(event.target.value)}
          >
            {statusOptions.map((option) => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
      </div>

      <FinanceTablePage
        title="Leads"
        service={service}
        columns={columns}
        emptyMessage="No leads found for this status."
        itemName="Lead"
      />
    </div>
  )
}
