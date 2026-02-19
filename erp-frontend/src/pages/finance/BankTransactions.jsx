import FinanceTablePage from './FinanceTablePage'
import { bankTransactionService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddBankTransactionForm from '../../components/forms/AddBankTransactionForm'

const columns = [
  {
    header: 'Date',
    accessor: (item) => formatDate(pickFirst(item, ['transactionDate', 'date'])),
  },
  {
    header: 'Description',
    accessor: (item) => pickFirst(item, ['description', 'memo']),
  },
  {
    header: 'Amount',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['amount', 'transactionAmount'])),
  },
  {
    header: 'Type',
    accessor: (item) => pickFirst(item, ['type', 'transactionType']),
  },
  {
    header: 'Account',
    accessor: (item) => item?.bankAccount?.accountName || pickFirst(item, ['accountName', 'account']),
  },
]

export default function BankTransactions() {
  return (
    <FinanceTablePage
      title="Bank Transactions"
      service={bankTransactionService}
      columns={columns}
      emptyMessage="No bank transactions found."
      FormComponent={AddBankTransactionForm}
      itemName="Transaction"
    />
  )
}
