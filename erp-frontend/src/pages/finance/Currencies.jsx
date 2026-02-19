import FinanceTablePage from './FinanceTablePage'
import { currencyService } from '../../services/api'
import { pickFirst } from './financeUtils'
import AddCurrencyForm from '../../components/forms/AddCurrencyForm'

const columns = [
  {
    header: 'Currency Code',
    accessor: (item) => pickFirst(item, ['currencyCode', 'code']),
  },
  {
    header: 'Currency Name',
    accessor: (item) => pickFirst(item, ['currencyName', 'name']),
  },
  {
    header: 'Symbol',
    accessor: (item) => pickFirst(item, ['symbol']),
  },
  {
    header: 'Base Currency',
    accessor: (item) => pickFirst(item, ['isBaseCurrency', 'base']) === true ? 'Yes' : 'No',
  },
  {
    header: 'Active',
    accessor: (item) => pickFirst(item, ['active']) === true ? 'Active' : 'Inactive',
  },
]

export default function Currencies() {
  return (
    <FinanceTablePage
      title="Currencies"
      service={currencyService}
      columns={columns}
      emptyMessage="No currencies found."
      FormComponent={AddCurrencyForm}
      itemName="Currency"
    />
  )
}
