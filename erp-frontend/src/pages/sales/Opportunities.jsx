import { useEffect, useState } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { formatMoney, formatDate } from '../finance/financeUtils'
import { salesOpportunityService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'

const statusOptions = ['OPEN', 'WON', 'LOST', 'CLOSED']

export default function Opportunities() {
  const [status, setStatus] = useState('OPEN')
  const [service, setService] = useState({ getAll: () => salesOpportunityService.getByStatus(getCompanyId(), 'OPEN') })

  useEffect(() => {
    const companyId = getCompanyId()
    setService({
      getAll: () => salesOpportunityService.getByStatus(companyId, status),
    })
  }, [status])

  const columns = [
    { header: 'Opportunity', accessor: 'opportunityName' },
    { header: 'Status', accessor: 'status' },
    { header: 'Value', accessor: (item) => formatMoney(item.opportunityValue) },
    { header: 'Probability', accessor: (item) => item.probabilityPercentage ?? '-' },
    { header: 'Expected Close', accessor: (item) => formatDate(item.expectedCloseDate) },
    { header: 'Assigned', accessor: 'assignedTo' },
  ]

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Opportunities</h2>
          <p className="finance-subtitle">Pipeline visibility for active deals.</p>
        </div>
        <div className="finance-filter">
          <label className="finance-meta" htmlFor="opportunity-status">Status</label>
          <select
            id="opportunity-status"
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
        title="Opportunities"
        service={service}
        columns={columns}
        emptyMessage="No opportunities found for this status."
        itemName="Opportunity"
      />
    </div>
  )
}
