import { useState } from 'react'
import { billLineService } from '../../services/api'

export default function AddBillLineForm({ onSuccess, onCancel }) {
  const [formData, setFormData] = useState({
    billId: '',
    lineNumber: '',
    description: '',
    quantity: '',
    unitPrice: '',
    taxAmount: '',
    costCenterId: '',
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!formData.billId || !formData.lineNumber || !formData.quantity || !formData.unitPrice) {
      setError('Please fill in all required fields.')
      return
    }

    try {
      setLoading(true)
      await billLineService.create(formData)
      if (onSuccess) onSuccess()
      setFormData({
        billId: '',
        lineNumber: '',
        description: '',
        quantity: '',
        unitPrice: '',
        taxAmount: '',
        costCenterId: '',
      })
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="finance-form">
      <div className="form-group">
        <label htmlFor="billId">Bill ID *</label>
        <input
          id="billId"
          type="text"
          name="billId"
          value={formData.billId}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="lineNumber">Line Number *</label>
          <input
            id="lineNumber"
            type="number"
            name="lineNumber"
            value={formData.lineNumber}
            onChange={handleChange}
            min="1"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="quantity">Quantity *</label>
          <input
            id="quantity"
            type="number"
            name="quantity"
            value={formData.quantity}
            onChange={handleChange}
            step="0.01"
            min="0"
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="description">Description</label>
        <textarea
          id="description"
          name="description"
          value={formData.description}
          onChange={handleChange}
          rows="3"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="unitPrice">Unit Price *</label>
          <input
            id="unitPrice"
            type="number"
            name="unitPrice"
            value={formData.unitPrice}
            onChange={handleChange}
            step="0.01"
            min="0"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="taxAmount">Tax Amount</label>
          <input
            id="taxAmount"
            type="number"
            name="taxAmount"
            value={formData.taxAmount}
            onChange={handleChange}
            step="0.01"
            min="0"
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="costCenterId">Cost Center ID</label>
        <input
          id="costCenterId"
          type="text"
          name="costCenterId"
          value={formData.costCenterId}
          onChange={handleChange}
        />
      </div>

      {error && <div className="form-error">{error}</div>}

      <div className="form-actions">
        <button type="submit" disabled={loading} className="btn-primary">
          {loading ? 'Adding...' : 'Add Bill Line'}
        </button>
        {onCancel && (
          <button type="button" onClick={onCancel} className="btn-secondary">
            Cancel
          </button>
        )}
      </div>
    </form>
  )
}
