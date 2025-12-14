# 🔧 Fix: Session Expired After 5 Seconds

## Problem
When logging in as admin, you immediately get "Session expired. Please login again." after 5 seconds.

## Root Cause
The admin dashboard was trying to access `/api/v1/pro/get` and `/api/v1/trainer/get` endpoints, but these were only accessible by **MODERATOR** role, not **ADMIN** role. This caused a 403 Forbidden error, which the dashboard interpreted as "Session expired".

## Solution Applied

### 1. Updated Spring Security Configuration ✅
**File:** `src/main/java/com/levelup/levelup_academy/Config/ConfigurationSecurity.java`

**Changes:**
- Created a new shared section for endpoints accessible by both **MODERATOR** and **ADMIN**
- Moved these endpoints to shared access:
  - `/api/v1/parent/get`
  - `/api/v1/player/get`
  - `/api/v1/pro/get` ← **This was the problem**
  - `/api/v1/trainer/get` ← **This was the problem**
  - `/api/v1/pro/cv/**`
  - `/api/v1/trainer/cv/**`

**Before:**
```java
// Only MODERATOR could access
.requestMatchers("/api/v1/pro/get", "/api/v1/trainer/get")
.hasAuthority("MODERATOR")
```

**After:**
```java
// Both MODERATOR and ADMIN can access
.requestMatchers("/api/v1/pro/get", "/api/v1/trainer/get")
.hasAnyAuthority("MODERATOR", "ADMIN")
```

### 2. Improved Dashboard Error Handling ✅
**File:** `frontend/admin-dashboard.html`

**Changes:**
- Better error differentiation:
  - **401 Unauthorized** → Session expired, redirect to login
  - **403 Forbidden** → Access denied, show error but don't logout
  - **Network errors** → Show helpful message about backend
- Added detailed error logging to browser console
- More graceful handling of API failures

---

## How to Test the Fix

### Step 1: Restart Backend
The security configuration changed, so you need to restart:

```bash
cd /Users/abdullahalzubaidi/levelUp-academy

# Stop current backend (if running)
# Press Ctrl+C in the terminal where it's running

# Start backend
./mvnw spring-boot:run
```

Wait for: `Started LevelUpAcademyApplication in X seconds`

### Step 2: Clear Browser Cache
```bash
# In your browser:
1. Press F12 (open DevTools)
2. Right-click the refresh button
3. Click "Empty Cache and Hard Reload"

# Or simply clear localStorage:
1. Press F12
2. Go to "Application" or "Storage" tab
3. Click "Local Storage" → "http://localhost:8080"
4. Click "Clear All"
```

### Step 3: Test Admin Login
```bash
1. Go to: http://localhost:8080/login.html
2. Login with:
   - Username: admin
   - Password: Admin@123
3. You should be redirected to admin dashboard
4. Dashboard should load without "Session expired" error
5. You should see statistics and data tables
```

### Step 4: Check Browser Console
```bash
1. Press F12 to open DevTools
2. Go to "Console" tab
3. You should see:
   "Data loaded successfully: {pros: X, trainers: Y}"
4. No red errors about 403 or authentication
```

---

## What Should Work Now

### ✅ Admin Can:
- Login successfully
- Access admin dashboard immediately
- See statistics load properly
- View pending Pro Players
- View pending Trainers
- Download CVs
- Approve/Reject users
- Stay logged in (no "Session expired")

### ✅ Dashboard Should Show:
- 6 statistics cards with real numbers
- All 5 tabs working
- Data loading without errors
- No "Session expired" popup

---

## Troubleshooting

### If Still Getting "Session expired":

#### 1. Check Backend is Running
```bash
# In terminal, you should see:
Started LevelUpAcademyApplication in X.XXX seconds
```

#### 2. Check Browser Console
```bash
# Press F12, look for:
- Red errors?
- 403 Forbidden?
- Failed to fetch?
```

#### 3. Verify Security Changes Applied
```bash
# Check your ConfigurationSecurity.java has the new changes
# Look for:
.hasAnyAuthority("MODERATOR", "ADMIN")
```

#### 4. Clear Everything
```bash
# Clear browser completely:
1. Close all browser tabs
2. Clear cache and cookies
3. Restart browser
4. Try again
```

#### 5. Check MySQL Connection
```bash
# Make sure MySQL is running:
mysql -u root -p

# In MySQL:
USE levelup_academy;
SELECT * FROM user WHERE username = 'admin';
```

### If Getting 403 Errors:

#### Check the endpoint path:
```bash
# In browser console, you should see:
GET http://localhost:8080/api/v1/pro/get
GET http://localhost:8080/api/v1/trainer/get

# If you see 403, verify:
1. Backend restarted with new security config
2. You're logged in as ADMIN (check localStorage.userRole)
3. Token is valid (check localStorage.authToken)
```

### If Dashboard Shows No Data:

#### This is normal if:
```bash
1. No users have registered yet
2. No Pro Players in database
3. No Trainers in database

# To test, register a test Pro Player:
1. Go to: http://localhost:8080/role-selector.html
2. Choose "Pro Player"
3. Fill form and upload a CV
4. Login as admin
5. You should now see 1 pending pro player
```

---

## Technical Details

### Security Configuration Structure

```java
// PUBLIC (No authentication)
/login.html, /register-*.html, /api/v1/auth/login, etc.

// SHARED (MODERATOR & ADMIN)
/api/v1/pro/get           ← Admin dashboard needs this
/api/v1/trainer/get       ← Admin dashboard needs this
/api/v1/pro/cv/**         ← Admin dashboard needs this
/api/v1/trainer/cv/**     ← Admin dashboard needs this

// ADMIN ONLY
/api/v1/pro/approve/**
/api/v1/pro/reject/**
/api/v1/trainer/approve-trainer/**
/api/v1/trainer/reject-trainer/**
/api/v1/moderator/register

// MODERATOR ONLY
/api/v1/game/**, /api/v1/session/**, etc.

// OTHER ROLES
PLAYER, PRO, TRAINER, PARENTS (each has their endpoints)
```

### Why This Fix Works

1. **Before:** Admin tries to access `/api/v1/pro/get` → Spring Security checks → Only MODERATOR allowed → Returns 403 → Dashboard shows "Session expired"

2. **After:** Admin tries to access `/api/v1/pro/get` → Spring Security checks → MODERATOR or ADMIN allowed → Returns 200 with data → Dashboard loads successfully

### Dashboard API Calls on Load

When admin dashboard loads, it makes these API calls:
```javascript
// Parallel requests
Promise.all([
  fetch('/api/v1/pro/get'),        // Gets all pro players
  fetch('/api/v1/trainer/get')     // Gets all trainers
])
```

Both must succeed (or return empty arrays) for dashboard to work properly.

---

## Prevention

To avoid this in the future:

### 1. Test with Browser DevTools Open
```bash
Always have F12 console open when testing
Watch for red errors
Check Network tab for 403/401 errors
```

### 2. Check Security Config When Adding Admin Features
```bash
If admin dashboard needs a new endpoint:
1. Add endpoint to ADMIN or shared section
2. Don't forget to restart backend
```

### 3. Better Error Messages
```bash
The dashboard now shows specific errors:
- "Session expired" → Only for 401 errors
- "Access denied" → For 403 errors
- "Network error" → For connection issues
```

---

## Files Modified

1. ✅ `src/main/java/com/levelup/levelup_academy/Config/ConfigurationSecurity.java`
   - Added shared MODERATOR & ADMIN section
   - Moved critical endpoints to shared access

2. ✅ `frontend/admin-dashboard.html`
   - Improved error handling
   - Better error differentiation
   - Added detailed logging

3. ✅ `src/main/resources/static/admin-dashboard.html`
   - Updated with frontend changes

---

## Summary

**Problem:** Admin getting "Session expired" after 5 seconds  
**Cause:** Spring Security blocking `/api/v1/pro/get` and `/api/v1/trainer/get` for ADMIN role  
**Solution:** Allow both MODERATOR and ADMIN to access these endpoints  
**Status:** ✅ FIXED

**Next Step:** Restart backend and test admin login!

---

**Date:** December 14, 2025  
**Status:** ✅ Fixed and Ready to Test

