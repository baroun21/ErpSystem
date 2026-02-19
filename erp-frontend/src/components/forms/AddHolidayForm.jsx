import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddHolidayForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    name: '',
    date: '',
    holidayType: 'National',
    isPaid: true,
    description: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value
    setFormData({ ...formData, [e.target.name]: value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      if (!formData.name || !formData.date) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/holidays', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create holiday')

      setFormData({
        name: '',
        date: '',
        holidayType: 'National',
        isPaid: true,
        description: '',
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
    <FormModal isOpen={isOpen} title="Add Holiday" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Holiday Name *</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          placeholder="Christmas"
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Date *</label>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Type</label>
          <select name="holidayType" value={formData.holidayType} onChange={handleChange}>
            <option value="National">National</option>
            <option value="Regional">Regional</option>
            <option value="Company">Company</option>
            <option value="Optional">Optional</option>
          </select>
        </div>
      </div>

      <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <input
          type="checkbox"
          name="isPaid"
          checked={formData.isPaid}
          onChange={handleChange}
          id="isPaid"
        />
        <label htmlFor="isPaid" style={{ margin: 0 }}>Paid Holiday</label>
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Holiday description..."
        />
      </div>
    </FormModal>
  )
}