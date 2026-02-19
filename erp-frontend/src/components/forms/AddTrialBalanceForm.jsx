import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddTrialBalanceForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    account: '',
    reportDate: '',
    debitBalance: '',
    creditBalance: '',
    company: '',
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
      if (!formData.account || !formData.reportDate) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/trial-balance', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create trial balance record')

      setFormData({
        account: '',
        reportDate: '',
        debitBalance: '',
        creditBalance: '',
        company: '',
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
    <FormModal isOpen={isOpen} title="Add Trial Balance" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
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

      <div className="form-row">
        <div className="form-group">
          <label>Debit Balance</label>
          <input
            type="number"
            name="debitBalance"
            value={formData.debitBalance}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
        <div className="form-group">
          <label>Credit Balance</label>
          <input
            type="number"
            name="creditBalance"
            value={formData.creditBalance}
            onChange={handleChange}
            placeholder="0.00"
          />
        </div>
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
    </FormModal>
  )
}