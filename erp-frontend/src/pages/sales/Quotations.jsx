import { useEffect, useState } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { formatMoney, formatDate } from '../finance/financeUtils'
import { salesQuotationService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'

const statusOptions = ['DRAFT', 'SENT', 'ACCEPTED', 'REJECTED', 'CONVERTED_TO_ORDER', 'EXPIRED']

export default function Quotations() {
  const [status, setStatus] = useState('DRAFT')
  const [service, setService] = useState({ getAll: () => salesQuotationService.getByStatus(getCompanyId(), 'DRAFT') })

  useEffect(() => {
    const companyId = getCompanyId()
    setService({
      getAll: () => salesQuotationService.getByStatus(companyId, status),
    })
  }, [status])

  const columns = [
    { header: 'Quote #', accessor: 'quoteNumber' },
    { header: 'Status', accessor: 'status' },
    { header: 'Total', accessor: (item) => formatMoney(item.totalAmount) },
    { header: 'Quote Date', accessor: (item) => formatDate(item.quoteDate) },
    { header: 'Expiry', accessor: (item) => formatDate(item.expiryDate) },
    { header: 'Approved By', accessor: 'approvedBy' },
  ]

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Quotations</h2>
          <p className="finance-subtitle">Quote tracking and conversions.</p>
        </div>
        <div className="finance-filter">
          <label className="finance-meta" htmlFor="quotation-status">Status</label>
          <select
            id="quotation-status"
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
        title="Quotations"
        service={service}
        columns={columns}
        emptyMessage="No quotations found for this status."
        itemName="Quotation"
      />
    </div>
  )
}
