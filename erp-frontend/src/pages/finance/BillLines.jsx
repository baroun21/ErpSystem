import FinanceTablePage from './FinanceTablePage'
import { billLineService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddBillLineForm from '../../components/forms/AddBillLineForm'

const columns = [
  {
    header: 'Bill',
    accessor: (item) => pickFirst(item, ['billId', 'bill']),
  },
  {
    header: 'Line #',
    accessor: (item) => pickFirstNumber(item, ['lineNumber', 'line']),
  },
  {
    header: 'Description',
    accessor: (item) => pickFirst(item, ['description', 'memo']),
  },
  {
    header: 'Quantity',
    accessor: (item) => Math.round(pickFirstNumber(item, ['quantity']) * 10000) / 10000,
  },
  {
    header: 'Unit Price',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['unitPrice', 'price'])),
  },
  {
    header: 'Tax',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['taxAmount', 'tax'])),
  },
  {
    header: 'Line Total',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['lineTotal', 'total'])),
  },
  {
    header: 'Cost Center',
    accessor: (item) => pickFirst(item, ['costCenterId', 'costCenter']),
  },
]

export default function BillLines() {
  return (
    <FinanceTablePage
      title="Bill Line Items"
      service={billLineService}
      columns={columns}
      emptyMessage="No bill lines found."
      FormComponent={AddBillLineForm}
      itemName="Bill Line"
    />
  )
}
