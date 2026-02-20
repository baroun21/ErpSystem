import { useEffect, useState } from 'react'
import { salesAnalyticsService, salesOpportunityService } from '../../services/api'
import { formatMoney } from '../finance/financeUtils'
import { getCompanyId } from '../../services/salesConfig'

const summaryCards = [
  { key: 'forecast', label: 'Pipeline Forecast', tone: 'accent' },
  { key: 'bestCase', label: 'Best Case Revenue', tone: 'accent-2' },
  { key: 'avgDeal', label: 'Average Deal Size', tone: 'accent-3' },
  { key: 'openCount', label: 'Open Opportunities', tone: 'accent' },
]

export default function SalesDashboard() {
  const [summary, setSummary] = useState({})
  const [loading, setLoading] = useState(true)
  const companyId = getCompanyId()

  useEffect(() => {
    let isMounted = true

    const loadSummary = async () => {
      try {
        setLoading(true)
        const [forecast, bestCase, avgDeal, openCount] = await Promise.all([
          salesAnalyticsService.revenueForecast(companyId),
          salesAnalyticsService.bestCase(companyId),
          salesAnalyticsService.averageDealSize(companyId),
          salesOpportunityService.countOpen(companyId),
        ])

        if (!isMounted) {
          return
        }

        setSummary({
          forecast: forecast.data,
          bestCase: bestCase.data,
          avgDeal: avgDeal.data,
          openCount: openCount.data,
        })
      } catch (error) {
        if (isMounted) {
          setSummary({})
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    loadSummary()

    return () => {
      isMounted = false
    }
  }, [companyId])

  return (
    <div className="finance-page">
      <div className="finance-page-header">
        <div>
          <h2 className="finance-title">Sales Dashboard</h2>
          <p className="finance-subtitle">Live snapshot of pipeline and revenue momentum.</p>
        </div>
      </div>

      <div className="finance-grid">
        {summaryCards.map((card) => (
          <div key={card.key} className="finance-card">
            <p className="finance-meta">{card.label}</p>
            <p className="finance-kpi-value">
              {loading ? '—' : card.key === 'openCount'
                ? summary.openCount ?? 0
                : formatMoney(summary[card.key] ?? 0)}
            </p>
          </div>
        ))}
      </div>
    </div>
  )
}
