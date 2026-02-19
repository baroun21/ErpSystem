import FinanceTablePage from './FinanceTablePage'
import { bankAccountService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddBankAccountForm from '../../components/forms/AddBankAccountForm'

const columns = [
  {
    header: 'Account',
    accessor: (item) => pickFirst(item, ['accountName', 'name']),
  },
  {
    header: 'Bank',
    accessor: (item) => pickFirst(item, ['bankName', 'bank']),
  },
  {
    header: 'Number',
    accessor: (item) => pickFirst(item, ['accountNumber', 'number']),
  },
  {
    header: 'Balance',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['balance', 'currentBalance'])),
  },
  {
    header: 'Currency',
    accessor: (item) => pickFirst(item, ['currency', 'accountCurrency']),
  },
]

export default function BankAccounts() {
  return (
    <FinanceTablePage
      title="Bank Accounts"
      service={bankAccountService}
      columns={columns}
      emptyMessage="No bank accounts found."
      FormComponent={AddBankAccountForm}
      itemName="Account"
    />
  )
}
