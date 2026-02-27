# LBtoX - Letterboxd to Twitter Integration

A full-stack web application that connects your Letterboxd film reviews with Twitter, enabling automatic tweeting of your movie experiences.

## 🎬 Overview

LBtoX is a powerful integration platform that combines:
- **Letterboxd** - Your personal film database and social network
- **Twitter** - Real-time sharing with your audience

With a clean, modern interface and secure OAuth authentication.

## 📁 Project Structure

```
LBtoX/
├── src/                          # Java Backend (Spring Boot)
│   ├── main/
│   │   ├── java/com/example/LBtoX/
│   │   │   ├── LBtoXApplication.java
│   │   │   ├── controllers/
│   │   │   │   └── ProfileController.java      # NEW
│   │   │   ├── models/
│   │   │   │   ├── LetterboxdProfile.java
│   │   │   │   └── TwitterCredential.java      # NEW
│   │   │   ├── repositories/
│   │   │   │   ├── ProfileProcessingRepository.java
│   │   │   │   └── TwitterCredentialRepository.java  # NEW
│   │   │   ├── services/
│   │   │   │   ├── RssFeedMessageService.java
│   │   │   │   └── TwitterOAuthService.java    # NEW
│   │   │   ├── messaging/
│   │   │   ├── scheduler/
│   │   │   └── utils/
│   │   └── resources/
│   │       └── application.properties          # UPDATED
│   └── test/
│
├── frontend/                     # React Frontend (Vite)
│   ├── src/
│   │   ├── components/
│   │   │   ├── ProfileInput.jsx      # NEW
│   │   │   ├── ProfileInput.css      # NEW
│   │   │   ├── TwitterConnect.jsx    # NEW
│   │   │   ├── TwitterConnect.css    # NEW
│   │   │   ├── StatusDashboard.jsx   # NEW
│   │   │   └── StatusDashboard.css   # NEW
│   │   ├── App.jsx                   # NEW
│   │   ├── App.css                   # NEW
│   │   ├── index.css                 # NEW
│   │   └── main.jsx                  # NEW
│   ├── index.html                    # NEW
│   ├── package.json                  # NEW
│   ├── vite.config.js               # NEW
│   ├── .gitignore                    # NEW
│   └── README.md                     # NEW
│
├── pom.xml                          # UPDATED (added Twitter deps)
├── BACKEND_SETUP.md                 # NEW - Backend configuration guide
├── README.md                         # Project overview (this file)
└── HELP.md
```

## 🚀 Quick Start

### Prerequisites
- Java 21+ (for backend)
- Node.js 16+ (for frontend)
- PostgreSQL 12+ (already configured)
- Twitter Developer Account with API credentials

### 1. Backend Setup

#### Step 1: Get Twitter API Credentials
See [BACKEND_SETUP.md](BACKEND_SETUP.md) for detailed instructions on:
- Creating Twitter Developer account
- Getting API keys and secrets
- Configuring OAuth settings

#### Step 2: Configure Backend
Update `src/main/resources/application.properties`:
```properties
twitter.api.key=your_api_key
twitter.api.secret=your_api_secret
twitter.bearer.token=your_bearer_token
twitter.callback.url=http://localhost:3000/callback
```

#### Step 3: Start Backend
```bash
# Navigate to project root
cd LBtoX

# Build project
mvn clean package

# Run application (port 8081)
mvn spring-boot:run
```

### 2. Frontend Setup

#### Step 1: Install Dependencies
```bash
cd frontend
npm install
```

#### Step 2: Start Development Server
```bash
npm run dev
```

Frontend will be available at: `http://localhost:3000`

## 🔄 User Flow

1. **Visit the App**
   - User opens `http://localhost:3000`

2. **Enter Letterboxd Profile**
   - Input Letterboxd username
   - Profile is saved to database

3. **Connect Twitter Account**
   - Click "Connect to Twitter"
   - Redirected to Twitter OAuth
   - User authorizes the app
   - Returns to dashboard with connection confirmed

4. **Post Tweets**
   - Compose tweet in dashboard
   - Click "Tweet Now"
   - Tweet is posted to user's Twitter account

## 📊 Technical Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.5.4
- **Database**: PostgreSQL
- **Message Queue**: ActiveMQ
- **Authentication**: OAuth 2.0
- **HTTP Client**: OkHttp
- **API Style**: RESTful

### Frontend (React + Vite)
- **Framework**: React 18
- **Build Tool**: Vite 5
- **HTTP Client**: Axios
- **Styling**: CSS with CSS Variables

### Authentication Flow
```
User → Frontend → Backend Twitter OAuth Service → Twitter API
                 ↓
           Store Access Token
           Store User Credentials
           ↓
       Backend Ready to Tweet
```

## 🔐 Security Features

- **OAuth 2.0**: Secure Twitter authorization without storing passwords
- **CSRF Protection**: State token validation
- **Encrypted Storage**: API tokens encrypted in database
- **CORS**: Configured for localhost development
- **Secure Headers**: Standard security headers

## 📝 API Endpoints

All endpoints are prefixed with `/api`

### Profiles
- `POST /profiles` - Save Letterboxd profile
- `GET /profiles/{letterboxdId}/twitter/status` - Check Twitter connection

### Twitter OAuth
- `GET /profiles/{letterboxdId}/twitter/authorize` - Start OAuth flow
- `POST /profiles/{letterboxdId}/twitter/callback` - Handle OAuth callback

### Tweeting
- `POST /profiles/{letterboxdId}/twitter/tweet` - Post a tweet

Complete API documentation in [BACKEND_SETUP.md](BACKEND_SETUP.md)

## 🎨 UI Components

### ProfileInput Component
- Text input for Letterboxd username
- Form validation
- Loading state

### TwitterConnect Component
- Beautiful button to initiate OAuth
- Explanation text
- Loading state

### StatusDashboard Component
- Connection status indicator
- Tweet composition box
- Character counter (280 char limit)
- Success/error messages
- Disconnect option

## 🗄️ Database Schema

### twitter_credentials table
```sql
id               BIGINT PRIMARY KEY
letterboxd_id    VARCHAR(255) UNIQUE NOT NULL
twitter_id       VARCHAR(255) NOT NULL
twitter_handle   VARCHAR(255) NOT NULL
access_token     TEXT NOT NULL
bearer_token     TEXT
created_at       TIMESTAMP NOT NULL
updated_at       TIMESTAMP
is_active        BOOLEAN DEFAULT TRUE
```

## 🔧 Configuration Files

### Backend: application.properties
```properties
server.port=8081
spring.datasource.url=postgresql://...
twitter.api.key=...
twitter.api.secret=...
twitter.bearer.token=...
twitter.callback.url=http://localhost:3000/callback
```

### Frontend: vite.config.js
```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    }
  }
}
```

## 🧪 Testing

### Backend Testing
```bash
mvn test
```

### Frontend Testing
```bash
cd frontend
npm run test
```

Build for production:
```bash
npm run build
```

## 📦 Production Deployment

### Backend
1. Build JAR: `mvn clean package`
2. Deploy to server (Heroku, AWS, etc.)
3. Set environment variables for secrets
4. Configure CORS for production domain

### Frontend
1. Build static files: `npm run build`
2. Deploy `dist/` folder to CDN or static server
3. Update API endpoints for production
4. Configure environment variables

## 🚨 Troubleshooting

### Backend Issues
- Port 8081 in use: Change `server.port` in properties
- Database connection: Check PostgreSQL credentials
- Twitter API errors: Verify credentials in properties

### Frontend Issues
- Port 3000 in use: `npm run dev -- --host localhost --port 5173`
- API connection: Check backend is running on 8081
- CORS errors: Verify backend CORS configuration

### Twitter OAuth Issues
- See detailed troubleshooting in [BACKEND_SETUP.md](BACKEND_SETUP.md)

## 📚 Documentation

- **[BACKEND_SETUP.md](BACKEND_SETUP.md)** - Complete backend configuration guide
- **[frontend/README.md](frontend/README.md)** - Frontend development guide
- **Twitter OAuth 2.0**: https://developer.twitter.com/en/docs/authentication/oauth-2-0

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 🎯 Future Roadmap

- [ ] Batch import of Letterboxd reviews
- [ ] Automatic tweeting on new reviews
- [ ] Tweet scheduling
- [ ] Analytics and statistics
- [ ] Multiple Twitter accounts per profile
- [ ] Mobile app version
- [ ] Dark mode
- [ ] Rate limiting and caching
- [ ] Webhook support
- [ ] Custom tweet templates

## 💬 Support

For issues or questions:
1. Check [BACKEND_SETUP.md](BACKEND_SETUP.md) for configuration help
2. Review frontend [README.md](frontend/README.md)
3. Check Twitter API documentation
4. Open an issue on the repository

## 👤 Authors

- **Your Name** - Initial work

## 🙏 Acknowledgments

- Spring Boot community
- React community
- Twitter API team
- Letterboxd for the awesome film database

---

**Last Updated**: February 2026
**Version**: 1.0.0
**Status**: In Development
