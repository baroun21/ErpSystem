import { useState } from 'react'
import FormModal from '../FormModal'

export default function AddHRRoleForm({ isOpen, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    roleName: '',
    description: '',
    permissions: [],
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const permissionOptions = [
    { id: 'view_employees', label: 'View Employees' },
    { id: 'create_employee', label: 'Create Employee' },
    { id: 'edit_employee', label: 'Edit Employee' },
    { id: 'delete_employee', label: 'Delete Employee' },
    { id: 'view_payroll', label: 'View Payroll' },
    { id: 'process_payroll', label: 'Process Payroll' },
    { id: 'manage_leaves', label: 'Manage Leaves' },
    { id: 'view_reports', label: 'View Reports' },
  ]

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handlePermissionChange = (permissionId) => {
    const newPermissions = formData.permissions.includes(permissionId)
      ? formData.permissions.filter(p => p !== permissionId)
      : [...formData.permissions, permissionId]
    setFormData({ ...formData, permissions: newPermissions })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      if (!formData.roleName) {
        throw new Error('Please fill in all required fields')
      }

      const response = await fetch('http://localhost:8081/api/hr/roles', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      if (!response.ok) throw new Error('Failed to create role')

      setFormData({
        roleName: '',
        description: '',
        permissions: [],
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
    <FormModal isOpen={isOpen} title="Add HR Role" onClose={onClose} onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}
      
      <div className="form-group">
        <label>Role Name *</label>
        <input
          type="text"
          name="roleName"
          value={formData.roleName}
          onChange={handleChange}
          placeholder="HR Manager"
          required
        />
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Role description and responsibilities..."
        />
      </div>

      <div className="form-group">
        <label>Permissions</label>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
          {permissionOptions.map(permission => (
            <div key={permission.id} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <input
                type="checkbox"
                id={permission.id}
                checked={formData.permissions.includes(permission.id)}
                onChange={() => handlePermissionChange(permission.id)}
              />
              <label htmlFor={permission.id} style={{ margin: 0, fontSize: '13px' }}>
                {permission.label}
              </label>
            </div>
          ))}
        </div>
      </div>
    </FormModal>
  )
}