import './TwitterConnect.css'

export default function TwitterConnect({ onConnect, loading }) {
  return (
    <div className="twitter-connect">
      <div className="twitter-icon">𝕏</div>
      <h2 className="twitter-title">Connect to Twitter</h2>
      <p className="twitter-description">
        Connect your Twitter account to enable automatic tweeting of your Letterboxd reviews
      </p>
      <button 
        onClick={onConnect}
        className="btn btn-twitter"
        disabled={loading}
      >
        {loading ? 'Connecting...' : '🔗 Connect to Twitter'}
      </button>
      <p className="twitter-note">
        You'll be redirected to Twitter to authorize this application
      </p>
    </div>
  )
}
