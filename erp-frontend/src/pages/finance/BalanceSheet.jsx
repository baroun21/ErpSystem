import { useState, useEffect } from 'react'
import './FinanceReportStyle.css'
import { financeReportService } from '../../services/api'
import { formatMoney } from './financeUtils'

export default function BalanceSheet() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [fiscalYear, setFiscalYear] = useState(new Date().getFullYear())
  const [date, setDate] = useState(new Date().toISOString().split('T')[0])

  useEffect(() => {
    fetchReport()
  }, [fiscalYear, date])

  const fetchReport = async () => {
    try {
      setLoading(true)
      const response = await financeReportService.getBalanceSheet(date)
      setData(response.data)
      setError(null)
    } catch (err) {
      setError(err.message)
      setData(null)
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="finance-loading">Loading Balance Sheet...</div>
  if (error) return <div className="finance-error">Error: {error}</div>

  if (!data) return <div className="finance-empty">No data available</div>

  const totalAssets = data.currentAssets + data.fixedAssets
  const totalLiabilities = data.currentLiabilities + data.longTermLiabilities
  const totalEquity = data.commonStock + data.retainedEarnings

  return (
    <div className="finance-report-container">
      <div className="report-header">
        <h1>Balance Sheet</h1>
        <div className="report-controls">
          <label>
            As of:
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
            />
          </label>
        </div>
      </div>

      <div className="report-content">
        <table className="finance-report-table">
          <thead>
            <tr>
              <th>Item</th>
              <th className="amount">Amount</th>
              <th className="percentage">% of Total Assets</th>
            </tr>
          </thead>
          <tbody>
            {/* ASSETS Section */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>ASSETS</strong>
              </td>
            </tr>

            {/* Current Assets */}
            <tr className="subsection-header">
              <td className="indent">
                <strong>Current Assets</strong>
              </td>
              <td className="amount" />
              <td className="percentage" />
            </tr>
            <tr>
              <td className="indent indent-2">Cash & Cash Equivalents</td>
              <td className="amount">{formatMoney(data.cash)}</td>
              <td className="percentage">{((data.cash / totalAssets) * 100).toFixed(2)}%</td>
            </tr>
            <tr>
              <td className="indent indent-2">Accounts Receivable</td>
              <td className="amount">{formatMoney(data.accountsReceivable)}</td>
              <td className="percentage">
                {((data.accountsReceivable / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Inventory</td>
              <td className="amount">{formatMoney(data.inventory)}</td>
              <td className="percentage">
                {((data.inventory / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Other Current Assets</td>
              <td className="amount">{formatMoney(data.otherCurrentAssets)}</td>
              <td className="percentage">
                {((data.otherCurrentAssets / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td className="indent">Total Current Assets</td>
              <td className="amount">{formatMoney(data.currentAssets)}</td>
              <td className="percentage">
                {((data.currentAssets / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Fixed Assets */}
            <tr className="subsection-header">
              <td className="indent">
                <strong>Fixed Assets</strong>
              </td>
              <td className="amount" />
              <td className="percentage" />
            </tr>
            <tr>
              <td className="indent indent-2">Property, Plant & Equipment</td>
              <td className="amount">{formatMoney(data.ppe)}</td>
              <td className="percentage">
                {((data.ppe / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Accumulated Depreciation</td>
              <td className="amount">({formatMoney(data.accumulatedDepreciation)})</td>
              <td className="percentage">
                ({((data.accumulatedDepreciation / totalAssets) * 100).toFixed(2)}%)
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Intangible Assets</td>
              <td className="amount">{formatMoney(data.intangibleAssets)}</td>
              <td className="percentage">
                {((data.intangibleAssets / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Goodwill</td>
              <td className="amount">{formatMoney(data.goodwill)}</td>
              <td className="percentage">
                {((data.goodwill / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td className="indent">Total Fixed Assets</td>
              <td className="amount">{formatMoney(data.fixedAssets)}</td>
              <td className="percentage">
                {((data.fixedAssets / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Total Assets */}
            <tr className="total">
              <td>
                <strong>TOTAL ASSETS</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(totalAssets)}</strong>
              </td>
              <td className="percentage">
                <strong>100%</strong>
              </td>
            </tr>

            {/* LIABILITIES & EQUITY Section */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>LIABILITIES & EQUITY</strong>
              </td>
            </tr>

            {/* Current Liabilities */}
            <tr className="subsection-header">
              <td className="indent">
                <strong>Current Liabilities</strong>
              </td>
              <td className="amount" />
              <td className="percentage" />
            </tr>
            <tr>
              <td className="indent indent-2">Accounts Payable</td>
              <td className="amount">{formatMoney(data.accountsPayable)}</td>
              <td className="percentage">
                {((data.accountsPayable / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Short-Term Debt</td>
              <td className="amount">{formatMoney(data.shortTermDebt)}</td>
              <td className="percentage">
                {((data.shortTermDebt / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Accrued Expenses</td>
              <td className="amount">{formatMoney(data.accruedExpenses)}</td>
              <td className="percentage">
                {((data.accruedExpenses / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Deferred Revenue</td>
              <td className="amount">{formatMoney(data.deferredRevenue)}</td>
              <td className="percentage">
                {((data.deferredRevenue / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td className="indent">Total Current Liabilities</td>
              <td className="amount">{formatMoney(data.currentLiabilities)}</td>
              <td className="percentage">
                {((data.currentLiabilities / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Long-Term Liabilities */}
            <tr className="subsection-header">
              <td className="indent">
                <strong>Long-Term Liabilities</strong>
              </td>
              <td className="amount" />
              <td className="percentage" />
            </tr>
            <tr>
              <td className="indent indent-2">Long-Term Debt</td>
              <td className="amount">{formatMoney(data.longTermDebt)}</td>
              <td className="percentage">
                {((data.longTermDebt / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Deferred Tax Liabilities</td>
              <td className="amount">{formatMoney(data.deferredTaxLiabilities)}</td>
              <td className="percentage">
                {((data.deferredTaxLiabilities / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td className="indent">Total Long-Term Liabilities</td>
              <td className="amount">{formatMoney(data.longTermLiabilities)}</td>
              <td className="percentage">
                {((data.longTermLiabilities / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Total Liabilities */}
            <tr className="highlight">
              <td>
                <strong>TOTAL LIABILITIES</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(totalLiabilities)}</strong>
              </td>
              <td className="percentage">
                <strong>
                  {((totalLiabilities / totalAssets) * 100).toFixed(2)}%
                </strong>
              </td>
            </tr>

            {/* STOCKHOLDERS' EQUITY */}
            <tr className="subsection-header">
              <td colSpan="3">
                <strong>STOCKHOLDERS' EQUITY</strong>
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Common Stock</td>
              <td className="amount">{formatMoney(data.commonStock)}</td>
              <td className="percentage">
                {((data.commonStock / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent indent-2">Retained Earnings</td>
              <td className="amount">{formatMoney(data.retainedEarnings)}</td>
              <td className="percentage">
                {((data.retainedEarnings / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td className="indent">Total Stockholders' Equity</td>
              <td className="amount">{formatMoney(totalEquity)}</td>
              <td className="percentage">
                {((totalEquity / totalAssets) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Total Liabilities & Equity */}
            <tr className="total">
              <td>
                <strong>TOTAL LIABILITIES & EQUITY</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(totalLiabilities + totalEquity)}</strong>
              </td>
              <td className="percentage">
                <strong>100%</strong>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div className="report-footer">
        <p>
          Generated on {new Date().toLocaleDateString()} at{' '}
          {new Date().toLocaleTimeString()}
        </p>
        <p>This is an unaudited financial statement for internal use only.</p>
      </div>
    </div>
  )
}
