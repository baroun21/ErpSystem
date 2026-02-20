import { useEffect, useState } from 'react'
import { salesAnalyticsService } from '../../services/api'
import { getCompanyId } from '../../services/salesConfig'
import { formatMoney } from '../finance/financeUtils'

const emptyMetrics = {
  totalDeals: 0,
  winRate: 0,
  lossRate: 0,
  pipelineValue: 0,
  avgSalesCycleDays: 0,
}

export default function SalesAnalytics() {
  const [metrics, setMetrics] = useState(emptyMetrics)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let isMounted = true
    const companyId = getCompanyId()

    setIsLoading(true)
    setError(null)

    salesAnalyticsService
      .getSummary(companyId)
      .then((response) => {
        if (!isMounted) {
          return
        }
        setMetrics(response.data || emptyMetrics)
        setIsLoading(false)
      })
      .catch(() => {
        if (!isMounted) {
          return
        }
        setError('Unable to load analytics summary. Please try again.')
        setIsLoading(false)
      })

    return () => {
      isMounted = false
    }
  }, [])

  return (
    <div>
      <div className="finance-page-header" style={{ marginBottom: '16px' }}>
        <div>
          <h2 className="finance-title">Sales Analytics</h2>
          <p className="finance-subtitle">Pipeline health and conversion performance.</p>
        </div>
      </div>

      {error && <div className="finance-alert">{error}</div>}

      <div className="finance-kpi-grid">
        <div className="finance-kpi">
          <span className="finance-kpi-label">Total Deals</span>
          <strong className="finance-kpi-value">{metrics.totalDeals}</strong>
          <span className="finance-kpi-meta">Open + closed</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Win Rate</span>
          <strong className="finance-kpi-value">{metrics.winRate}%</strong>
          <span className="finance-kpi-meta">Share of closed wins</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Loss Rate</span>
          <strong className="finance-kpi-value">{metrics.lossRate}%</strong>
          <span className="finance-kpi-meta">Share of closed losses</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Pipeline Value</span>
          <strong className="finance-kpi-value">{formatMoney(metrics.pipelineValue)}</strong>
          <span className="finance-kpi-meta">Total open pipeline</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Avg Sales Cycle</span>
          <strong className="finance-kpi-value">{metrics.avgSalesCycleDays} days</strong>
          <span className="finance-kpi-meta">From lead to close</span>
        </div>
      </div>

      {isLoading && <div className="finance-muted" style={{ marginTop: '12px' }}>Loading analytics...</div>}
    </div>
  )
}
