import { useEffect, useState } from 'react'
import { invoiceService } from '../../services/api'
import { useApp } from '../../context/AppContext'

const InvoiceList = () => {
  const { setLoading, setError } = useApp()
  const [invoices, setInvoices] = useState([])
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    fetchInvoices()
  }, [])

  const fetchInvoices = async () => {
    try {
      setLoading(true)
      const response = await invoiceService.getAll()
      const payload = response.data
      const list = Array.isArray(payload)
        ? payload
        : payload.content || payload.data || payload.results || payload.invoices || []
      setInvoices(Array.isArray(list) ? list : [])
    } catch (error) {
      setError(`Failed to fetch invoices: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Invoices</h1>
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + New Invoice
          </button>
        </div>

        {showForm && (
          <div className="bg-white rounded-lg shadow-lg p-8 mb-8">
            <h2 className="text-2xl font-bold mb-6">Create Invoice</h2>
            <form className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <input
                  type="text"
                  placeholder="Invoice Number"
                  className="border rounded px-4 py-2"
                />
                <select className="border rounded px-4 py-2">
                  <option>Select Customer</option>
                </select>
                <input
                  type="date"
                  placeholder="Issue Date"
                  className="border rounded px-4 py-2"
                />
                <input
                  type="date"
                  placeholder="Due Date"
                  className="border rounded px-4 py-2"
                />
              </div>
              <div className="border-t pt-4">
                <h3 className="font-semibold mb-2">Line Items</h3>
                <div className="space-y-2">
                  <div className="grid grid-cols-2 gap-2">
                    <input
                      type="text"
                      placeholder="Description"
                      className="border rounded px-4 py-2"
                    />
                    <input
                      type="number"
                      placeholder="Amount"
                      className="border rounded px-4 py-2"
                    />
                  </div>
                </div>
              </div>
              <div className="flex space-x-4">
                <button
                  type="submit"
                  className="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700"
                >
                  Save
                </button>
                <button
                  type="button"
                  onClick={() => setShowForm(false)}
                  className="bg-gray-400 text-white px-6 py-2 rounded hover:bg-gray-500"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        <div className="bg-white rounded-lg shadow-lg overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase">Invoice #</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase">Customer</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase">Amount</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase">Status</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {invoices.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-6 py-4 text-center text-gray-600">
                    No invoices found. Create one to get started!
                  </td>
                </tr>
              ) : (
                invoices.map(inv => (
                  <tr key={inv.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{inv.invoiceNumber || 'N/A'}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">{inv.customer?.name || '-'}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">${Number(inv.totalAmount || 0).toFixed(2)}</td>
                    <td className="px-6 py-4 text-sm">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        inv.status === 'POSTED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                      }`}>
                        {inv.status || 'DRAFT'}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm space-x-2">
                      <button className="text-blue-600 hover:underline">View</button>
                      {inv.status !== 'POSTED' && <button className="text-green-600 hover:underline">Post</button>}
                      <button className="text-red-600 hover:underline">Delete</button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default InvoiceList
