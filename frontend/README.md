# LBtoX Frontend Setup Guide

This is the React frontend for the LBtoX application - a tool to connect your Letterboxd profile to Twitter and automatically tweet your reviews.

## Features

- 🎬 **Letterboxd Profile Integration** - Save your Letterboxd username
- 𝕏 **Twitter OAuth Authentication** - Securely connect your Twitter account
- ✨ **Tweet Management** - Compose and post tweets directly from the app
- 📊 **Status Dashboard** - View connection status and tweet history
- 🎨 **Modern UI** - Beautiful, responsive interface

## Prerequisites

- Node.js (v16+)
- npm or yarn
- React 18+
- Backend API running on `http://localhost:8081`

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Create a `.env` file (optional, for custom API endpoint):
```
VITE_API_URL=http://localhost:8081/api
```

## Development

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Building for Production

```bash
npm run build
```

The compiled files will be in the `dist` folder.

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── ProfileInput.jsx      # Letterboxd profile input form
│   │   ├── ProfileInput.css
│   │   ├── TwitterConnect.jsx    # Twitter connection button
│   │   ├── TwitterConnect.css
│   │   ├── StatusDashboard.jsx   # Tweet posting interface
│   │   └── StatusDashboard.css
│   ├── App.jsx                   # Main app component
│   ├── App.css
│   ├── index.css                 # Global styles
│   └── main.jsx                  # Entry point
├── index.html                    # HTML template
├── package.json
├── vite.config.js               # Vite configuration
└── .gitignore
```

## API Endpoints

The frontend communicates with the backend via these endpoints:

### Profile Management
- `POST /api/profiles` - Save Letterboxd profile
- `GET /api/profiles/{letterboxdId}/twitter/status` - Check Twitter connection status

### Twitter OAuth
- `GET /api/profiles/{letterboxdId}/twitter/authorize` - Get Twitter OAuth URL
- `POST /api/profiles/{letterboxdId}/twitter/callback` - Handle OAuth callback

### Tweeting
- `POST /api/profiles/{letterboxdId}/twitter/tweet` - Post a tweet

## Configuration

### Backend Configuration

Before running the frontend, ensure your backend is configured with Twitter API credentials in `application.properties`:

```properties
twitter.api.key=YOUR_TWITTER_API_KEY
twitter.api.secret=YOUR_TWITTER_API_SECRET
twitter.bearer.token=YOUR_TWITTER_BEARER_TOKEN
twitter.callback.url=http://localhost:3000/callback
```

### CORS

The backend should have CORS configured to allow requests from `http://localhost:3000`:

```java
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
```

## Twitter Developer Setup

1. Go to [Twitter Developer Portal](https://developer.twitter.com/en/portal/dashboard)
2. Create a new app or use an existing one
3. Set up OAuth 2.0 with:
   - **Callback URL**: `http://localhost:3000/callback`
   - **Website**: `http://localhost:3000`
4. Copy your API Key, API Secret, and Bearer Token
5. Add these to your backend `application.properties`

## Component Details

### ProfileInput Component
Handles Letterboxd profile username input and validation.

```jsx
<ProfileInput onSave={handleProfileSave} loading={loading} />
```

### TwitterConnect Component
Displays Twitter connection button and initiates OAuth flow.

```jsx
<TwitterConnect onConnect={handleTwitterConnect} loading={loading} />
```

### StatusDashboard Component
Shows connection status and tweet composition interface.

```jsx
<StatusDashboard 
  letterboxdId={letterboxdId}
  twitterHandle={twitterHandle}
  onDisconnect={handleDisconnect}
/>
```

## Styling

The app uses CSS with CSS variables for theming. You can customize colors by modifying `/src/index.css`:

```css
:root {
  --primary-color: #1da1f2;        /* Twitter Blue */
  --secondary-color: #14171a;      /* Dark Blue */
  --success-color: #17bf63;        /* Green */
  --error-color: #e0245e;          /* Red */
  --light-bg: #f7f9fa;            /* Light Gray */
  --border-color: #cfd9de;        /* Border Gray */
  --text-primary: #0f1419;        /* Dark Text */
  --text-secondary: #536471;      /* Gray Text */
}
```

## Error Handling

The app provides user-friendly error messages for:
- Invalid Letterboxd profile names
- Twitter OAuth failures
- Network errors
- Tweet length validation (max 280 characters)
- Missing credentials

## Future Enhancements

- [ ] Batch tweet posting from Letterboxd reviews
- [ ] Tweet scheduling
- [ ] Tweet analytics
- [ ] Multiple profile support
- [ ] Dark mode toggle
- [ ] Mobile app version

## Troubleshooting

### CORS Errors
Ensure backend has correct CORS configuration and is running on port 8081.

### API Connection Issues
- Check that backend is running: `curl http://localhost:8081/api/profiles`
- Verify proxy settings in `vite.config.js`

### Twitter OAuth Errors
- Verify Twitter API credentials in backend configuration
- Check OAuth callback URL matches in Twitter Developer Portal
- Ensure required scopes: `tweet.read tweet.write users.read offline.access`

## License

This project is part of the LBtoX application. See main project LICENSE for details.

## Support

For issues, questions, or contributions, please refer to the main project repository.
