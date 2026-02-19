import FinanceTablePage from './FinanceTablePage'
import { arAgingService } from '../../services/api'
import { formatMoney, pickFirstNumber } from './financeUtils'
import AddARAagingForm from '../../components/forms/AddARAagingForm'

const columns = [
  {
    header: 'Customer',
    accessor: (item) => item?.customer?.name || item?.customerName || 'Unknown',
  },
  {
    header: 'Current',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['current', 'currentAmount'])),
  },
  {
    header: '30 Days',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['bucket30', 'days30'])),
  },
  {
    header: '60 Days',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['bucket60', 'days60'])),
  },
  {
    header: '90+ Days',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['bucket90', 'days90'])),
  },
  {
    header: 'Total',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['totalBalance', 'balance'])),
  },
]

export default function ARAging() {
  return (
    <FinanceTablePage
      title="AR Aging"
      service={arAgingService}
      columns={columns}
      emptyMessage="No AR aging records found."
      FormComponent={AddARAagingForm}
      itemName="AR Aging"
    />
  )
}
