import FinanceTablePage from './FinanceTablePage'
import { chartOfAccountsService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddChartOfAccountsForm from '../../components/forms/AddChartOfAccountsForm'

const columns = [
  {
    header: 'Code',
    accessor: (item) => pickFirst(item, ['code', 'accountCode']),
  },
  {
    header: 'Name',
    accessor: (item) => pickFirst(item, ['name', 'accountName']),
  },
  {
    header: 'Type',
    accessor: (item) => pickFirst(item, ['type', 'accountType']),
  },
  {
    header: 'Category',
    accessor: (item) => pickFirst(item, ['category', 'accountCategory']),
  },
  {
    header: 'Balance',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['balance', 'currentBalance'])),
  },
]

export default function ChartOfAccounts() {
  return (
    <FinanceTablePage
      title="Chart of Accounts"
      service={chartOfAccountsService}
      columns={columns}
      emptyMessage="No accounts found."
      FormComponent={AddChartOfAccountsForm}
      itemName="Account"
    />
  )
}
