import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './CommandCenter.css';

const CommandCenter = () => {
  const [snapshot, setSnapshot] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [companyId, setCompanyId] = useState(1); // Default company ID
  const [autoRefresh, setAutoRefresh] = useState(true);

  const companyConfig = {
    1: 'Company A',
    2: 'Company B',
  };

  // Fetch snapshot from API
  const fetchSnapshot = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`/api/command-center/${companyId}`);
      setSnapshot(response.data);
      setError(null);
    } catch (err) {
      setError(`Failed to load dashboard: ${err.message}`);
      console.error('Error fetching snapshot:', err);
    } finally {
      setLoading(false);
    }
  };

  // Initial load
  useEffect(() => {
    fetchSnapshot();
  }, [companyId]);

  // Auto-refresh every 30 seconds
  useEffect(() => {
    if (!autoRefresh) return;
    const interval = setInterval(fetchSnapshot, 30000);
    return () => clearInterval(interval);
  }, [autoRefresh, companyId]);

  const handleRefresh = async () => {
    try {
      const response = await axios.post(`/api/command-center/${companyId}/refresh`);
      setSnapshot(response.data);
      setError(null);
    } catch (err) {
      setError(`Failed to refresh: ${err.message}`);
    }
  };

  if (loading && !snapshot) {
    return <div className="command-center-loading">Loading dashboard...</div>;
  }

  if (error) {
    return <div className="command-center-error">{error}</div>;
  }

  if (!snapshot) {
    return <div className="command-center-error">No data available</div>;
  }

  const cashStatus = snapshot.cashSnapshot;
  const salesData = snapshot.salesSummary;
  const profitData = snapshot.profitTrend;
  const topProducts = snapshot.topProducts || [];
  const overdueInvoices = snapshot.overdueInvoices || [];
  const upcomingPayments = snapshot.upcomingPayments || [];
  const riskAlerts = snapshot.riskAlerts || [];
  const dailyActions = snapshot.dailyActions || [];

  // Status color helper
  const getStatusColor = (status) => {
    switch (status?.toUpperCase()) {
      case 'GREEN': return '#4caf50';
      case 'YELLOW': return '#ff9800';
      case 'RED': return '#f44336';
      default: return '#9e9e9e';
    }
  };

  // Risk color helper
  const getRiskColor = (severity) => {
    switch (severity?.toUpperCase()) {
      case 'CRITICAL': return '#f44336';
      case 'HIGH': return '#ff9800';
      case 'MEDIUM': return '#ffc107';
      case 'LOW': return '#4caf50';
      default: return '#9e9e9e';
    }
  };

  return (
    <div className="command-center-container">
      <header className="command-center-header">
        <h1>Command Center Dashboard</h1>
        <div className="header-controls">
          <select 
            value={companyId} 
            onChange={(e) => setCompanyId(parseInt(e.target.value))}
            className="company-selector"
          >
            {Object.entries(companyConfig).map(([id, name]) => (
              <option key={id} value={id}>{name}</option>
            ))}
          </select>
          <button onClick={handleRefresh} className="refresh-btn">🔄 Refresh</button>
          <label className="auto-refresh-toggle">
            <input 
              type="checkbox" 
              checked={autoRefresh} 
              onChange={(e) => setAutoRefresh(e.target.checked)}
            />
            Auto Refresh (30s)
          </label>
        </div>
      </header>

      <main className="command-center-main">
        {/* Cash Status Widget */}
        <section className="widget cash-status-widget">
          <h2>Cash Status</h2>
          <div 
            className="status-indicator" 
            style={{ backgroundColor: getStatusColor(cashStatus?.status) }}
          >
            {cashStatus?.status || 'N/A'}
          </div>
          <div className="cash-metrics">
            <div className="metric">
              <span className="metric-label">Total Cash</span>
              <span className="metric-value">${cashStatus?.totalCash?.toFixed(2) || '0.00'}</span>
            </div>
            <div className="metric">
              <span className="metric-label">Reconciled Cash</span>
              <span className="metric-value">${cashStatus?.reconciledCash?.toFixed(2) || '0.00'}</span>
            </div>
            <div className="metric">
              <span className="metric-label">30-Day Cash Runway</span>
              <span className="metric-value">{cashStatus?.cashRunwayDays?.toFixed(1) || '0'} days</span>
            </div>
          </div>
          <div className="cash-flow">
            <div className="inflow">
              <span>Expected Inflow</span>
              <strong>${cashStatus?.expectedInflow?.toFixed(2) || '0.00'}</strong>
            </div>
            <div className="outflow">
              <span>Expected Outflow</span>
              <strong>${cashStatus?.expectedOutflow?.toFixed(2) || '0.00'}</strong>
            </div>
          </div>
        </section>

        {/* Sales Summary Widget */}
        <section className="widget sales-widget">
          <h2>Sales Today</h2>
          <div className="sales-metrics">
            <div className="big-metric">
              <span className="metric-label">Sales Today</span>
              <span className="metric-value">${salesData?.salesToday?.toFixed(2) || '0.00'}</span>
            </div>
            <div className="metric">
              <span className="metric-label">Orders Today</span>
              <span className="metric-value">{salesData?.ordersToday || 0}</span>
            </div>
            <div className="metric">
              <span className="metric-label">Avg Order Value</span>
              <span className="metric-value">${salesData?.avgOrderValue?.toFixed(2) || '0.00'}</span>
            </div>
          </div>
        </section>

        {/* Profit Trend Widget */}
        <section className="widget profit-widget">
          <h2>Profit Trend</h2>
          <div className="profit-metrics">
            <div className="metric">
              <span className="metric-label">Current Month Profit</span>
              <span className="metric-value">${profitData?.currentMonthProfit?.toFixed(2) || '0.00'}</span>
            </div>
            <div className={`metric ${profitData?.profitChangePercent >= 0 ? 'positive' : 'negative'}`}>
              <span className="metric-label">Change vs Last Month</span>
              <span className="metric-value">{profitData?.profitChangePercent >= 0 ? '+' : ''}{profitData?.profitChangePercent?.toFixed(1) || '0'}%</span>
            </div>
            <div className="metric">
              <span className="metric-label">Gross Margin</span>
              <span className="metric-value">{profitData?.grossMarginPercent?.toFixed(1) || '0'}%</span>
            </div>
          </div>
        </section>

        {/* Top Products Widget */}
        <section className="widget top-products-widget">
          <h2>Top Products by Revenue</h2>
          {topProducts.length > 0 ? (
            <div className="product-list">
              {topProducts.slice(0, 6).map((product, idx) => (
                <div key={idx} className="product-item">
                  <div className="product-name">{product.productName}</div>
                  <div className="product-stats">
                    <span>{product.totalQuantitySold} units</span>
                    <strong>${product.totalRevenue?.toFixed(2)}</strong>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data">No product data available</p>
          )}
        </section>

        {/* Overdue Invoices Widget */}
        <section className="widget overdue-widget">
          <h2>Overdue Invoices ({overdueInvoices.length})</h2>
          {overdueInvoices.length > 0 ? (
            <div className="items-list">
              {overdueInvoices.slice(0, 5).map((inv, idx) => (
                <div key={idx} className="item danger">
                  <div className="item-header">
                    <span className="item-id">INV-{inv.invoiceNumber}</span>
                    <span className="item-amount">${inv.amount?.toFixed(2)}</span>
                  </div>
                  <div className="item-details">
                    <span>{inv.customerName}</span>
                    <span className="overdue-days">{inv.daysPastDue} days past due</span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data">No overdue invoices</p>
          )}
        </section>

        {/* Upcoming Payments Widget */}
        <section className="widget upcoming-widget">
          <h2>Upcoming Supplier Payments ({upcomingPayments.length})</h2>
          {upcomingPayments.length > 0 ? (
            <div className="items-list">
              {upcomingPayments.slice(0, 5).map((pmt, idx) => (
                <div key={idx} className="item warning">
                  <div className="item-header">
                    <span className="item-id">BILL-{pmt.billNumber}</span>
                    <span className="item-amount">${pmt.amount?.toFixed(2)}</span>
                  </div>
                  <div className="item-details">
                    <span>{pmt.supplierName}</span>
                    <span className="due-days">Due in {pmt.daysUntilDue} days</span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data">No upcoming payments</p>
          )}
        </section>

        {/* Risk Alerts Widget */}
        <section className="widget risks-widget">
          <h2>Risk Alerts ({riskAlerts.length})</h2>
          {riskAlerts.length > 0 ? (
            <div className="alerts-list">
              {riskAlerts.slice(0, 4).map((alert, idx) => (
                <div 
                  key={idx} 
                  className="alert-item" 
                  style={{ borderLeftColor: getRiskColor(alert.severity) }}
                >
                  <div className="alert-header">
                    <span className="alert-type">{alert.type}</span>
                    <span className="alert-severity" style={{ backgroundColor: getRiskColor(alert.severity) }}>
                      {alert.severity}
                    </span>
                  </div>
                  <div className="alert-message">{alert.message}</div>
                  {alert.affectedEntity && (
                    <div className="alert-details">Entity: {alert.affectedEntity}</div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data">No active risks</p>
          )}
        </section>

        {/* Daily Actions Widget */}
        <section className="widget actions-widget">
          <h2>Daily Actions ({dailyActions.length})</h2>
          {dailyActions.length > 0 ? (
            <div className="actions-list">
              {dailyActions.slice(0, 5).map((action, idx) => (
                <div key={idx} className={`action-item ${action.status?.toLowerCase()}`}>
                  <div className="action-header">
                    <span className="action-type">{action.actionType}</span>
                    <span className="action-status">{action.status}</span>
                  </div>
                  <div className="action-description">{action.description}</div>
                  {action.dueDate && (
                    <div className="action-due">Due: {new Date(action.dueDate).toLocaleDateString()}</div>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data">No pending actions</p>
          )}
        </section>
      </main>
    </div>
  );
};

export default CommandCenter;
