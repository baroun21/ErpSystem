import { useEffect, useState } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { formatDate } from '../finance/financeUtils'
import { salesRiskScoreService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'

const ratingOptions = ['LOW', 'MEDIUM', 'HIGH']

export default function CustomerRiskScores() {
  const [rating, setRating] = useState('LOW')
  const [service, setService] = useState({ getAll: () => salesRiskScoreService.getByRating(getCompanyId(), 'LOW') })

  useEffect(() => {
    const companyId = getCompanyId()
    setService({
      getAll: () => salesRiskScoreService.getByRating(companyId, rating),
    })
  }, [rating])

  const columns = [
    { header: 'Customer', accessor: 'customerName' },
    { header: 'Risk Rating', accessor: 'riskRating' },
    { header: 'Risk Score', accessor: 'riskScore' },
    { header: 'Exposure', accessor: 'exposure' },
    { header: 'Last Updated', accessor: (item) => formatDate(item.lastUpdated) },
  ]

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Customer Risk Scores</h2>
          <p className="finance-subtitle">Monitor risk exposure across key accounts.</p>
        </div>
        <div className="finance-filter">
          <label className="finance-meta" htmlFor="risk-rating">Risk Rating</label>
          <select
            id="risk-rating"
            className="finance-select"
            value={rating}
            onChange={(event) => setRating(event.target.value)}
          >
            {ratingOptions.map((option) => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
      </div>

      <FinanceTablePage
        title="Customer Risk Scores"
        service={service}
        columns={columns}
        emptyMessage="No risk scores found for this rating."
        itemName="Risk Score"
      />
    </div>
  )
}
