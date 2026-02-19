import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddARAagingForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    customer: '',
    reportDate: '',
    current: '',
    thirtyDays: '',
    sixtyDays: '',
    ninetyDaysPlus: '',
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
      if (!formData.customer || !formData.reportDate) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/ar-aging', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create AR aging record')

      setFormData({
        customer: '',
        reportDate: '',
        current: '',
        thirtyDays: '',
        sixtyDays: '',
        ninetyDaysPlus: '',
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
    <FormModal isOpen={isOpen} title="Add AR Aging" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Customer *</label>
          <input
            type="text"
            name="customer"
            value={formData.customer}
            onChange={handleChange}
            placeholder="Customer Name"
            required
          />
        </div>
        <div className="form-group">
          <label>Report Date *</label>
          <input
            type="date"
            name="reportDate"
            value={formData.reportDate}
            onChange={handleChange}
            required
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Current (0-30 days)</label>
          <input
            type="number"
            name="current"
            value={formData.current}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
        <div className="form-group">
          <label>31-60 Days</label>
          <input
            type="number"
            name="thirtyDays"
            value={formData.thirtyDays}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>61-90 Days</label>
          <input
            type="number"
            name="sixtyDays"
            value={formData.sixtyDays}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
        <div className="form-group">
          <label>90+ Days</label>
          <input
            type="number"
            name="ninetyDaysPlus"
            value={formData.ninetyDaysPlus}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
      </div>
    </FormModal>
  )
}