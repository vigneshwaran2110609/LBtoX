# LBtoX Quick Start Checklist

Complete these steps to get LBtoX running on your local machine.

## ⏱️ Estimated Time: 30 minutes

---

## Step 1: Get Twitter API Credentials (10 minutes)

- [ ] Go to https://developer.twitter.com/en/portal/dashboard
- [ ] Log in to your Twitter Developer account (create one if needed)
- [ ] Create a new app or use an existing one
- [ ] Navigate to Keys and Tokens section
- [ ] Copy:
  - [ ] **API Key** (Client ID)
  - [ ] **API Secret** (Client Secret)  
  - [ ] **Bearer Token**
- [ ] Go to Authentication Settings → OAuth 2.0
- [ ] Set **Callback URL**: `http://localhost:3000/callback`
- [ ] Enable all these scopes:
  - [ ] `tweet.read`
  - [ ] `tweet.write`
  - [ ] `users.read`
  - [ ] `offline.access`

## Step 2: Configure Backend (5 minutes)

### Edit Backend Configuration
1. Open: `src/main/resources/application.properties`
2. Add these lines at the end:
```properties
twitter.api.key=YOUR_API_KEY_HERE
twitter.api.secret=YOUR_API_SECRET_HERE
twitter.bearer.token=YOUR_BEARER_TOKEN_HERE
twitter.callback.url=http://localhost:3000/callback
```
3. Replace with your actual credentials from Step 1
4. Save the file

- [ ] API Key added
- [ ] API Secret added
- [ ] Bearer Token added
- [ ] Config file saved

## Step 3: Start Backend (5 minutes)

### Option A: Using Terminal
```bash
# From project root directory
mvn clean package
mvn spring-boot:run
```

### Option B: Using IDE
- Open the project in IntelliJ IDEA or Eclipse
- Right-click on `LBtoXApplication.java`
- Select "Run 'LBtoXApplication.main()'"

**Wait for message**: `Started LBtoXApplication in X seconds`

- [ ] Backend running on port 8081
- [ ] No errors in console
- [ ] Database connected

## Step 4: Test Backend Connection (2 minutes)

Open a new terminal and run (make sure you call the real curl binary or use PowerShell correctly):

- **Windows CMD/PowerShell**:
  ```powershell
  curl.exe http://localhost:8081/api/profiles/vignesh_cinema/twitter/status
  # or
  Invoke-RestMethod http://localhost:8081/api/profiles/vignesh_cinema/twitter/status
  ```

- **If you are inside WSL2** the `localhost` address may not resolve to the Windows host. either start the backend from within WSL or use the Windows host IP:
  ```bash
  WIN_IP=$(grep nameserver /etc/resolv.conf | awk '{print $2}')
  curl --noproxy '*' http://$WIN_IP:8081/api/profiles/vignesh_cinema/twitter/status
  ```

Expected response:
```json
{
  "letterboxdId": "testuser",
  "isConnected": false,
  "canTweet": false
}
```

(PowerShell's default `curl` alias returns an object and prints nothing; use `curl.exe` or `Invoke-RestMethod`.)

- [ ] Backend responds to API requests

## Step 5: Install Frontend (5 minutes)

```bash
# Navigate to frontend folder
cd frontend

# Install dependencies
npm install
```

- [ ] Dependencies installed
- [ ] `node_modules` folder created

## Step 6: Start Frontend (3 minutes)

```bash
# From frontend directory
npm run dev
```

You should see:
```
  VITE v5.x.x  ready in XXX ms
  ➜  Local:   http://localhost:3000/
```

- [ ] Frontend running on port 3000
- [ ] No build errors

## Step 7: Complete Full Test Flow (5 minutes)

### In Browser
1. Open: `http://localhost:3000`

### Test Flow
1. [ ] See the LBtoX homepage
2. [ ] Enter a test Letterboxd username (e.g., "testuser")
3. [ ] Click "Continue"
4. [ ] See success message
5. [ ] Click "Connect to Twitter"
6. [ ] Get redirected to Twitter
7. [ ] Click "Authorize" (or log in if needed)
8. [ ] Get redirected back to http://localhost:3000
9. [ ] See connected Twitter handle
10. [ ] See tweet composition box
11. [ ] Type a test tweet (less than 280 characters)
12. [ ] Click "Tweet Now"
13. [ ] See success message with Tweet ID
14. [ ] ✅ Check your actual Twitter account - tweet should be there!

## ✅ Completion Checklist

Backend:
- [ ] Twitter credentials in application.properties
- [ ] Maven built successfully
- [ ] Spring Boot application running
- [ ] API responds to requests

Frontend:
- [ ] Node dependencies installed
- [ ] Vite dev server running
- [ ] Website loads at localhost:3000

Full Flow:
- [ ] Can enter Letterboxd profile
- [ ] Can connect to Twitter via OAuth
- [ ] Can post tweet
- [ ] Tweet appears on actual Twitter

---

## 🎉 Success!

If all checks pass, your LBtoX application is fully functional!

## 📚 Next Steps

### To Understand the Code
- Read `IMPLEMENTATION_SUMMARY.md` - What was built
- Read `SETUP_GUIDE.md` - Full architecture guide
- Read `BACKEND_SETUP.md` - Backend details
- Read `frontend/README.md` - Frontend details

### To Customize
- Edit `frontend/src/App.css` for styling
- Edit components in `frontend/src/components/`
- Add more API endpoints in backend controllers

### To Deploy
- See "Production Deployment" in SETUP_GUIDE.md
- Consider Heroku, AWS, or DigitalOcean for backend
- Consider Vercel, Netlify for frontend

---

## 🆘 Troubleshooting

### Port Already in Use
- **Port 8081 (Backend)**: 
  ```bash
  # Find and kill process on port 8081, or change in application.properties
  server.port=8082
  ```
- **Port 3000 (Frontend)**:
  ```bash
  npm run dev -- --port 5173
  ```

### Backend Not Connecting to Database
- Verify PostgreSQL is running
- Check connection string in `application.properties`
- Ensure database `lbtox` exists

### Twitter OAuth Not Working
- Verify Callback URL in Twitter Developer Settings matches exactly
- Check credentials are correct in `application.properties`
- Clear browser cookies and try again

### Frontend Can't Connect to Backend
- Ensure backend is running on port 8081
- Check network tab in browser DevTools
- Verify proxy settings in `frontend/vite.config.js`

### Tweet Not Posting
- Check tweet length (max 280 characters)
- Verify Twitter credentials are connected
- Check that Twitter account has tweet permissions

---

## 📞 Need Help?

1. Check the error message in console/browser
2. Look in the relevant README file
3. Check BACKEND_SETUP.md for detailed configs
4. Verify Twitter API credentials are correct

---

**Total Time**: ~30 minutes ⏱️

**Difficulty**: Beginner-Friendly ✨

**Result**: Fully functional LBtoX application 🎬→𝕏
