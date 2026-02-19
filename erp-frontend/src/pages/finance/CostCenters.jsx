import FinanceTablePage from './FinanceTablePage'
import { costCenterService } from '../../services/api'
import { pickFirst } from './financeUtils'
import AddCostCenterForm from '../../components/forms/AddCostCenterForm'

const columns = [
  {
    header: 'Cost Center',
    accessor: (item) => pickFirst(item, ['name', 'costCenterName']),
  },
  {
    header: 'Code',
    accessor: (item) => pickFirst(item, ['code', 'costCenterCode']),
  },
  {
    header: 'Department',
    accessor: (item) => pickFirst(item, ['department', 'departmentName']),
  },
  {
    header: 'Manager',
    accessor: (item) => pickFirst(item, ['manager', 'managerName']),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state', 'active']),
  },
]

export default function CostCenters() {
  return (
    <FinanceTablePage
      title="Cost Centers"
      service={costCenterService}
      columns={columns}
      emptyMessage="No cost centers found."
      FormComponent={AddCostCenterForm}
      itemName="Cost Center"
    />
  )
}
