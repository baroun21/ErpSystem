import FinanceTablePage from './FinanceTablePage'
import { invoiceService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddInvoiceForm from '../../components/forms/AddInvoiceForm'

const columns = [
  {
    header: 'Invoice #',
    accessor: (item) => pickFirst(item, ['invoiceNumber', 'reference', 'number']),
  },
  {
    header: 'Customer',
    accessor: (item) => item?.customer?.name || pickFirst(item, ['customerName', 'customer']),
  },
  {
    header: 'Amount',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['totalAmount', 'amount', 'invoiceAmount'])),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state', 'invoiceStatus']),
  },
  {
    header: 'Due Date',
    accessor: (item) => formatDate(pickFirst(item, ['dueDate', 'paymentDueDate', 'invoiceDueDate'])),
  },
]

export default function Invoices() {
  return (
    <FinanceTablePage
      title="Invoices"
      service={invoiceService}
      columns={columns}
      emptyMessage="No invoices found."
      FormComponent={AddInvoiceForm}
      itemName="Invoice"
    />
  )
}
