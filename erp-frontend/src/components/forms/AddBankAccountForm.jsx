import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddBankAccountForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    accountNumber: '',
    bankName: '',
    accountHolder: '',
    balance: '',
    currency: 'USD',
    accountType: 'Checking',
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
      if (!formData.accountNumber || !formData.bankName) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/bank-accounts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create bank account')

      setFormData({
        accountNumber: '',
        bankName: '',
        accountHolder: '',
        balance: '',
        currency: 'USD',
        accountType: 'Checking',
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
    <FormModal isOpen={isOpen} title="Add Bank Account" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Account Number *</label>
          <input
            type="text"
            name="accountNumber"
            value={formData.accountNumber}
            onChange={handleChange}
            placeholder="123456789"
            required
          />
        </div>
        <div className="form-group">
          <label>Bank Name *</label>
          <input
            type="text"
            name="bankName"
            value={formData.bankName}
            onChange={handleChange}
            placeholder="First National Bank"
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label>Account Holder</label>
        <input
          type="text"
          name="accountHolder"
          value={formData.accountHolder}
          onChange={handleChange}
          placeholder="Account Holder Name"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Balance</label>
          <input
            type="number"
            name="balance"
            value={formData.balance}
            onChange={handleChange}
            placeholder="10000"
          />
        </div>
        <div className="form-group">
          <label>Currency</label>
          <select name="currency" value={formData.currency} onChange={handleChange}>
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="GBP">GBP</option>
            <option value="INR">INR</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Account Type</label>
        <select name="accountType" value={formData.accountType} onChange={handleChange}>
          <option value="Checking">Checking</option>
          <option value="Savings">Savings</option>
          <option value="Money Market">Money Market</option>
          <option value="Business">Business</option>
        </select>
      </div>
    </FormModal>
  )
}