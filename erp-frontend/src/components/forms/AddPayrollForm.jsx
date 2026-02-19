import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddPayrollForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    payrollPeriod: '',
    grossSalary: '',
    netSalary: '',
    status: 'Pending',
    paymentDate: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      if (!formData.employee || !formData.payrollPeriod) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/payroll', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create payroll')

      setFormData({
        employee: '',
        payrollPeriod: '',
        grossSalary: '',
        netSalary: '',
        status: 'Pending',
        paymentDate: '',
      })

      setLoading(false)
      onClose()
      onSuccess()
    } catch (err) {
      setError(err.message)
      setLoading(false)
    }
  }

  return (
    <FormModal isOpen={isOpen} title="Add Payroll" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Employee *</label>
        <input
          type="text"
          name="employee"
          value={formData.employee}
          onChange={handleChange}
          placeholder="John Doe"
          required
        />
      </div>

      <div className="form-group">
        <label>Payroll Period *</label>
        <input
          type="text"
          name="payrollPeriod"
          value={formData.payrollPeriod}
          onChange={handleChange}
          placeholder="January 2024"
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Gross Salary</label>
          <input
            type="number"
            name="grossSalary"
            value={formData.grossSalary}
            onChange={handleChange}
            placeholder="50000"
          />
        </div>
        <div className="form-group">
          <label>Net Salary</label>
          <input
            type="number"
            name="netSalary"
            value={formData.netSalary}
            onChange={handleChange}
            placeholder="42000"
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Status</label>
          <select name="status" value={formData.status} onChange={handleChange}>
            <option value="Pending">Pending</option>
            <option value="Processed">Processed</option>
            <option value="Paid">Paid</option>
          </select>
        </div>
        <div className="form-group">
          <label>Payment Date</label>
          <input
            type="date"
            name="paymentDate"
            value={formData.paymentDate}
            onChange={handleChange}
          />
        </div>
      </div>
    </FormModal>
  )
}