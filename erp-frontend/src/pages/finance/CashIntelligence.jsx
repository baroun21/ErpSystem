import { useEffect, useState } from 'react'
import { cashIntelligenceService } from '../../services/api'
import { formatMoney } from './financeUtils'
import { getCompanyId } from '../../services/companyConfig'

const emptySummary = {
  cashPosition: {
    totalCash: 0,
    reconciledCash: 0,
    accountCount: 0,
  },
  forecast: {
    expectedInflow: 0,
    expectedOutflow: 0,
    projection30: 0,
    projection60: 0,
    projection90: 0,
    cashRunwayDays: 0,
  },
  burnRate: {
    burnRateMonthly: 0,
    netCashFlowMonthly: 0,
  },
  receivableRisk: {
    topCustomerName: '-',
    topCustomerShare: 0,
    overdueRatio: 0,
    marginTrend: [],
  },
}

export default function CashIntelligence() {
  const [summary, setSummary] = useState(emptySummary)
  const [jobId, setJobId] = useState(null)
  const [status, setStatus] = useState('PENDING')
  const [error, setError] = useState(null)
  const [simulation, setSimulation] = useState(null)
  const [inflowMultiplier, setInflowMultiplier] = useState('1.0')
  const [outflowMultiplier, setOutflowMultiplier] = useState('1.0')

  useEffect(() => {
    const companyId = getCompanyId()

    const startJob = async () => {
      try {
        const response = await cashIntelligenceService.startJob(companyId)
        setJobId(response.data.jobId)
        setStatus(response.data.status)
        setError(null)
      } catch (err) {
        setError('Unable to start cash intelligence job.')
      }
    }

    startJob()
  }, [])

  useEffect(() => {
    if (!jobId) {
      return undefined
    }

    const intervalId = setInterval(async () => {
      try {
        const response = await cashIntelligenceService.getJob(jobId)
        const data = response.data
        setStatus(data.status)
        if (data.status === 'COMPLETE' && data.summary) {
          setSummary(data.summary)
          clearInterval(intervalId)
        }
        if (data.status === 'FAILED') {
          setError(data.error || 'Cash intelligence job failed.')
          clearInterval(intervalId)
        }
      } catch (err) {
        setError('Unable to load cash intelligence status.')
        clearInterval(intervalId)
      }
    }, 2000)

    return () => {
      clearInterval(intervalId)
    }
  }, [jobId])

  const handleSimulate = async () => {
    try {
      const payload = {
        companyId: getCompanyId(),
        inflowMultiplier: Number(inflowMultiplier),
        outflowMultiplier: Number(outflowMultiplier),
      }
      const response = await cashIntelligenceService.simulate(payload)
      setSimulation(response.data)
    } catch (err) {
      setError('Unable to simulate cash scenario.')
    }
  }

  const topCustomerShare = Number(summary.receivableRisk?.topCustomerShare || 0) * 100
  const overdueRatio = Number(summary.receivableRisk?.overdueRatio || 0) * 100

  return (
    <div className="finance-page">
      <div className="finance-page-header">
        <div>
          <h2 className="finance-title">Cash Intelligence</h2>
          <p className="finance-subtitle">Forecast liquidity, runway, and receivable risk.</p>
        </div>
      </div>

      {error && <div className="finance-alert">{error}</div>}

      <div className="finance-kpi-grid">
        <div className="finance-kpi">
          <span className="finance-kpi-label">Current Cash</span>
          <strong className="finance-kpi-value">{formatMoney(summary.cashPosition.totalCash)}</strong>
          <span className="finance-kpi-meta">Across {summary.cashPosition.accountCount} accounts</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Expected Inflow</span>
          <strong className="finance-kpi-value">{formatMoney(summary.forecast.expectedInflow)}</strong>
          <span className="finance-kpi-meta">Weighted AR pipeline</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Expected Outflow</span>
          <strong className="finance-kpi-value">{formatMoney(summary.forecast.expectedOutflow)}</strong>
          <span className="finance-kpi-meta">AP + payroll + recurring</span>
        </div>
        <div className="finance-kpi">
          <span className="finance-kpi-label">Cash Runway</span>
          <strong className="finance-kpi-value">{summary.forecast.cashRunwayDays.toFixed(1)} days</strong>
          <span className="finance-kpi-meta">Based on burn rate</span>
        </div>
      </div>

      <div className="finance-grid" style={{ marginTop: '16px' }}>
        <div className="finance-card">
          <div className="finance-card-header">
            <h3>30/60/90 Projection</h3>
          </div>
          <div className="finance-card-body">
            <ul className="finance-list">
              <li className="finance-list-item">
                <span className="finance-list-title">30 days</span>
                <span className="finance-list-value">{formatMoney(summary.forecast.projection30)}</span>
              </li>
              <li className="finance-list-item">
                <span className="finance-list-title">60 days</span>
                <span className="finance-list-value">{formatMoney(summary.forecast.projection60)}</span>
              </li>
              <li className="finance-list-item">
                <span className="finance-list-title">90 days</span>
                <span className="finance-list-value">{formatMoney(summary.forecast.projection90)}</span>
              </li>
            </ul>
          </div>
        </div>

        <div className="finance-card">
          <div className="finance-card-header">
            <h3>Burn Rate</h3>
          </div>
          <div className="finance-card-body">
            <ul className="finance-list">
              <li className="finance-list-item">
                <span className="finance-list-title">Net monthly cash flow</span>
                <span className="finance-list-value">{formatMoney(summary.burnRate.netCashFlowMonthly)}</span>
              </li>
              <li className="finance-list-item">
                <span className="finance-list-title">Burn rate</span>
                <span className="finance-list-value">{formatMoney(summary.burnRate.burnRateMonthly)}</span>
              </li>
            </ul>
          </div>
        </div>

        <div className="finance-card">
          <div className="finance-card-header">
            <h3>Receivable Risk</h3>
          </div>
          <div className="finance-card-body">
            <ul className="finance-list">
              <li className="finance-list-item">
                <span className="finance-list-title">Top customer</span>
                <span className="finance-list-value">{summary.receivableRisk.topCustomerName}</span>
              </li>
              <li className="finance-list-item">
                <span className="finance-list-title">Revenue concentration</span>
                <span className="finance-list-value">{topCustomerShare.toFixed(1)}%</span>
              </li>
              <li className="finance-list-item">
                <span className="finance-list-title">Overdue ratio</span>
                <span className="finance-list-value">{overdueRatio.toFixed(1)}%</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div className="finance-card" style={{ marginTop: '16px' }}>
        <div className="finance-card-header">
          <h3>Scenario Simulation</h3>
        </div>
        <div className="finance-card-body">
          <div className="finance-header-actions">
            <label className="finance-meta" htmlFor="inflow-multiplier">Inflow Multiplier</label>
            <input
              id="inflow-multiplier"
              className="finance-select"
              type="number"
              step="0.1"
              value={inflowMultiplier}
              onChange={(event) => setInflowMultiplier(event.target.value)}
            />
            <label className="finance-meta" htmlFor="outflow-multiplier">Outflow Multiplier</label>
            <input
              id="outflow-multiplier"
              className="finance-select"
              type="number"
              step="0.1"
              value={outflowMultiplier}
              onChange={(event) => setOutflowMultiplier(event.target.value)}
            />
            <button className="finance-btn-primary" type="button" onClick={handleSimulate}>
              Run Simulation
            </button>
          </div>

          {simulation && (
            <div className="finance-grid" style={{ marginTop: '12px' }}>
              <div className="finance-kpi">
                <span className="finance-kpi-label">Simulated Inflow</span>
                <strong className="finance-kpi-value">{formatMoney(simulation.expectedInflow)}</strong>
              </div>
              <div className="finance-kpi">
                <span className="finance-kpi-label">Simulated Outflow</span>
                <strong className="finance-kpi-value">{formatMoney(simulation.expectedOutflow)}</strong>
              </div>
              <div className="finance-kpi">
                <span className="finance-kpi-label">Simulated 90 Day Cash</span>
                <strong className="finance-kpi-value">{formatMoney(simulation.projection90)}</strong>
              </div>
            </div>
          )}
        </div>
      </div>

      {status !== 'COMPLETE' && !error && (
        <div className="finance-muted" style={{ marginTop: '12px' }}>
          Cash intelligence job status: {status}
        </div>
      )}
    </div>
  )
}
