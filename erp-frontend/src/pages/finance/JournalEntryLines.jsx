import FinanceTablePage from './FinanceTablePage'
import { journalEntryLineService } from '../../services/api'
import { formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddJournalEntryLineForm from '../../components/forms/AddJournalEntryLineForm'

const columns = [
  {
    header: 'Account',
    accessor: (item) =>
      pickFirst(item, ['accountCode', 'accountName']) ||
      item?.account?.name ||
      item?.chartOfAccount?.name ||
      '-'
  },
  {
    header: 'Description',
    accessor: (item) => pickFirst(item, ['description', 'memo']),
  },
  {
    header: 'Debit',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['debit', 'debitAmount'])),
  },
  {
    header: 'Credit',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['credit', 'creditAmount'])),
  },
]

export default function JournalEntryLines() {
  return (
    <FinanceTablePage
      title="Journal Entry Lines"
      service={journalEntryLineService}
      columns={columns}
      emptyMessage="No journal entry lines found."
      FormComponent={AddJournalEntryLineForm}
      itemName="Entry Line"
    />
  )
}
