import FinanceTablePage from './FinanceTablePage'
import { supplierPaymentService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddSupplierPaymentForm from '../../components/forms/AddSupplierPaymentForm'

const columns = [
  {
    header: 'Payment #',
    accessor: (item) => pickFirst(item, ['paymentNumber', 'reference', 'receiptNumber']),
  },
  {
    header: 'Supplier',
    accessor: (item) => item?.supplier?.name || pickFirst(item, ['supplierName', 'supplier']),
  },
  {
    header: 'Date',
    accessor: (item) => formatDate(pickFirst(item, ['paymentDate', 'date'])),
  },
  {
    header: 'Amount',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['amount', 'paymentAmount'])),
  },
  {
    header: 'Method',
    accessor: (item) => pickFirst(item, ['method', 'paymentMethod']),
  },
]

export default function SupplierPayments() {
  return (
    <FinanceTablePage
      title="Supplier Payments"
      service={supplierPaymentService}
      columns={columns}
      emptyMessage="No supplier payments found."
      FormComponent={AddSupplierPaymentForm}
      itemName="Payment"
    />
  )
}
