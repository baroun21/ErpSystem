import { useState, useEffect } from 'react'
import './FinanceReportStyle.css'
import { financeReportService } from '../../services/api'
import { formatMoney } from './financeUtils'

export default function IncomeStatement() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [fiscalYear, setFiscalYear] = useState(new Date().getFullYear())
  const [period, setPeriod] = useState('FULL_YEAR')

  useEffect(() => {
    fetchReport()
  }, [fiscalYear, period])

  const fetchReport = async () => {
    try {
      setLoading(true)
      const response = await financeReportService.getIncomeStatement(fiscalYear, period)
      setData(response.data)
      setError(null)
    } catch (err) {
      setError(err.message)
      setData(null)
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="finance-loading">Loading Income Statement...</div>
  if (error) return <div className="finance-error">Error: {error}</div>

  if (!data) return <div className="finance-empty">No data available</div>

  return (
    <div className="finance-report-container">
      <div className="report-header">
        <h1>Income Statement</h1>
        <div className="report-controls">
          <label>
            Fiscal Year:
            <input
              type="number"
              value={fiscalYear}
              onChange={(e) => setFiscalYear(Number(e.target.value))}
              min="2000"
              max="2099"
            />
          </label>
          <label>
            Period:
            <select value={period} onChange={(e) => setPeriod(e.target.value)}>
              <option value="FULL_YEAR">Full Year</option>
              <option value="Q1">Q1</option>
              <option value="Q2">Q2</option>
              <option value="Q3">Q3</option>
              <option value="Q4">Q4</option>
            </select>
          </label>
        </div>
      </div>

      <div className="report-content">
        <table className="finance-report-table">
          <thead>
            <tr>
              <th>Item</th>
              <th className="amount">Amount</th>
              <th className="percentage">% of Revenue</th>
            </tr>
          </thead>
          <tbody>
            {/* Revenue Section */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>REVENUE</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Total Revenue</td>
              <td className="amount">{formatMoney(data.revenue)}</td>
              <td className="percentage">100%</td>
            </tr>

            {/* Cost of Goods Sold Section */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>COST OF GOODS SOLD</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Beginning Inventory</td>
              <td className="amount">{formatMoney(data.begInventory)}</td>
              <td className="percentage">-</td>
            </tr>
            <tr>
              <td className="indent">Purchases</td>
              <td className="amount">{formatMoney(data.purchases)}</td>
              <td className="percentage">-</td>
            </tr>
            <tr>
              <td className="indent">Ending Inventory</td>
              <td className="amount">({formatMoney(data.endInventory)})</td>
              <td className="percentage">-</td>
            </tr>
            <tr className="subtotal">
              <td>Cost of Goods Sold</td>
              <td className="amount">{formatMoney(data.cogs)}</td>
              <td className="percentage">
                {((data.cogs / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>

            {/* Gross Profit */}
            <tr className="highlight">
              <td>
                <strong>GROSS PROFIT</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(data.grossProfit)}</strong>
              </td>
              <td className="percentage">
                <strong>{((data.grossProfit / data.revenue) * 100).toFixed(2)}%</strong>
              </td>
            </tr>

            {/* Operating Expenses */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>OPERATING EXPENSES</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Salaries & Wages</td>
              <td className="amount">{formatMoney(data.salariesWages)}</td>
              <td className="percentage">
                {((data.salariesWages / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent">Depreciation</td>
              <td className="amount">{formatMoney(data.depreciation)}</td>
              <td className="percentage">
                {((data.depreciation / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent">Rent & Utilities</td>
              <td className="amount">{formatMoney(data.rent)}</td>
              <td className="percentage">
                {((data.rent / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent">Other Operating Expenses</td>
              <td className="amount">{formatMoney(data.otherOpex)}</td>
              <td className="percentage">
                {((data.otherOpex / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr className="subtotal">
              <td>Total Operating Expenses</td>
              <td className="amount">
                {formatMoney(
                  data.salariesWages + data.depreciation + data.rent + data.otherOpex
                )}
              </td>
              <td className="percentage">
                {(
                  ((data.salariesWages +
                    data.depreciation +
                    data.rent +
                    data.otherOpex) /
                    data.revenue) *
                  100
                ).toFixed(2)}
                %
              </td>
            </tr>

            {/* Operating Income */}
            <tr className="highlight">
              <td>
                <strong>OPERATING INCOME</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(data.operatingIncome)}</strong>
              </td>
              <td className="percentage">
                <strong>
                  {((data.operatingIncome / data.revenue) * 100).toFixed(2)}%
                </strong>
              </td>
            </tr>

            {/* Other Income/Expenses */}
            <tr className="section-header">
              <td colSpan="3">
                <strong>OTHER INCOME / EXPENSES</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Interest Income</td>
              <td className="amount">{formatMoney(data.interestIncome)}</td>
              <td className="percentage">
                {((data.interestIncome / data.revenue) * 100).toFixed(2)}%
              </td>
            </tr>
            <tr>
              <td className="indent">Interest Expense</td>
              <td className="amount">({formatMoney(data.interestExpense)})</td>
              <td className="percentage">
                ({((data.interestExpense / data.revenue) * 100).toFixed(2)}%)
              </td>
            </tr>

            {/* Net Income */}
            <tr className="net-income">
              <td>
                <strong>NET INCOME (LOSS)</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(data.netIncome)}</strong>
              </td>
              <td className="percentage">
                <strong>
                  {((data.netIncome / data.revenue) * 100).toFixed(2)}%
                </strong>
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
