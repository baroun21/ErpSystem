import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddPositionForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    title: '',
    department: '',
    level: '',
    salaryRange: '',
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
      if (!formData.title) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/jobs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create position')

      setFormData({
        title: '',
        department: '',
        level: '',
        salaryRange: '',
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
    <FormModal isOpen={isOpen} title="Add Position" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Job Title *</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          placeholder="Senior Software Engineer"
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Department</label>
          <input
            type="text"
            name="department"
            value={formData.department}
            onChange={handleChange}
            placeholder="Engineering"
          />
        </div>
        <div className="form-group">
          <label>Level</label>
          <select name="level" value={formData.level} onChange={handleChange}>
            <option value="">Select Level</option>
            <option value="Junior">Junior</option>
            <option value="Mid">Mid</option>
            <option value="Senior">Senior</option>
            <option value="Lead">Lead</option>
            <option value="Manager">Manager</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Salary Range</label>
        <input
          type="text"
          name="salaryRange"
          value={formData.salaryRange}
          onChange={handleChange}
          placeholder="$70,000 - $100,000"
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Job description and responsibilities..."
        />
      </div>
    </FormModal>
  )
}