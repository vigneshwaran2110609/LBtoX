import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import './App.css'
import ProfileInput from './components/ProfileInput'
import Callback from './Callback'

function Home() {
  const [letterboxdId, setLetterboxdId] = useState('')
  const [profileSaved, setProfileSaved] = useState(false)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')

  const handleProfileSave = async (id) => {
    setLoading(true)
    setMessage('')

    try {
      const saveResponse = await fetch('http://localhost:8081/profile', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ letterboxdId: id })
      })

      if (!saveResponse.ok) {
        const error = await saveResponse.json()
        setMessage(`Error: ${error.error || 'Failed to save profile'}`)
        setLoading(false)
        return
      }

      setLetterboxdId(id)
      setProfileSaved(true)

      // 2️⃣ Request Twitter authorization URL
      window.location.href = `http://localhost:8081/${id}/twitter/authorize`;

      // Store for callback usage
      localStorage.setItem('lbtox_letterboxdId', id)

    } catch (error) {
      setMessage(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app-container">
      <div className="app-content">
        <header className="header">
          <h1>🎬 LBtoX</h1>
          <p>Connect your Letterboxd reviews to Twitter</p>
        </header>

        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}

        <div className="main-card">
          {!profileSaved ? (
            <ProfileInput onSave={handleProfileSave} loading={loading} />
          ) : (
            <div className="profile-info">
              <p className="profile-name">
                ✓ Profile: <strong>{letterboxdId}</strong>
              </p>
              <p>Redirecting to Twitter for authorization...</p>
            </div>
          )}
        </div>

        <footer className="footer">
          <p>LBtoX v1.0 - Seamlessly integrate your film reviews with Twitter</p>
        </footer>
      </div>
    </div>
  )
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/callback" element={<Callback />} />
    </Routes>
  )
}

export default App