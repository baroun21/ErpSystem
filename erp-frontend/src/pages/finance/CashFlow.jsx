import { useState, useEffect } from 'react'
import './FinanceReportStyle.css'
import { financeReportService } from '../../services/api'
import { formatMoney } from './financeUtils'

export default function CashFlow() {
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
      const response = await financeReportService.getCashFlowStatement(
        fiscalYear,
        period
      )
      setData(response.data)
      setError(null)
    } catch (err) {
      setError(err.message)
      setData(null)
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="finance-loading">Loading Cash Flow Statement...</div>
  if (error) return <div className="finance-error">Error: {error}</div>

  if (!data) return <div className="finance-empty">No data available</div>

  return (
    <div className="finance-report-container">
      <div className="report-header">
        <h1>Cash Flow Statement</h1>
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
              <th>Cash Flow Item</th>
              <th className="amount">Amount</th>
            </tr>
          </thead>
          <tbody>
            {/* Operating Activities */}
            <tr className="section-header">
              <td colSpan="2">
                <strong>CASH FLOWS FROM OPERATING ACTIVITIES</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Net Income</td>
              <td className="amount">{formatMoney(data.netIncome)}</td>
            </tr>
            <tr>
              <td className="indent">Adjustments:</td>
              <td className="amount" />
            </tr>
            <tr>
              <td className="indent indent-2">Depreciation & Amortization</td>
              <td className="amount">{formatMoney(data.depreciation)}</td>
            </tr>
            <tr>
              <td className="indent indent-2">Stock-Based Compensation</td>
              <td className="amount">{formatMoney(data.stockCompensation)}</td>
            </tr>
            <tr>
              <td className="indent indent-2">Deferred Income Taxes</td>
              <td className="amount">{formatMoney(data.deferredTaxes)}</td>
            </tr>
            <tr>
              <td className="indent">Changes in Operating Assets/Liabilities:</td>
              <td className="amount" />
            </tr>
            <tr>
              <td className="indent indent-2">Accounts Receivable</td>
              <td className="amount">{formatMoney(data.changeAccountsReceivable)}</td>
            </tr>
            <tr>
              <td className="indent indent-2">Inventory</td>
              <td className="amount">{formatMoney(data.changeInventory)}</td>
            </tr>
            <tr>
              <td className="indent indent-2">Accounts Payable</td>
              <td className="amount">{formatMoney(data.changeAccountsPayable)}</td>
            </tr>
            <tr>
              <td className="indent indent-2">Accrued Expenses</td>
              <td className="amount">{formatMoney(data.changeAccruedExpenses)}</td>
            </tr>
            <tr className="subtotal">
              <td>Net Cash from Operating Activities</td>
              <td className="amount">
                <strong>{formatMoney(data.operatingCashFlow)}</strong>
              </td>
            </tr>

            {/* Investing Activities */}
            <tr className="section-header">
              <td colSpan="2">
                <strong>CASH FLOWS FROM INVESTING ACTIVITIES</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Purchase of Property & Equipment</td>
              <td className="amount">{formatMoney(data.capExpenditures)}</td>
            </tr>
            <tr>
              <td className="indent">Sale of Fixed Assets</td>
              <td className="amount">{formatMoney(data.saleOfAssets)}</td>
            </tr>
            <tr>
              <td className="indent">Acquisition of Investments</td>
              <td className="amount">{formatMoney(data.investmentAcquisitions)}</td>
            </tr>
            <tr>
              <td className="indent">Sale of Investments</td>
              <td className="amount">{formatMoney(data.investmentSales)}</td>
            </tr>
            <tr className="subtotal">
              <td>Net Cash from Investing Activities</td>
              <td className="amount">
                <strong>{formatMoney(data.investingCashFlow)}</strong>
              </td>
            </tr>

            {/* Financing Activities */}
            <tr className="section-header">
              <td colSpan="2">
                <strong>CASH FLOWS FROM FINANCING ACTIVITIES</strong>
              </td>
            </tr>
            <tr>
              <td className="indent">Proceeds from Debt Issuance</td>
              <td className="amount">{formatMoney(data.debtProceeds)}</td>
            </tr>
            <tr>
              <td className="indent">Repayment of Debt</td>
              <td className="amount">({formatMoney(data.debtRepayment)})</td>
            </tr>
            <tr>
              <td className="indent">Proceeds from Equity Issuance</td>
              <td className="amount">{formatMoney(data.equityProceeds)}</td>
            </tr>
            <tr>
              <td className="indent">Dividend Payments</td>
              <td className="amount">({formatMoney(data.dividends)})</td>
            </tr>
            <tr className="subtotal">
              <td>Net Cash from Financing Activities</td>
              <td className="amount">
                <strong>{formatMoney(data.financingCashFlow)}</strong>
              </td>
            </tr>

            {/* Net Change in Cash */}
            <tr className="highlight">
              <td>
                <strong>NET CHANGE IN CASH</strong>
              </td>
              <td className="amount">
                <strong>
                  {formatMoney(
                    data.operatingCashFlow +
                      data.investingCashFlow +
                      data.financingCashFlow
                  )}
                </strong>
              </td>
            </tr>

            {/* Cash Reconciliation */}
            <tr>
              <td className="indent">Cash at Beginning of Period</td>
              <td className="amount">{formatMoney(data.beginningCash)}</td>
            </tr>
            <tr className="net-income">
              <td>
                <strong>Cash at End of Period</strong>
              </td>
              <td className="amount">
                <strong>{formatMoney(data.endingCash)}</strong>
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
