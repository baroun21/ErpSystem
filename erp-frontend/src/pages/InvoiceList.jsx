import { useState, useEffect } from 'react'
import { invoiceService } from '../services/api'

export default function InvoiceList() {
  const [invoices, setInvoices] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchInvoices()
  }, [])

  const fetchInvoices = async () => {
    try {
      setLoading(true)
      const response = await invoiceService.getAll()
      // Handle various response structures
      let data = response.data
      if (data && typeof data === 'object' && !Array.isArray(data)) {
        // If response is an object with a data property
        data = data.content || data.data || data.results || data.invoices || []
      }
      setInvoices(Array.isArray(data) ? data : [])
      setError(null)
    } catch (err) {
      setError('Failed to load invoices. Make sure Spring Boot is running on port 8081.')
      setInvoices([])
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Finance Module - Invoices</h2>
        <button className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700">
          New Invoice
        </button>
      </div>

      {error && (
        <div className="bg-orange-50 border border-orange-200 text-orange-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {loading ? (
        <div className="text-center py-8">
          <p className="text-gray-600">Loading invoices...</p>
        </div>
      ) : invoices.length === 0 ? (
        <div className="bg-white rounded-lg shadow p-8 text-center">
          <p className="text-gray-600">No invoices found</p>
          <p className="text-sm text-gray-500 mt-2">Create your first invoice to get started</p>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Invoice #</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Customer</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Date</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Amount</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Status</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Actions</th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((inv) => (
                <tr key={inv.id} className="border-b hover:bg-gray-50">
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">{inv.invoiceNumber || 'N/A'}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{inv.customer?.name || 'N/A'}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{inv.issueDate ? new Date(inv.issueDate).toLocaleDateString() : 'N/A'}</td>
                  <td className="px-6 py-4 text-sm font-medium text-gray-900">${parseFloat(inv.totalAmount || 0).toFixed(2)}</td>
                  <td className="px-6 py-4 text-sm">
                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                      inv.status === 'POSTED' ? 'bg-green-100 text-green-800' :
                      inv.status === 'DRAFT' ? 'bg-blue-100 text-blue-800' :
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {inv.status || 'Unknown'}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm space-x-2">
                    <button className="text-blue-600 hover:text-blue-800">Edit</button>
                    <button className="text-red-600 hover:text-red-800">Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <button 
        onClick={fetchInvoices}
        className="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700"
      >
        Refresh
      </button>
    </div>
  )
}
