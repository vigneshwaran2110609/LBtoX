import React from 'react'
import './App.css'

function Success() {
  return (
    <div className="app-container">
      <div className="app-content">
        <header className="header">
          <h1>🎬 LBtoX</h1>
        </header>

        <div className="main-card">
          <div className="profile-info" style={{ textAlign: 'center' }}>
            <p style={{ fontSize: '18px', fontWeight: '500' }}>
              ✅ Your Twitter account has been successfully connected!
            </p>
          </div>
        </div>

        <footer className="footer">
          <p>LBtoX v1.0 - Seamlessly integrate your film reviews with Twitter</p>
        </footer>
      </div>
    </div>
  )
}

export default Success