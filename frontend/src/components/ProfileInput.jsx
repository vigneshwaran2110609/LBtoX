import { useState } from 'react'
import './ProfileInput.css'

export default function ProfileInput({ onSave, loading }) {
  const [letterboxdId, setLetterboxdId] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault()
    setError('')

    if (!letterboxdId.trim()) {
      setError('Please enter a Letterboxd profile name')
      return
    }

    if (letterboxdId.length < 2) {
      setError('Profile name must be at least 2 characters')
      return
    }

    onSave(letterboxdId.trim())
  }

  return (
    <form onSubmit={handleSubmit} className="profile-form">
      <div className="form-group">
        <label htmlFor="letterboxdId" className="form-label">
          🎬 Enter your Letterboxd Username
        </label>
        <input
          id="letterboxdId"
          type="text"
          value={letterboxdId}
          onChange={(e) => {
            setLetterboxdId(e.target.value)
            setError('')
          }}
          placeholder="e.g., filmfanatic"
          className="form-input"
          disabled={loading}
          autoFocus
        />
        {error && <p className="form-error">{error}</p>}
        <p className="form-hint">Your Letterboxd profile will be saved and linked to your Twitter account</p>
      </div>

      <button 
        type="submit" 
        className="btn btn-primary"
        disabled={loading}
      >
        {loading ? 'Saving...' : 'Continue'}
      </button>
    </form>
  )
}
