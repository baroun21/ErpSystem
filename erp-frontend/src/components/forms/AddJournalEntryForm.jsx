import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddJournalEntryForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    entryDate: '',
    description: '',
    reference: '',
    company: '',
    status: 'Draft',
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
      if (!formData.entryDate || !formData.description) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/journal-entries', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create journal entry')

      setFormData({
        entryDate: '',
        description: '',
        reference: '',
        company: '',
        status: 'Draft',
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
    <FormModal isOpen={isOpen} title="Add Journal Entry" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Entry Date *</label>
        <input
          type="date"
          name="entryDate"
          value={formData.entryDate}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label>Description *</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Journal entry description..."
          required
        />
      </div>

      <div className="form-group">
        <label>Reference</label>
        <input
          type="text"
          name="reference"
          value={formData.reference}
          onChange={handleChange}
          placeholder="Invoice #, Bill #, etc."
        />
      </div>

      <div className="form-group">
        <label>Company</label>
        <input
          type="text"
          name="company"
          value={formData.company}
          onChange={handleChange}
          placeholder="Company Name"
        />
      </div>

      <div className="form-group">
        <label>Status</label>
        <select name="status" value={formData.status} onChange={handleChange}>
          <option value="Draft">Draft</option>
          <option value="Posted">Posted</option>
          <option value="Reversed">Reversed</option>
        </select>
      </div>
    </FormModal>
  )
}