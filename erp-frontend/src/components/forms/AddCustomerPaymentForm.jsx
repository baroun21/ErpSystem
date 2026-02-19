import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddCustomerPaymentForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    customer: '',
    invoiceNumber: '',
    paymentDate: '',
    amount: '',
    paymentMethod: 'Check',
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
      if (!formData.customer || !formData.amount) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/customer-payments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create payment')

      setFormData({
        customer: '',
        invoiceNumber: '',
        paymentDate: '',
        amount: '',
        paymentMethod: 'Check',
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
    <FormModal isOpen={isOpen} title="Add Customer Payment" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Customer *</label>
          <input
            type="text"
            name="customer"
            value={formData.customer}
            onChange={handleChange}
            placeholder="Customer Name"
            required
          />
        </div>
        <div className="form-group">
          <label>Invoice Number</label>
          <input
            type="text"
            name="invoiceNumber"
            value={formData.invoiceNumber}
            onChange={handleChange}
            placeholder="INV-001"
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Payment Date</label>
          <input
            type="date"
            name="paymentDate"
            value={formData.paymentDate}
            onChange={handleChange}
          />
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
        <label>Payment Method</label>
        <select name="paymentMethod" value={formData.paymentMethod} onChange={handleChange}>
          <option value="Check">Check</option>
          <option value="Wire Transfer">Wire Transfer</option>
          <option value="Credit Card">Credit Card</option>
          <option value="ACH">ACH</option>
          <option value="Cash">Cash</option>
        </select>
      </div>

      <div className="form-group">
        <label>Reference</label>
        <input
          type="text"
          name="reference"
          value={formData.reference}
          onChange={handleChange}
          placeholder="Check #, Reference #, etc."
        />
      </div>
    </FormModal>
  )
}