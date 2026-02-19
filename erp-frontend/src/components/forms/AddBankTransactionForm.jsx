import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddBankTransactionForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    bankAccount: '',
    transactionDate: '',
    type: 'Debit',
    amount: '',
    description: '',
    reference: '',
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
      if (!formData.bankAccount || !formData.amount) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/bank-transactions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create transaction')

      setFormData({
        bankAccount: '',
        transactionDate: '',
        type: 'Debit',
        amount: '',
        description: '',
        reference: '',
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
    <FormModal isOpen={isOpen} title="Add Bank Transaction" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Bank Account *</label>
          <input
            type="text"
            name="bankAccount"
            value={formData.bankAccount}
            onChange={handleChange}
            placeholder="Account #"
            required
          />
        </div>
        <div className="form-group">
          <label>Transaction Date</label>
          <input
            type="date"
            name="transactionDate"
            value={formData.transactionDate}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Type</label>
          <select name="type" value={formData.type} onChange={handleChange}>
            <option value="Debit">Debit</option>
            <option value="Credit">Credit</option>
            <option value="Cheque">Cheque</option>
            <option value="Transfer">Transfer</option>
          </select>
        </div>
        <div className="form-group">
          <label>Amount *</label>
          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            placeholder="0.00"
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label>Reference</label>
        <input
          type="text"
          name="reference"
          value={formData.reference}
          onChange={handleChange}
          placeholder="Check #, Transfer ID, etc."
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Transaction details..."
        />
      </div>
    </FormModal>
  )
}