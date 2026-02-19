import FinanceTablePage from './FinanceTablePage'
import { financeUserRoleService } from '../../services/api'
import { pickFirst } from './financeUtils'

const columns = [
  {
    header: 'User',
    accessor: (item) =>
      item?.user?.name || item?.user?.username || pickFirst(item, ['userName', 'user']),
  },
  {
    header: 'Role',
    accessor: (item) => item?.role?.name || pickFirst(item, ['roleName', 'role']),
  },
  {
    header: 'Company',
    accessor: (item) => item?.company?.name || pickFirst(item, ['companyName', 'company']),
  },
  {
    header: 'Status',
    accessor: (item) => pickFirst(item, ['status', 'state', 'active']),
  },
]

export default function FinanceUserRoles() {
  return (
    <FinanceTablePage
      title="Finance User Roles"
      service={financeUserRoleService}
      columns={columns}
      emptyMessage="No finance roles found."
    />
  )
}
