import { useState, useEffect } from 'react'
import './FinanceReportStyle.css'
import { financeReportService } from '../../services/api'
import { formatMoney } from './financeUtils'

export default function APAging() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [asOfDate, setAsOfDate] = useState(new Date().toISOString().split('T')[0])
  const [vendorFilter, setVendorFilter] = useState('')

  useEffect(() => {
    fetchReport()
  }, [asOfDate, vendorFilter])

  const fetchReport = async () => {
    try {
      setLoading(true)
      const response = await financeReportService.getAPAging(asOfDate, vendorFilter)
      setData(response.data)
      setError(null)
    } catch (err) {
      setError(err.message)
      setData(null)
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="finance-loading">Loading AP Aging Report...</div>
  if (error) return <div className="finance-error">Error: {error}</div>

  if (!data) return <div className="finance-empty">No data available</div>

  const summary = data.summary || {
    total: 0,
    current: 0,
    days30: 0,
    days60: 0,
    days90: 0,
    days120plus: 0,
  }

  const totalOutstanding = summary.total || 0

  return (
    <div className="finance-report-container">
      <div className="report-header">
        <h1>Accounts Payable - Aging Report</h1>
        <div className="report-controls">
          <label>
            As of Date:
            <input
              type="date"
              value={asOfDate}
              onChange={(e) => setAsOfDate(e.target.value)}
            />
          </label>
          <label>
            Vendor Filter (optional):
            <input
              type="text"
              value={vendorFilter}
              onChange={(e) => setVendorFilter(e.target.value)}
              placeholder="Search by vendor name..."
            />
          </label>
        </div>
      </div>

      <div className="report-content">
        {/* Summary Section */}
        <div className="aging-summary">
          <h3>Summary by Age</h3>
          <div className="summary-grid">
            <div className="summary-item">
              <span className="label">Current (0-30 days)</span>
              <span className="value">{formatMoney(summary.current)}</span>
              <span className="percent">
                {((summary.current / totalOutstanding) * 100).toFixed(1)}%
              </span>
            </div>
            <div className="summary-item">
              <span className="label">31 - 60 days</span>
              <span className="value">{formatMoney(summary.days30)}</span>
              <span className="percent">
                {((summary.days30 / totalOutstanding) * 100).toFixed(1)}%
              </span>
            </div>
            <div className="summary-item">
              <span className="label">61 - 90 days</span>
              <span className="value">{formatMoney(summary.days60)}</span>
              <span className="percent">
                {((summary.days60 / totalOutstanding) * 100).toFixed(1)}%
              </span>
            </div>
            <div className="summary-item">
              <span className="label">91 - 120 days</span>
              <span className="value">{formatMoney(summary.days90)}</span>
              <span className="percent">
                {((summary.days90 / totalOutstanding) * 100).toFixed(1)}%
              </span>
            </div>
            <div className="summary-item overdue">
              <span className="label">Over 120 days</span>
              <span className="value">{formatMoney(summary.days120plus)}</span>
              <span className="percent">
                {((summary.days120plus / totalOutstanding) * 100).toFixed(1)}%
              </span>
            </div>
            <div className="summary-item total">
              <span className="label">
                <strong>Total Outstanding</strong>
              </span>
              <span className="value">
                <strong>{formatMoney(totalOutstanding)}</strong>
              </span>
              <span className="percent">
                <strong>100%</strong>
              </span>
            </div>
          </div>
        </div>

        {/* Aging Detail Table */}
        <table className="finance-report-table">
          <thead>
            <tr>
              <th>Vendor</th>
              <th className="amount">Invoice #</th>
              <th className="amount">Invoice Date</th>
              <th className="amount">Due Date</th>
              <th className="amount">Days Outstanding</th>
              <th className="amount">Amount Due</th>
              <th className="amount">Age Category</th>
            </tr>
          </thead>
          <tbody>
            {data.details && data.details.length > 0 ? (
              data.details.map((item, idx) => {
                const daysOld = item.daysOutstanding || 0
                let ageCategory = 'Current'
                if (daysOld > 120) ageCategory = 'Over 120 days'
                else if (daysOld > 90) ageCategory = '91 - 120 days'
                else if (daysOld > 60) ageCategory = '61 - 90 days'
                else if (daysOld > 30) ageCategory = '31 - 60 days'

                return (
                  <tr
                    key={idx}
                    className={
                      daysOld > 120 ? 'overdue-row' : daysOld > 60 ? 'aging-row' : ''
                    }
                  >
                    <td>{item.vendorName}</td>
                    <td className="amount">{item.invoiceNumber}</td>
                    <td className="amount">
                      {new Date(item.invoiceDate).toLocaleDateString()}
                    </td>
                    <td className="amount">
                      {new Date(item.dueDate).toLocaleDateString()}
                    </td>
                    <td className="amount">{daysOld}</td>
                    <td className="amount">{formatMoney(item.amountDue)}</td>
                    <td className="amount">
                      <span
                        className={`age-badge ${ageCategory
                          .toLowerCase()
                          .replace(/\s+/g, '-')}`}
                      >
                        {ageCategory}
                      </span>
                    </td>
                  </tr>
                )
              })
            ) : (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center' }}>
                  No invoices found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div className="report-footer">
        <p>
          Generated on {new Date().toLocaleDateString()} at{' '}
          {new Date().toLocaleTimeString()}
        </p>
        <p>This report should be reviewed regularly to manage vendor relationships.</p>
      </div>
    </div>
  )
}
