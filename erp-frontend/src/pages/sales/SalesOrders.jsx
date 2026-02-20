import { useEffect, useState } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { formatMoney, formatDate } from '../finance/financeUtils'
import { salesOrderService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'

const statusOptions = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'INVOICED', 'CANCELLED']

export default function SalesOrders() {
  const [status, setStatus] = useState('PENDING')
  const [service, setService] = useState({ getAll: () => salesOrderService.getByStatus(getCompanyId(), 'PENDING') })

  useEffect(() => {
    const companyId = getCompanyId()
    setService({
      getAll: () => salesOrderService.getByStatus(companyId, status),
    })
  }, [status])

  const columns = [
    { header: 'Order #', accessor: 'orderNumber' },
    { header: 'Customer', accessor: 'customerName' },
    { header: 'Status', accessor: 'status' },
    { header: 'Payment', accessor: 'paymentStatus' },
    { header: 'Total', accessor: (item) => formatMoney(item.totalAmount) },
    { header: 'Order Date', accessor: (item) => formatDate(item.orderDate) },
  ]

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Sales Orders</h2>
          <p className="finance-subtitle">Fulfillment milestones and payment state.</p>
        </div>
        <div className="finance-filter">
          <label className="finance-meta" htmlFor="order-status">Status</label>
          <select
            id="order-status"
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
        title="Sales Orders"
        service={service}
        columns={columns}
        emptyMessage="No sales orders found for this status."
        itemName="Sales Order"
      />
    </div>
  )
}
