import FinanceTablePage from './FinanceTablePage'
import { trialBalanceService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddTrialBalanceForm from '../../components/forms/AddTrialBalanceForm'

const columns = [
  {
    header: 'Account',
    accessor: (item) =>
      item?.account?.name || pickFirst(item, ['accountName', 'accountCode', 'account']),
  },
  {
    header: 'Debit',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['debit', 'totalDebit'])),
  },
  {
    header: 'Credit',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['credit', 'totalCredit'])),
  },
  {
    header: 'Period',
    accessor: (item) => pickFirst(item, ['period', 'periodName', 'asOfDate']),
  },
]

export default function TrialBalance() {
  return (
    <FinanceTablePage
      title="Trial Balance"
      service={trialBalanceService}
      columns={columns}
      emptyMessage="No trial balance data found."
      FormComponent={AddTrialBalanceForm}
      itemName="Trial Balance"
    />
  )
}
