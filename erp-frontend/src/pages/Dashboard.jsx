import { useState, useEffect } from 'react'
import { healthService } from '../services/api'

export default function Dashboard() {
  const [springStatus, setSpringStatus] = useState('checking')

  useEffect(() => {
    checkServices()
  }, [])

  const checkServices = async () => {
    const results = await healthService.checkAll()
    const springResult = results.find(r => r.service === 'Spring Boot')
    setSpringStatus(springResult?.healthy ? 'up' : 'down')
  }

  return (
    <div className="space-y-12 p-8">
      {/* Page Header */}
      <div className="space-y-3">
        <h1 className="text-5xl font-black text-gray-900\">Command Center</h1>
        <p className="text-xl text-gray-600\">Your business at a glance</p>
      </div>

      {/* Status Bar */}
      <div className={`p-5 rounded-2xl flex items-center justify-between ${springStatus === 'up' ? 'bg-teal-50 border border-teal-200' : 'bg-red-50 border border-red-200'}`}>
        <div className="flex items-center gap-4">
          <div className={`w-3 h-3 rounded-full ${springStatus === 'up' ? 'bg-teal-500' : 'bg-red-500'}`}></div>
          <span className={`text-sm font-semibold ${springStatus === 'up' ? 'text-teal-900' : 'text-red-900'}`}>
            {springStatus === 'up' ? 'All systems operational' : 'System offline'}
          </span>
        </div>
        <button 
          onClick={checkServices}
          className="text-xs px-4 py-2 rounded-lg hover:bg-gray-100 text-gray-700 font-medium transition"
        >
          Refresh
        </button>
      </div>

      {/* Key Metrics Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {/* Cash Status */}
        <div className="group bg-white p-8 rounded-2xl border border-gray-200 hover:border-teal-300 hover:shadow-lg transition">
          <div className="flex items-center justify-between mb-5">
            <p className="text-sm font-semibold text-gray-600 uppercase tracking-wide">Cash Status</p>
            <span className="inline-block w-3 h-3 bg-teal-500 rounded-full"></span>
          </div>
          <p className="text-4xl font-black text-gray-900">$24.5K</p>
          <p className="text-xs text-gray-500 mt-3">Available balance</p>
        </div>

        {/* Sales Today */}
        <div className="group bg-white p-8 rounded-2xl border border-gray-200 hover:border-teal-300 hover:shadow-lg transition">
          <div className="flex items-center justify-between mb-5">
            <p className="text-sm font-semibold text-gray-600 uppercase tracking-wide">Sales Today</p>
            <span className="text-lg">📈</span>
          </div>
          <p className="text-4xl font-black text-gray-900">$3.2K</p>
          <p className="text-xs text-green-600 mt-3">↑ 12% from yesterday</p>
        </div>

        {/* Monthly Profit */}
        <div className="group bg-white p-8 rounded-2xl border border-gray-200 hover:border-teal-300 hover:shadow-lg transition">
          <div className="flex items-center justify-between mb-5">
            <p className="text-sm font-semibold text-gray-600 uppercase tracking-wide">Month Profit</p>
            <span className="text-lg">💰</span>
          </div>
          <p className="text-4xl font-black text-gray-900">$18.7K</p>
          <p className="text-xs text-gray-500 mt-3">Through Feb 21</p>
        </div>

        {/* Pending Actions */}
        <div className="group bg-white p-8 rounded-2xl border border-gray-200 hover:border-orange-300 hover:shadow-lg transition">
          <div className="flex items-center justify-between mb-5">
            <p className="text-sm font-semibold text-gray-600 uppercase tracking-wide">Pending</p>
            <span className="inline-block px-2 py-1 bg-orange-100 text-orange-700 rounded-lg text-xs font-bold">5</span>
          </div>
          <p className="text-4xl font-black text-orange-600">5 Actions</p>
          <p className="text-xs text-gray-500 mt-3">Need your attention</p>
        </div>
      </div>

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left Column - Overdue & Quick Actions */}
        <div className="lg:col-span-2 space-y-8">
          {/* Overdue Invoices */}
          <div className="bg-white p-10 rounded-2xl border border-gray-200">
            <div className="flex items-center justify-between mb-8">
              <div>
                <h3 className="text-lg font-bold text-gray-900">Invoices Needing Action</h3>
                <p className="text-sm text-gray-600 mt-2">Follow up with 3 customers</p>
              </div>
              <button className="px-5 py-3 bg-teal-50 text-teal-700 rounded-xl hover:bg-teal-100 text-sm font-semibold transition">
                View All
              </button>
            </div>

            <div className="space-y-3">
              {[
                { customer: 'Acme Corp', amount: '$2,450', daysOverdue: 5 },
                { customer: 'Tech Solutions', amount: '$1,200', daysOverdue: 3 },
                { customer: 'Global Services', amount: '$890', daysOverdue: 7 }
              ].map((item, idx) => (
                <div key={idx} className="flex items-center justify-between p-4 bg-red-50 rounded-xl border border-red-200">
                  <div>
                    <p className="font-semibold text-gray-900">{item.customer}</p>
                    <p className="text-xs text-gray-600 mt-1">{item.daysOverdue} days overdue</p>
                  </div>
                  <div className="text-right">
                    <p className="font-bold text-gray-900">{item.amount}</p>
                    <button className="text-xs text-teal-600 hover:text-teal-700 font-semibold mt-1">Send reminder →</button>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Daily Action List */}
          <div className="bg-white p-10 rounded-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-gray-900 mb-8">Today's Actions</h3>
            <div className="space-y-3">
              {[
                { action: 'Review Q1 budget proposal', priority: 'high' },
                { action: 'Approve 4 new expense reports', priority: 'medium' },
                { action: 'Reconcile bank transactions', priority: 'medium' },
                { action: 'Schedule payroll review', priority: 'low' }
              ].map((item, idx) => (
                <label key={idx} className="flex items-center gap-3 p-4 rounded-xl hover:bg-gray-50 cursor-pointer transition">
                  <input type="checkbox" className="w-5 h-5 accent-teal-600 rounded cursor-pointer" />
                  <div className="flex-1">
                    <p className="text-gray-900 font-medium">{item.action}</p>
                  </div>
                  <span className={`text-xs px-2 py-1 rounded-lg font-semibold ${
                    item.priority === 'high' ? 'bg-red-100 text-red-700' :
                    item.priority === 'medium' ? 'bg-yellow-100 text-yellow-700' :
                    'bg-gray-100 text-gray-700'
                  }`}>
                    {item.priority}
                  </span>
                </label>
              ))}
            </div>
          </div>
        </div>

        {/* Right Column - Top Products & Upcoming */}
        <div className="space-y-8">
          {/* Top Products */}
          <div className="bg-white p-10 rounded-2xl border border-gray-200">
            <h3 className="text-lg font-bold text-gray-900 mb-8">Top 3 Products</h3>
            <div className="space-y-5">
              {[
                { name: 'Product A', sales: '$12.5K' },
                { name: 'Product B', sales: '$8.2K' },
                { name: 'Product C', sales: '$5.1K' }
              ].map((product, idx) => (
                <div key={idx} className="flex items-center justify-between">
                  <div className="flex-1">
                    <p className="font-semibold text-gray-900">{product.name}</p>
                    <div className="w-full h-2 bg-gray-100 rounded-full mt-2">
                      <div 
                        className="h-full bg-teal-500 rounded-full" 
                        style={{width: `${100 - idx * 30}%`}}
                      ></div>
                    </div>
                  </div>
                  <p className="text-sm font-bold text-gray-900 ml-4">{product.sales}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Upcoming Payments */}
          <div className="bg-white p-10 rounded-2xl border border-gray-200 border-blue-200 bg-blue-50">
            <h3 className="text-lg font-bold text-gray-900 mb-8">Next Payments</h3>
            <div className="space-y-3">
              {[
                { vendor: 'Supplier X', amount: '$3,200', date: 'Mar 2' },
                { vendor: 'Supplier Y', amount: '$1,800', date: 'Mar 5' }
              ].map((payment, idx) => (
                <div key={idx} className="flex items-center justify-between p-3 bg-white rounded-xl">
                  <div>
                    <p className="text-sm font-semibold text-gray-900">{payment.vendor}</p>
                    <p className="text-xs text-gray-600">{payment.date}</p>
                  </div>
                  <p className="font-bold text-gray-900">{payment.amount}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
