import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddReviewForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    reviewer: '',
    rating: '5',
    feedback: '',
    reviewDate: '',
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
      if (!formData.employee || !formData.reviewer) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/reviews', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create review')

      setFormData({
        employee: '',
        reviewer: '',
        rating: '5',
        feedback: '',
        reviewDate: '',
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
    <FormModal isOpen={isOpen} title="Add Performance Review" onClose={onClose} onSubmit={handleSubmit}>
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
        <label>Reviewer *</label>
        <input
          type="text"
          name="reviewer"
          value={formData.reviewer}
          onChange={handleChange}
          placeholder="Manager Name"
          required
        />
      </div>

      <div className="form-group">
        <label>Rating</label>
        <select name="rating" value={formData.rating} onChange={handleChange}>
          <option value="1">1 - Poor</option>
          <option value="2">2 - Below Average</option>
          <option value="3">3 - Average</option>
          <option value="4">4 - Good</option>
          <option value="5">5 - Excellent</option>
        </select>
      </div>

      <div className="form-group">
        <label>Feedback</label>
        <textarea
          name="feedback"
          value={formData.feedback}
          onChange={handleChange}
          placeholder="Performance feedback and comments..."
        />
      </div>

      <div className="form-group">
        <label>Review Date</label>
        <input
          type="date"
          name="reviewDate"
          value={formData.reviewDate}
          onChange={handleChange}
        />
      </div>
    </FormModal>
  )
}