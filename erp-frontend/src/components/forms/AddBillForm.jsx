import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddBillForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    billNumber: '',
    supplier: '',
    billDate: '',
    dueDate: '',
    amount: '',
    status: 'Draft',
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
      if (!formData.billNumber || !formData.supplier) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/bills', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create bill')

      setFormData({
        billNumber: '',
        supplier: '',
        billDate: '',
        dueDate: '',
        amount: '',
        status: 'Draft',
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
    <FormModal isOpen={isOpen} title="Add Bill" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-row">
        <div className="form-group">
          <label>Bill Number *</label>
          <input
            type="text"
            name="billNumber"
            value={formData.billNumber}
            onChange={handleChange}
            placeholder="BILL-001"
            required
          />
        </div>
        <div className="form-group">
          <label>Supplier *</label>
          <input
            type="text"
            name="supplier"
            value={formData.supplier}
            onChange={handleChange}
            placeholder="Supplier Name"
            required
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Bill Date</label>
          <input
            type="date"
            name="billDate"
            value={formData.billDate}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Due Date</label>
          <input
            type="date"
            name="dueDate"
            value={formData.dueDate}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Amount</label>
          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            placeholder="3000"
          />
        </div>
        <div className="form-group">
          <label>Status</label>
          <select name="status" value={formData.status} onChange={handleChange}>
            <option value="Draft">Draft</option>
            <option value="Submitted">Submitted</option>
            <option value="Approved">Approved</option>
            <option value="Paid">Paid</option>
          </select>
        </div>
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Bill details..."
        />
      </div>
    </FormModal>
  )
}