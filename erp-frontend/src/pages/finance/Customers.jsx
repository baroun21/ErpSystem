import FinanceTablePage from './FinanceTablePage'
import { customerService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddCustomerForm from '../../components/forms/AddCustomerForm'

const columns = [
  {
    header: 'Customer',
    accessor: (item) => pickFirst(item, ['name', 'customerName', 'legalName']),
  },
  {
    header: 'Email',
    accessor: (item) => pickFirst(item, ['email', 'contactEmail']),
  },
  {
    header: 'Phone',
    accessor: (item) => pickFirst(item, ['phone', 'phoneNumber', 'contactPhone']),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state', 'active']),
  },
  {
    header: 'AR Balance',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['arBalance', 'balance', 'currentBalance'])),
  },
]

export default function Customers() {
  return (
    <FinanceTablePage
      title="Customers"
      service={customerService}
      columns={columns}
      emptyMessage="No customers found."
      FormComponent={AddCustomerForm}
      itemName="Customer"
    />
  )
}
