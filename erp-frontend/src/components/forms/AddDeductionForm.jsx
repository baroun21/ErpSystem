import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddDeductionForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    deductionType: '',
    amount: '',
    frequency: 'Monthly',
    startDate: '',
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
      if (!formData.employee || !formData.deductionType || !formData.amount) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/deductions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create deduction')

      setFormData({
        employee: '',
        deductionType: '',
        amount: '',
        frequency: 'Monthly',
        startDate: '',
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
    <FormModal isOpen={isOpen} title="Add Deduction" onClose={onClose} onSubmit={handleSubmit}>
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
        <label>Deduction Type *</label>
        <select name="deductionType" value={formData.deductionType} onChange={handleChange} required>
          <option value="">Select Type</option>
          <option value="Tax">Tax</option>
          <option value="Insurance">Insurance</option>
          <option value="Loan">Loan</option>
          <option value="Professional Fee">Professional Fee</option>
          <option value="Other">Other</option>
        </select>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Amount *</label>
          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            placeholder="1000"
            required
          />
        </div>
        <div className="form-group">
          <label>Frequency</label>
          <select name="frequency" value={formData.frequency} onChange={handleChange}>
            <option value="Monthly">Monthly</option>
            <option value="Quarterly">Quarterly</option>
            <option value="Annually">Annually</option>
            <option value="One-time">One-time</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Start Date</label>
        <input
          type="date"
          name="startDate"
          value={formData.startDate}
          onChange={handleChange}
        />
      </div>
    </FormModal>
  )
}