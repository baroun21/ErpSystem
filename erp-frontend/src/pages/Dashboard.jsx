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
    <div className="space-y-8">
      <h2 className="text-2xl font-bold text-gray-900">Dashboard</h2>

      {/* Service Health */}
      <div className="grid grid-cols-1 gap-4">
        <div className={`p-6 rounded-lg shadow ${springStatus === 'up' ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'}`}>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Spring Boot Service</p>
              <p className="text-2xl font-bold mt-2">{springStatus === 'up' ? '✓ Running' : '✗ Down'}</p>
              <p className="text-xs text-gray-500 mt-2">Port 8081</p>
            </div>
            <div className={`w-12 h-12 rounded-full flex items-center justify-center ${springStatus === 'up' ? 'bg-green-200' : 'bg-red-200'}`}>
              {springStatus === 'up' ? '✓' : '✗'}
            </div>
          </div>
        </div>
      </div>

      {/* Quick Stats */}
      <div>
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Quick Stats</h3>
        <div className="grid grid-cols-4 gap-4">
          <div className="bg-white p-6 rounded-lg shadow">
            <p className="text-sm text-gray-600">Total Employees</p>
            <p className="text-3xl font-bold text-blue-600 mt-2">—</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow">
            <p className="text-sm text-gray-600">Active Departments</p>
            <p className="text-3xl font-bold text-green-600 mt-2">—</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow">
            <p className="text-sm text-gray-600">Pending Invoices</p>
            <p className="text-3xl font-bold text-orange-600 mt-2">—</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow">
            <p className="text-sm text-gray-600">Accounts Payable</p>
            <p className="text-3xl font-bold text-red-600 mt-2">—</p>
          </div>
        </div>
      </div>

      <button 
        onClick={checkServices}
        className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
      >
        Refresh Status
      </button>
    </div>
  )
}
