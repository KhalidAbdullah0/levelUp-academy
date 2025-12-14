# Testing Guide - LevelUp Academy Frontend

Quick guide to test the authentication flow.

## 🧪 Test Checklist

### Prerequisites
- [ ] Backend is running (default: `http://localhost:8080`)
- [ ] Database is up and running (MySQL on port 3306)
- [ ] Frontend is accessible (served via Spring Boot or separate server)

### Test 1: Sign Up Flow

1. **Open Sign Up Page**
   - Navigate to `index.html` or `signup.html`
   - Page should load with dark theme and gradient background

2. **Fill Registration Form**
   ```
   Username: testplayer123
   First Name: Test
   Last Name: Player
   Email: testplayer@example.com
   Password: password123
   Confirm Password: password123
   ```

3. **Submit Form**
   - Click "Sign up" button
   - Button should show "Creating account…"
   - Success message should appear
   - Should auto-redirect to login page after ~1.2s

4. **Check Backend**
   - Check database `users` table for new user
   - Check email inbox for welcome email (if email service is configured)

### Test 2: Login Flow

1. **Open Login Page**
   - Navigate to `login.html`
   - Or click "Login" link from signup page

2. **Fill Login Form**
   ```
   Username: testplayer123
   Password: password123
   ```

3. **Submit Form**
   - Click "Login" button
   - Button should show "Signing in…"
   - Success message should appear with role badge
   - JWT token should be displayed (truncated)
   - Should auto-redirect to dashboard after ~0.8s

4. **Check localStorage**
   - Open browser DevTools (F12) → Application/Storage → Local Storage
   - Should see:
     - `authToken`: JWT string (long string starting with eyJ...)
     - `userRole`: "PLAYER"
     - `tokenType`: "Bearer"

### Test 3: Dashboard Access

1. **After Login Redirect**
   - Should land on `dashboard.html`
   - Header should show "PLAYER" badge
   - Should display welcome message
   - Should show full JWT token

2. **Test Protected Route**
   - Clear localStorage or open in incognito
   - Try to access `dashboard.html` directly
   - Should auto-redirect to `login.html`

3. **Test Logout**
   - Click "Logout" button
   - Should clear localStorage
   - Should redirect to login page
   - Try accessing dashboard again (should redirect to login)

### Test 4: Validation Tests

#### Sign Up Validation
Test these scenarios (should show error messages):
- [ ] Username less than 3 characters
- [ ] Invalid email format
- [ ] Password less than 8 characters
- [ ] Passwords don't match
- [ ] Empty first or last name

#### Login Validation
- [ ] Empty username
- [ ] Empty password
- [ ] Wrong credentials (should show error from backend)

### Test 5: Navigation

- [ ] Click "Login" link on signup page → goes to login
- [ ] Click "Sign up" link on login page → goes to signup
- [ ] After signup → redirects to login
- [ ] After login → redirects to dashboard
- [ ] Logout → redirects to login

## 🐛 Common Issues & Solutions

### Issue: CORS Error
**Error:** `Access to fetch at 'http://localhost:8080/api/v1/auth/login' from origin 'http://localhost:3000' has been blocked by CORS policy`

**Solution:**
Add your frontend origin to `ConfigurationSecurity.java`:
```java
cfg.setAllowedOrigins(List.of(
    "http://localhost:8080",
    "http://localhost:3000", // Add this
    // ...
));
```

### Issue: 401 Unauthorized on Dashboard
**Error:** Dashboard loads but shows no data

**Solution:**
- Check localStorage has valid token
- Verify token hasn't expired (default: 24 hours)
- Check backend JWT secret matches in `application.properties`

### Issue: Network Error
**Error:** `Failed to fetch`

**Solution:**
- Verify backend is running: `curl http://localhost:8080/api/v1/auth/login`
- Check if port is correct
- If using separate server, update `config.js`:
  ```javascript
  API_BASE_URL: 'http://localhost:8080',
  ```

### Issue: Registration Fails with Validation Error
**Error:** Backend returns validation messages

**Solution:**
- Username: 3-30 characters
- Password: 8-200 characters
- Email: valid format
- All fields required

### Issue: Email Not Sent
**Note:** Email sending is configured in the backend but may fail if:
- Gmail credentials are wrong
- "Less secure app access" not enabled
- Using 2FA without app password

This is non-critical - registration still succeeds, email is just not sent.

## 📊 Expected Backend Responses

### Successful Registration
```json
{
  "message": "Player registers"
}
```

### Successful Login
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "role": "PLAYER"
}
```

### Failed Login
```json
{
  "message": "Bad credentials",
  "error": "Unauthorized"
}
```

## 🔍 DevTools Network Tab Inspection

### Registration Request
```
POST /api/v1/player/register
Content-Type: application/json

{
  "username": "testplayer123",
  "password": "password123",
  "email": "testplayer@example.com",
  "firstName": "Test",
  "lastName": "Player"
}
```

### Login Request
```
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "testplayer123",
  "password": "password123"
}
```

### Authenticated Request (Example)
```
GET /api/v1/player/player
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## ✅ Success Criteria

All tests passing means:
- ✅ User can register successfully
- ✅ User receives JWT token on login
- ✅ Token is stored in localStorage
- ✅ Protected routes redirect unauthenticated users
- ✅ Logout clears authentication
- ✅ Navigation links work correctly
- ✅ Validation prevents invalid data
- ✅ Error messages display properly

## 🚀 Next Steps After Testing

1. **Build actual dashboard content** (replace placeholder)
2. **Implement token refresh** (before 24h expiration)
3. **Add "Remember me"** functionality
4. **Implement "Forgot password"** flow
5. **Add role-based UI** (different dashboard for ADMIN, PLAYER, etc.)
6. **Improve error handling** (more specific messages)
7. **Add loading animations** (better UX)
8. **Implement session timeout** warning

## 📝 Test Results Template

Use this to document your test results:

```
Date: ___________
Tester: ___________

Sign Up Flow: ☐ Pass ☐ Fail
  Notes: _______________________________

Login Flow: ☐ Pass ☐ Fail
  Notes: _______________________________

Dashboard Access: ☐ Pass ☐ Fail
  Notes: _______________________________

Validation Tests: ☐ Pass ☐ Fail
  Notes: _______________________________

Navigation: ☐ Pass ☐ Fail
  Notes: _______________________________

Overall Status: ☐ All Pass ☐ Some Issues
```

---

Happy Testing! 🎮✨

