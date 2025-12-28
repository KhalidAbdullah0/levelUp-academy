# 🔍 Debugging Guide - Admin Dashboard Issues

## Current Issues to Check

### 1. User Data Showing as "N/A"

**Possible Causes:**
- Backend not restarted after changes
- User relationship not being loaded
- JSON serialization issue

**How to Debug:**

1. **Check Browser Console:**
   - Open DevTools (F12)
   - Go to Console tab
   - Look for: `First pro data:` and `First pro user:`
   - This will show the actual data structure

2. **Check Network Tab:**
   - Open DevTools (F12)
   - Go to Network tab
   - Refresh page
   - Find `/api/v1/pro/get` request
   - Click on it
   - Go to "Response" tab
   - Check if `user` field is included in the JSON

3. **Expected Response Structure:**
   ```json
   [
     {
       "id": 1,
       "isApproved": false,
       "user": {
         "id": 1,
         "username": "john_doe",
         "firstName": "John",
         "lastName": "Doe",
         "email": "john@example.com"
       }
     }
   ]
   ```

### 2. CV Download Failing (400 Bad Request)

**Possible Causes:**
- Admin doesn't have moderator
- CV file doesn't exist
- File path is incorrect

**How to Debug:**

1. **Check Browser Console:**
   - Look for: `CV download error:` messages
   - Check the error status and message

2. **Check Backend Logs:**
   - Look for errors when clicking "View CV"
   - Check if file path exists
   - Verify file permissions

3. **Test CV Endpoint:**
   ```bash
   # Get your admin token first, then:
   curl -H "Authorization: Bearer YOUR_TOKEN" \
        http://localhost:8080/api/v1/pro/cv/1 \
        --output test_cv.pdf
   ```

---

## Quick Fixes

### Fix 1: Restart Backend

**This is REQUIRED after making backend changes:**

```bash
# Stop backend (Ctrl+C)
# Restart:
cd /Users/abdullahalzubaidi/levelUp-academy
./mvnw spring-boot:run
```

Wait for: `Started LevelUpAcademyApplication in X seconds`

### Fix 2: Clear Browser Cache

1. Press **F12** (open DevTools)
2. Right-click the **refresh button**
3. Select **"Empty Cache and Hard Reload"**

Or:
1. Press **F12**
2. Go to **Application** tab
3. Click **"Clear storage"**
4. Click **"Clear site data"**

### Fix 3: Check Backend Response

**In Browser Console, run:**
```javascript
// After page loads, check the data:
console.log('Pros:', pros.value);
console.log('First pro:', pros.value[0]);
console.log('First pro user:', pros.value[0]?.user);
```

**Expected Output:**
```
Pros: Array(2)
First pro: {id: 1, isApproved: false, user: {…}}
First pro user: {id: 1, username: "john_doe", firstName: "John", ...}
```

If `user` is `undefined`, the backend isn't loading it correctly.

---

## Backend Verification

### Check if Changes Were Applied:

1. **Verify ProRepository has the query:**
   ```bash
   grep -A 2 "findAllWithUser" src/main/java/com/levelup/levelup_academy/Repository/ProRepository.java
   ```
   
   Should show:
   ```java
   @Query("SELECT p FROM Pro p JOIN FETCH p.user")
   List<Pro> findAllWithUser();
   ```

2. **Verify ProService uses it:**
   ```bash
   grep -A 3 "getAllProForAdmin" src/main/java/com/levelup/levelup_academy/Service/ProService.java
   ```
   
   Should show:
   ```java
   public List<Pro> getAllProForAdmin(){
       return proRepository.findAllWithUser();
   }
   ```

3. **Verify Pro model has user field:**
   ```bash
   grep -A 3 "@JsonIgnoreProperties" src/main/java/com/levelup/levelup_academy/Model/Pro.java
   ```
   
   Should show user field with `@JsonIgnoreProperties` (not `@JsonIgnore`)

---

## Step-by-Step Debugging

### Step 1: Verify Backend is Running
```bash
# Check if backend is running
curl http://localhost:8080/api/v1/auth/login
# Should return 400 (Bad Request) not 404 (Not Found)
```

### Step 2: Test API Directly
```bash
# Login as admin first to get token
# Then test the endpoint:
curl -H "Authorization: Bearer YOUR_TOKEN" \
     http://localhost:8080/api/v1/pro/get | jq '.[0]'
```

**Check if `user` field is in the response.**

### Step 3: Check Frontend Console
1. Open admin dashboard
2. Press F12
3. Check Console for:
   - `Data loaded successfully:`
   - `First pro data:`
   - `First pro user:`

### Step 4: Check Network Requests
1. Open DevTools → Network tab
2. Refresh page
3. Find `/api/v1/pro/get`
4. Click → Response tab
5. Verify JSON structure includes `user` field

---

## Common Issues & Solutions

### Issue: User is `undefined` in console

**Solution:**
- Backend not restarted → Restart backend
- Query not working → Check ProRepository.findAllWithUser()
- JSON serialization → Check @JsonIgnoreProperties

### Issue: CV download returns 400

**Solution:**
- Check if admin has access → Verify ProController.cv() method
- Check if file exists → Verify CV was uploaded
- Check file path → Look in backend logs

### Issue: Data loads but shows "N/A"

**Solution:**
- Check if `pro.user` exists → Use console.log
- Check if `pro.user.username` exists → Verify data structure
- Frontend template issue → Check Vue.js binding

---

## Testing Checklist

- [ ] Backend restarted after changes
- [ ] Browser cache cleared
- [ ] Console shows "Data loaded successfully"
- [ ] Console shows "First pro data" with user field
- [ ] Network tab shows user in JSON response
- [ ] Frontend displays username, name, email
- [ ] CV download works (no 400 error)

---

## Still Having Issues?

1. **Check Backend Logs:**
   - Look for errors when loading data
   - Check for SQL query errors
   - Verify user relationship is loaded

2. **Check Frontend Console:**
   - Look for JavaScript errors
   - Check network request failures
   - Verify data structure

3. **Verify Database:**
   ```sql
   SELECT p.id, u.username, u.first_name, u.email 
   FROM pro p 
   JOIN user u ON p.user_id = u.id 
   LIMIT 1;
   ```
   
   Should return data if users exist.

---

**Last Updated:** December 14, 2025

