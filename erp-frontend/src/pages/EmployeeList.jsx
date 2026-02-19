import { useState, useEffect } from 'react'
import { employeeService } from '../services/api'

export default function EmployeeList() {
  const [employees, setEmployees] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchEmployees()
  }, [])

  const fetchEmployees = async () => {
    try {
      setLoading(true)
      const response = await employeeService.getAll()
      // Handle various response structures
      let data = response.data
      if (data && typeof data === 'object' && !Array.isArray(data)) {
        // If response is an object with a data property
        data = data.content || data.data || data.results || data.employees || []
      }
      setEmployees(Array.isArray(data) ? data : [])
      setError(null)
    } catch (err) {
      setError('Failed to load employees. Make sure Spring Boot is running on port 8081.')
      setEmployees([])
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">HR Module - Employees</h2>
        <button className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700">
          Add Employee
        </button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {loading ? (
        <div className="text-center py-8">
          <p className="text-gray-600">Loading employees...</p>
        </div>
      ) : employees.length === 0 ? (
        <div className="bg-white rounded-lg shadow p-8 text-center">
          <p className="text-gray-600">No employees found</p>
          <p className="text-sm text-gray-500 mt-2">Add your first employee to get started</p>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Name</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Email</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Department</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Position</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-900">Actions</th>
              </tr>
            </thead>
            <tbody>
              {employees.map((emp) => (
                <tr key={emp.id} className="border-b hover:bg-gray-50">
                  <td className="px-6 py-4 text-sm text-gray-900">{emp.name || 'N/A'}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{emp.email || 'N/A'}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{emp.department || 'N/A'}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{emp.position || 'N/A'}</td>
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
        onClick={fetchEmployees}
        className="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700"
      >
        Refresh
      </button>
    </div>
  )
}
