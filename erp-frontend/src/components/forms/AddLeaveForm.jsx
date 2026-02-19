import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddLeaveForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    employee: '',
    leaveType: 'Vacation',
    fromDate: '',
    toDate: '',
    reason: '',
    status: 'Pending',
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
      if (!formData.employee || !formData.fromDate || !formData.toDate) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/leave-requests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create leave request')

      setFormData({
        employee: '',
        leaveType: 'Vacation',
        fromDate: '',
        toDate: '',
        reason: '',
        status: 'Pending',
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
    <FormModal isOpen={isOpen} title="Request Leave" onClose={onClose} onSubmit={handleSubmit}>
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
        <label>Leave Type</label>
        <select name="leaveType" value={formData.leaveType} onChange={handleChange}>
          <option value="Vacation">Vacation</option>
          <option value="Sick">Sick Leave</option>
          <option value="Personal">Personal</option>
          <option value="Maternity">Maternity</option>
          <option value="Paternity">Paternity</option>
        </select>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>From Date *</label>
          <input
            type="date"
            name="fromDate"
            value={formData.fromDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>To Date *</label>
          <input
            type="date"
            name="toDate"
            value={formData.toDate}
            onChange={handleChange}
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label>Reason</label>
        <textarea
          name="reason"
          value={formData.reason}
          onChange={handleChange}
          placeholder="Reason for leave..."
        />
      </div>

      <div className="form-group">
        <label>Status</label>
        <select name="status" value={formData.status} onChange={handleChange}>
          <option value="Pending">Pending</option>
          <option value="Approved">Approved</option>
          <option value="Rejected">Rejected</option>
        </select>
      </div>
    </FormModal>
  )
}