import { useMemo } from 'react'
import FinanceTablePage from '../finance/FinanceTablePage'
import { automationLogService } from '../../services/api'
import { formatDate } from '../finance/financeUtils'

export default function AutomationLogs() {
  const service = useMemo(() => ({
    getAll: () => automationLogService.getAll(),
  }), [])

  const columns = [
    { header: 'Rule', accessor: (item) => item.ruleName || item.ruleId },
    { header: 'Trigger', accessor: 'triggerType' },
    { header: 'Action', accessor: 'actionType' },
    { header: 'Status', accessor: 'status' },
    { header: 'Started', accessor: (item) => formatDate(item.startedAt) },
    { header: 'Message', accessor: 'message' },
  ]

  return (
    <FinanceTablePage
      title="Automation Execution Logs"
      service={service}
      columns={columns}
      emptyMessage="No execution logs yet."
      itemName="Log"
    />
  )
}
