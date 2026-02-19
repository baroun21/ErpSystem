import FinanceTablePage from './FinanceTablePage'
import { customerPaymentService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddCustomerPaymentForm from '../../components/forms/AddCustomerPaymentForm'

const columns = [
  {
    header: 'Payment #',
    accessor: (item) => pickFirst(item, ['paymentNumber', 'reference', 'receiptNumber']),
  },
  {
    header: 'Customer',
    accessor: (item) => item?.customer?.name || pickFirst(item, ['customerName', 'customer']),
  },
  {
    header: 'Date',
    accessor: (item) => formatDate(pickFirst(item, ['paymentDate', 'date', 'receivedDate'])),
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

export default function CustomerPayments() {
  return (
    <FinanceTablePage
      title="Customer Payments"
      service={customerPaymentService}
      columns={columns}
      emptyMessage="No customer payments found."
      FormComponent={AddCustomerPaymentForm}
      itemName="Payment"
    />
  )
}
