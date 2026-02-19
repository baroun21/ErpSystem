import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddCostCenterForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    code: '',
    name: '',
    manager: '',
    budget: '',
    description: '',
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
      if (!formData.code || !formData.name) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/cost-centers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create cost center')

      setFormData({
        code: '',
        name: '',
        manager: '',
        budget: '',
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
    <FormModal isOpen={isOpen} title="Add Cost Center" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Code *</label>
          <input
            type="text"
            name="code"
            value={formData.code}
            onChange={handleChange}
            placeholder="CC001"
            required
          />
        </div>
        <div className="form-group">
          <label>Name *</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Marketing"
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label>Manager</label>
        <input
          type="text"
          name="manager"
          value={formData.manager}
          onChange={handleChange}
          placeholder="Manager Name"
        />
      </div>

      <div className="form-group">
        <label>Budget</label>
        <input
          type="number"
          name="budget"
          value={formData.budget}
          onChange={handleChange}
          placeholder="100000"
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Cost center description..."
        />
      </div>
    </FormModal>
  )
}