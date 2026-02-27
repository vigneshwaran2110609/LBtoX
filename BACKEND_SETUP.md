# Backend Twitter Integration Setup

## Required Twitter API Configuration

Add the following environment variables or properties to your `application.properties`:

```properties
# Twitter OAuth Configuration
twitter.api.key=${TWITTER_API_KEY:your_twitter_api_key}
twitter.api.secret=${TWITTER_API_SECRET:your_twitter_api_secret}
twitter.bearer.token=${TWITTER_BEARER_TOKEN:your_twitter_bearer_token}
twitter.callback.url=http://localhost:3000/callback
```

## Getting Twitter API Credentials

1. **Create Twitter Developer Account**
   - Go to https://developer.twitter.com/en/portal/dashboard
   - Log in or create account
   - Accept Developer Agreement

2. **Create an Application**
   - Click "Create app"
   - Enter app name, description, use case
   - Accept terms and verify email

3. **Get API Keys**
   - Navigate to "Keys and Tokens" tab
   - Copy:
     - API Key (Client ID)
     - API Secret (Client Secret)
     - Bearer Token

4. **Configure OAuth Settings**
   - Under "Authentication settings" → "OAuth 2.0"
   - Enable OAuth 2.0
   - Set Callback URL: `http://localhost:3000/callback`
   - Set Website: `http://localhost:3000`
   - Request email address: Yes
   - Type of App: Confidential client

5. **Request Elevated Access** (if needed)
   - Go to "Products" or "Settings"
   - Request "Elevated" or "Pro" access for Tweet posting
   - May require waiting for review

## Required API Permissions

Ensure your application has these scopes enabled:
- `tweet.read` - Read tweets
- `tweet.write` - Write tweets
- `users.read` - Read user information
- `offline.access` - Refresh token access

## Database Schema

The backend automatically creates the `twitter_credentials` table with:

```sql
CREATE TABLE twitter_credentials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    letterboxd_id VARCHAR(255) NOT NULL UNIQUE,
    twitter_id VARCHAR(255) NOT NULL,
    twitter_handle VARCHAR(255) NOT NULL,
    access_token TEXT NOT NULL,
    access_token_secret TEXT,
    bearer_token TEXT,
    oauth_state VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

This is automatically created by Hibernate with `spring.jpa.hibernate.ddl-auto=validate`

## API Endpoints Reference

### Save Letterboxd Profile
```
POST /api/profiles
Content-Type: application/json

{
  "letterboxdId": "username"
}

Response:
{
  "message": "Letterboxd profile saved successfully",
  "letterboxdId": "username",
  "status": "SAVED"
}
```

### Get Twitter Authorization URL
```
GET /api/profiles/{letterboxdId}/twitter/authorize

Response:
{
  "authUrl": "https://twitter.com/i/oauth2/authorize?...",
  "letterboxdId": "username",
  "message": "Redirect to this URL to authorize with Twitter"
}
```

### Handle OAuth Callback
```
POST /api/profiles/{letterboxdId}/twitter/callback?code={AUTH_CODE}

Response:
{
  "message": "Twitter account connected successfully",
  "letterboxdId": "username",
  "twitterHandle": "@handle",
  "twitterId": "123456789",
  "status": "CONNECTED"
}
```

### Check Twitter Connection Status
```
GET /api/profiles/{letterboxdId}/twitter/status

Response:
{
  "letterboxdId": "username",
  "isConnected": true,
  "twitterHandle": "@handle",
  "twitterId": "123456789",
  "canTweet": true
}
```

### Post a Tweet
```
POST /api/profiles/{letterboxdId}/twitter/tweet
Content-Type: application/json

{
  "message": "Just watched an amazing movie! #letterboxd"
}

Response:
{
  "message": "Tweet posted successfully",
  "tweetId": "1234567890",
  "letterboxdId": "username"
}
```

## Dependency Information

Added dependencies in pom.xml:
- **twitter4j** - Twitter API library
- **spring-security-oauth2-client** - OAuth 2.0 support
- **okhttp** - HTTP client for REST calls
- **lombok** - Reduce boilerplate code

## Running the Backend

1. Build the project:
```bash
mvn clean package
```

2. Run the application:
```bash
mvn spring-boot:run
```

Server starts on: `http://localhost:8081`

## CORS Configuration

The backend has CORS enabled for local development:
```java
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
```

For production, update the origins to match your frontend domain.

## Troubleshooting

### Twitter API Errors
- **401 Unauthorized**: Check API credentials in application.properties
- **403 Forbidden**: Ensure app has correct permissions/scopes
- **429 Too Many Requests**: Twitter rate limiting - implement retry logic

### OAuth Flow Issues
- **Invalid redirect_uri**: Ensure callback URL matches in Twitter Developer Settings
- **State validation failed**: Clear cookies and restart flow
- **Code exchange failed**: Certificate/SSL issues or expired authorization code

### Database Issues
- Check PostgreSQL connection string
- Verify username/password
- Ensure `lbtox` database exists

## Production Checklist

- [ ] Generate strong OAuth state tokens using secure random
- [ ] Use HTTPS/TLS for all connections
- [ ] Encrypt stored access tokens in database
- [ ] Implement token refresh logic
- [ ] Add request signing for API calls
- [ ] Rate limit API endpoints
- [ ] Log OAuth events for security audit
- [ ] Implement refresh token rotation
- [ ] Use environment variables, not hardcoded secrets
- [ ] Add monitoring and alerting

## Additional Resources

- [Twitter API v2 Documentation](https://developer.twitter.com/en/docs/twitter-api)
- [OAuth 2.0 Flow Guide](https://developer.twitter.com/en/docs/authentication/oauth-2-0)
- [Spring Security OAuth2 Guide](https://www.baeldung.com/spring-security-oauth)
- [OkHttp Documentation](https://square.github.io/okhttp/)
