# LevelUp Academy Frontend

Modern authentication UI for LevelUp Academy with full backend integration.

## 📁 Files

- **`index.html`** - Sign up page (entry point)
- **`signup.html`** - Sign up page (same as index.html)
- **`login.html`** - Login page
- **`dashboard.html`** - Protected dashboard page (redirects if not authenticated)

## 🚀 Features

### Sign Up Page (`index.html` / `signup.html`)
- Fields: Username, First Name, Last Name, Email, Password, Confirm Password
- Client-side validation (min 3 chars username, 8+ chars password, email format, password match)
- POST to `/api/v1/player/register`
- Redirects to `login.html` on success

### Login Page (`login.html`)
- Fields: Username, Password
- POST to `/api/v1/auth/login`
- Receives JWT token and user role
- Stores token in `localStorage` as:
  - `authToken` - JWT token
  - `userRole` - User role (PLAYER, ADMIN, MODERATOR, etc.)
  - `tokenType` - "Bearer"
- Redirects to `dashboard.html` on success

### Dashboard Page (`dashboard.html`)
- Protected route (redirects to login if no token)
- Displays user role and JWT token
- Logout button (clears localStorage and redirects to login)

## 🔧 Backend Integration

### Endpoints Used

| Endpoint | Method | Request Body | Response |
|----------|--------|--------------|----------|
| `/api/v1/player/register` | POST | `{ username, firstName, lastName, email, password }` | `{ message }` |
| `/api/v1/auth/login` | POST | `{ username, password }` | `{ token, role, expiresIn, tokenType }` |

### CORS Configuration

Your backend already has CORS configured for:
- `http://localhost:8080`
- `http://localhost:8081`
- `http://localhost:5173`
- External domains

If serving the frontend from a different port, add it to `ConfigurationSecurity.java`:

```java
cfg.setAllowedOrigins(List.of(
    "http://localhost:8080",
    "http://localhost:3000",  // Add your port here
    // ... other origins
));
```

## 🎯 How to Use

### Option 1: Serve with Spring Boot (Recommended)

1. Copy the frontend files to `src/main/resources/static/`
2. Run your Spring Boot application
3. Access at: `http://localhost:8080/index.html`

### Option 2: Serve Separately

1. Use any HTTP server (e.g., Live Server, Python HTTP server, Node http-server)
2. Make sure the server port is in the CORS allowed origins
3. Update API calls if needed (add base URL)

**Example with Python:**
```bash
cd frontend
python3 -m http.server 8081
```

**Example with Node:**
```bash
npx http-server frontend -p 8081
```

Then open: `http://localhost:8081`

### Option 3: Proxy Configuration

If using a development server (Vite, Webpack, etc.), configure proxy:

```javascript
// vite.config.js
export default {
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
}
```

## 🔐 Security Notes

1. **JWT Storage**: Tokens are stored in `localStorage`. For production, consider more secure options (httpOnly cookies, etc.)
2. **HTTPS**: Always use HTTPS in production
3. **Token Expiry**: Current expiration is 24 hours (86400000ms). Token refresh is not implemented yet.
4. **Password Requirements**: Backend validates min 8 characters. Frontend validates the same.

## 📱 API Request Example

After login, use the token for authenticated requests:

```javascript
const token = localStorage.getItem('authToken');
const tokenType = localStorage.getItem('tokenType');

fetch('/api/v1/player/edit', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `${tokenType} ${token}`
  },
  body: JSON.stringify({
    // your data
  })
})
```

## 🎨 Styling

- Uses **Space Grotesk** font from Google Fonts
- Modern dark theme with gradient backgrounds
- Cyan (`#22d3ee`) and Purple (`#a855f7`) accent colors
- Fully responsive design
- Glass-morphism card effects

## 🛠️ Customization

### Change Redirect URLs

**Login redirect (after successful login):**
```javascript
// In login.html, line 168
const DASHBOARD_REDIRECT = '/dashboard.html'; // Change this
```

**Signup redirect (after successful registration):**
```javascript
// In index.html/signup.html, line 168
const LOGIN_REDIRECT = 'login.html'; // Change this
```

### Change Backend URL

If your backend is on a different domain/port:

```javascript
// In login.html
const res = await fetch('http://your-backend.com/api/v1/auth/login', {
  // ...
});

// In signup.html
const res = await fetch('http://your-backend.com/api/v1/player/register', {
  // ...
});
```

Or create a config file:

```javascript
// config.js
const API_BASE_URL = 'http://localhost:8080';

// Then use it
const res = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
  // ...
});
```

## 📋 Backend Requirements Checklist

✅ JWT authentication configured  
✅ `/api/v1/auth/login` endpoint public  
✅ `/api/v1/player/register` endpoint public  
✅ CORS enabled for frontend origin  
✅ Returns `AuthResponse` with `token`, `role`, `expiresIn`, `tokenType`  
✅ `JwtAuthenticationFilter` configured  

All requirements are already met in your backend! 🎉

## 🐛 Troubleshooting

### CORS Errors
- Check that your frontend URL is in `ConfigurationSecurity.java`
- Verify `allowCredentials` is set to `true`

### 401 Unauthorized
- Check that token is being sent in Authorization header
- Verify token format: `Bearer <token>`
- Check token expiration

### Registration Fails
- Username must be 3-30 characters
- Password must be 8+ characters
- Email must be valid format
- All fields are required

## 📞 Support

For backend issues, check:
- `src/main/java/com/levelup/levelup_academy/Config/ConfigurationSecurity.java`
- `src/main/java/com/levelup/levelup_academy/Controller/AuthController.java`
- `src/main/java/com/levelup/levelup_academy/Service/PlayerService.java`

---

Built with ❤️ for LevelUp Academy

