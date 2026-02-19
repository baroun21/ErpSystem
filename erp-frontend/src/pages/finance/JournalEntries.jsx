import FinanceTablePage from './FinanceTablePage'
import { journalEntryService } from '../../services/api'
import { formatDate, formatMoney, pickFirst, pickFirstNumber } from './financeUtils'
import AddJournalEntryForm from '../../components/forms/AddJournalEntryForm'

const columns = [
  {
    header: 'Reference',
    accessor: (item) => pickFirst(item, ['reference', 'entryNumber', 'journalNumber']),
  },
  {
    header: 'Description',
    accessor: (item) => pickFirst(item, ['description', 'memo']),
  },
  {
    header: 'Entry Date',
    accessor: (item) => formatDate(pickFirst(item, ['entryDate', 'postingDate', 'date'])),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state']),
  },
  {
    header: 'Total',
    accessor: (item) => formatMoney(pickFirstNumber(item, ['totalAmount', 'totalDebit', 'totalCredit'])),
  },
]

export default function JournalEntries() {
  return (
    <FinanceTablePage
      title="Journal Entries"
      service={journalEntryService}
      columns={columns}
      emptyMessage="No journal entries found."
      FormComponent={AddJournalEntryForm}
      itemName="Journal Entry"
    />
  )
}
