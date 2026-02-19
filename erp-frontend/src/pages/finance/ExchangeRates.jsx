import FinanceTablePage from './FinanceTablePage'
import { exchangeRateService } from '../../services/api'
import { formatDate, pickFirst, pickFirstNumber } from './financeUtils'
import AddExchangeRateForm from '../../components/forms/AddExchangeRateForm'

const columns = [
  {
    header: 'From Currency',
    accessor: (item) => pickFirst(item, ['fromCurrency', 'from']),
  },
  {
    header: 'To Currency',
    accessor: (item) => pickFirst(item, ['toCurrency', 'to']),
  },
  {
    header: 'Rate',
    accessor: (item) => (Math.round(pickFirstNumber(item, ['rate', 'value']) * 100000) / 100000).toFixed(8),
  },
  {
    header: 'Inverse Rate',
    accessor: (item) => (Math.round(pickFirstNumber(item, ['inverseRate', 'reciprocal']) * 100000) / 100000).toFixed(8),
  },
  {
    header: 'Rate Date',
    accessor: (item) => formatDate(pickFirst(item, ['rateDate', 'date'])),
  },
  {
    header: 'Manual',
    accessor: (item) => pickFirst(item, ['isManual', 'manual']) === true ? 'Yes' : 'No',
  },
  {
    header: 'Source',
    accessor: (item) => pickFirst(item, ['source', 'provider']),
  },
]

export default function ExchangeRates() {
  return (
    <FinanceTablePage
      title="Exchange Rates"
      service={exchangeRateService}
      columns={columns}
      emptyMessage="No exchange rates found."
      FormComponent={AddExchangeRateForm}
      itemName="Exchange Rate"
    />
  )
}
