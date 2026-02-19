import FinanceTablePage from './FinanceTablePage'
import { supplierService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddSupplierForm from '../../components/forms/AddSupplierForm'

const columns = [
  {
    header: 'Supplier',
    accessor: (item) => pickFirst(item, ['name', 'supplierName', 'legalName']),
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
    header: 'AP Balance',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['apBalance', 'balance', 'currentBalance'])),
  },
]

export default function Suppliers() {
  return (
    <FinanceTablePage
      title="Suppliers"
      service={supplierService}
      columns={columns}
      emptyMessage="No suppliers found."
      FormComponent={AddSupplierForm}
      itemName="Supplier"
    />
  )
}
