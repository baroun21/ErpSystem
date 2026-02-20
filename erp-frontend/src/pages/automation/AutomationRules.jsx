import { useMemo } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { automationRuleService } from '../../services/api'
import { formatDate } from '../finance/financeUtils'
import { getCompanyId } from '../../services/salesConfig'

export default function AutomationRules() {
  const service = useMemo(() => ({
    getAll: () => automationRuleService.getAll(getCompanyId()),
  }), [])

  const columns = [
    { header: 'Rule', accessor: 'name' },
    { header: 'Trigger', accessor: 'triggerType' },
    { header: 'Event/Schedule', accessor: (item) => item.triggerEvent || item.scheduleExpression || '-' },
    { header: 'Action', accessor: 'actionType' },
    { header: 'Enabled', accessor: 'enabled' },
    { header: 'Next Run', accessor: (item) => formatDate(item.nextRunAt) },
    { header: 'Updated', accessor: (item) => formatDate(item.updatedAt) },
  ]

  return (
    <FinanceTablePage
      title="Automation Rules"
      service={service}
      columns={columns}
      emptyMessage="No automation rules defined yet."
      itemName="Rule"
    />
  )
}
