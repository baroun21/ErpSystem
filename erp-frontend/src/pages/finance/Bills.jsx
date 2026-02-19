import FinanceTablePage from './FinanceTablePage'
import { billService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddBillForm from '../../components/forms/AddBillForm'

const columns = [
  {
    header: 'Bill #',
    accessor: (item) => pickFirst(item, ['billNumber', 'reference']),
  },
  {
    header: 'Supplier',
    accessor: (item) => item?.supplier?.name || pickFirst(item, ['supplierName', 'supplier']),
  },
  {
    header: 'Amount',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['totalAmount', 'amount'])),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state']),
  },
  {
    header: 'Due Date',
    accessor: (item) => formatDate(pickFirst(item, ['dueDate', 'paymentDueDate'])),
  },
]

export default function Bills() {
  return (
    <FinanceTablePage
      title="Bills"
      service={billService}
      columns={columns}
      emptyMessage="No bills found."
      FormComponent={AddBillForm}
      itemName="Bill"
    />
  )
}
