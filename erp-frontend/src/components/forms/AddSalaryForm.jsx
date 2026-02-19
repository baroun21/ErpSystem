import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddSalaryForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    baseSalary: '',
    currency: 'USD',
    effectiveDate: '',
    salaryStructure: '',
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
      if (!formData.employee || !formData.baseSalary) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/salaries', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create salary record')

      setFormData({
        employee: '',
        baseSalary: '',
        currency: 'USD',
        effectiveDate: '',
        salaryStructure: '',
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
    <FormModal isOpen={isOpen} title="Add Salary" onClose={onClose} onSubmit={handleSubmit}>
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

      <div className="form-row">
        <div className="form-group">
          <label>Base Salary *</label>
          <input
            type="number"
            name="baseSalary"
            value={formData.baseSalary}
            onChange={handleChange}
            placeholder="50000"
            required
          />
        </div>
        <div className="form-group">
          <label>Currency</label>
          <select name="currency" value={formData.currency} onChange={handleChange}>
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="GBP">GBP</option>
            <option value="INR">INR</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Effective Date</label>
        <input
          type="date"
          name="effectiveDate"
          value={formData.effectiveDate}
          onChange={handleChange}
        />
      </div>

      <div className="form-group">
        <label>Salary Structure</label>
        <input
          type="text"
          name="salaryStructure"
          value={formData.salaryStructure}
          onChange={handleChange}
          placeholder="Fixed/Variable"
        />
      </div>
    </FormModal>
  )
}