import './FormModal.css'

export default function FormModal({ isOpen, title, onClose, onSubmit, children }) {
  if (!isOpen) return null

  return (
    <>
      <div className="form-modal-overlay" onClick={onClose} />
      <div className="form-modal-container">
        <div className="form-modal-header">
          <h2>{title}</h2>
          <button className="form-modal-close" onClick={onClose}>×</button>
        </div>
        <form className="form-modal-form" onSubmit={onSubmit}>
          {children}
          <div className="form-modal-actions">
            <button type="button" className="form-btn-cancel" onClick={onClose}>Cancel</button>
            <button type="submit" className="form-btn-submit">Create</button>
          </div>
        </form>
      </div>
    </>
  )
}
