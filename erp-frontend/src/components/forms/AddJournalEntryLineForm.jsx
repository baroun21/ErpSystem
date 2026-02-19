import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddJournalEntryLineForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    journalEntry: '',
    account: '',
    debit: '',
    credit: '',
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
      if (!formData.account) {
        throw new Error('Please select an account')
      }

      const response = await fetch('http://localhost:8081/api/journal-entry-lines', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create entry line')

      setFormData({
        journalEntry: '',
        account: '',
        debit: '',
        credit: '',
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
    <FormModal isOpen={isOpen} title="Add Journal Entry Line" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Journal Entry</label>
        <input
          type="text"
          name="journalEntry"
          value={formData.journalEntry}
          onChange={handleChange}
          placeholder="Entry Reference"
        />
      </div>

      <div className="form-group">
        <label>Account *</label>
        <input
          type="text"
          name="account"
          value={formData.account}
          onChange={handleChange}
          placeholder="Account Code/Name"
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Debit</label>
          <input
            type="number"
            name="debit"
            value={formData.debit}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
        <div className="form-group">
          <label>Credit</label>
          <input
            type="number"
            name="credit"
            value={formData.credit}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Entry line description..."
        />
      </div>
    </FormModal>
  )
}