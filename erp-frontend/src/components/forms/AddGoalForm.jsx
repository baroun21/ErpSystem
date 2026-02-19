import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddGoalForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    goalTitle: '',
    description: '',
    progress: '0',
    targetDate: '',
    status: 'Not Started',
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
      if (!formData.employee || !formData.goalTitle) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/goals', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create goal')

      setFormData({
        employee: '',
        goalTitle: '',
        description: '',
        progress: '0',
        targetDate: '',
        status: 'Not Started',
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
    <FormModal isOpen={isOpen} title="Add Goal" onClose={onClose} onSubmit={handleSubmit}>
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
        <label>Goal Title *</label>
        <input
          type="text"
          name="goalTitle"
          value={formData.goalTitle}
          onChange={handleChange}
          placeholder="Complete project X"
          required
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Detailed goal description..."
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Progress (%)</label>
          <input
            type="number"
            name="progress"
            min="0"
            max="100"
            value={formData.progress}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Status</label>
          <select name="status" value={formData.status} onChange={handleChange}>
            <option value="Not Started">Not Started</option>
            <option value="In Progress">In Progress</option>
            <option value="Completed">Completed</option>
            <option value="On Hold">On Hold</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Target Date</label>
        <input
          type="date"
          name="targetDate"
          value={formData.targetDate}
          onChange={handleChange}
        />
      </div>
    </FormModal>
  )
}