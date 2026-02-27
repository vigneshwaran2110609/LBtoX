import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './Callback.css'

export default function Callback() {
  const navigate = useNavigate()
  const [status, setStatus] = useState('processing')
  const [message, setMessage] = useState('')

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const code = params.get('code')
    const state = params.get('state')
    const letterboxdId = localStorage.getItem('lbtox_letterboxdId')

    if (!code) {
      setStatus('error')
      setMessage('Missing code in callback URL')
      return
    }
    if (!letterboxdId) {
      setStatus('error')
      setMessage('Missing saved Letterboxd ID in localStorage')
      return
    }

    async function postCallback() {
      try {
        const resp = await fetch(`/api/profiles/${letterboxdId}/twitter/callback`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ code, state })
        })

        if (resp.ok) {
          const data = await resp.json()
          setStatus('success')
          setMessage(`Connected as ${data.twitterHandle || data.twitterId}`)
          // cleanup
          localStorage.removeItem('lbtox_letterboxdId')
          // redirect back to home after a short delay
          setTimeout(() => { navigate('/') }, 2000)
        } else {
          const err = await resp.json()
          setStatus('error')
          setMessage(err.error || 'Failed to connect Twitter account')
        }
      } catch (e) {
        setStatus('error')
        setMessage(e.message)
      }
    }

    postCallback()
  }, [navigate])

  return (
    <div className="callback-container">
      <div className="callback-card">
        {status === 'processing' && <p>Finishing Twitter authorization...</p>}
        {status === 'success' && <p className="success">{message}</p>}
        {status === 'error' && <p className="error">{message}</p>}
      </div>
    </div>
  )
}
