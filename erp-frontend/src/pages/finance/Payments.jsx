import FinanceTablePage from './FinanceTablePage'
import { paymentService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddPaymentForm from '../../components/forms/AddPaymentForm'

const columns = [
  {
    header: 'Payment Number',
    accessor: (item) => pickFirst(item, ['paymentNumber', 'reference']),
  },
  {
    header: 'Type',
    accessor: (item) => pickFirst(item, ['paymentType', 'type']),
  },
  {
    header: 'Amount',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['amount', 'total'])),
  },
  {
    header: 'Currency',
    accessor: (item) => pickFirst(item, ['currency', 'currencyCode']),
  },
  {
    header: 'Payment Date',
    accessor: (item) => formatDate(pickFirst(item, ['paymentDate', 'date'])),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state']),
  },
]

export default function Payments() {
  return (
    <FinanceTablePage
      title="Payments"
      service={paymentService}
      columns={columns}
      emptyMessage="No payments found."
      FormComponent={AddPaymentForm}
      itemName="Payment"
    />
  )
}
