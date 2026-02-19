import FinanceTablePage from './FinanceTablePage'
import { invoiceLineService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddInvoiceLineForm from '../../components/forms/AddInvoiceLineForm'

const columns = [
  {
    header: 'Invoice',
    accessor: (item) => pickFirst(item, ['invoiceId', 'invoice']),
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

export default function InvoiceLines() {
  return (
    <FinanceTablePage
      title="Invoice Line Items"
      service={invoiceLineService}
      columns={columns}
      emptyMessage="No invoice lines found."
      FormComponent={AddInvoiceLineForm}
      itemName="Invoice Line"
    />
  )
}
