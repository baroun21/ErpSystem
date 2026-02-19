import FinanceTablePage from './FinanceTablePage'
import { companyService } from '../../services/api'
import { pickFirst } from './financeUtils'
import AddCompanyForm from '../../components/forms/AddCompanyForm'

const columns = [
  {
    header: 'Company',
    accessor: (item) => pickFirst(item, ['name', 'companyName', 'legalName']),
  },
  {
    header: 'Code',
    accessor: (item) => pickFirst(item, ['code', 'companyCode', 'shortCode']),
  },
  {
    header: 'Currency',
    accessor: (item) => pickFirst(item, ['baseCurrency', 'currency', 'defaultCurrency']),
  },
  {
    header: 'Country',
    accessor: (item) => pickFirst(item, ['country', 'countryCode']),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state', 'active']),
  },
]

export default function Companies() {
  return (
    <FinanceTablePage
      title="Companies"
      service={companyService}
      columns={columns}
      emptyMessage="No companies found."
      FormComponent={AddCompanyForm}
      itemName="Company"
    />
  )
}
