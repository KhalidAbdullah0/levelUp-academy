# 🚀 LevelUp Academy - Quick Start

## ⚡ Start in 3 Steps

### 1. Start Backend
```bash
cd /Users/abdullahalzubaidi/levelUp-academy
./mvnw spring-boot:run
```

### 2. Create Admin User
Run this SQL in your MySQL database:
```sql
-- Create admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role, created_at)
VALUES (
  'admin',
  '$2a$10$N9qo8uLOickgx2ZrVzY45.1Ey67YkSal4/Z0GZtU6WBOvyWzLg9CO',
  'admin@levelupacademy.com',
  'Admin',
  'User',
  'ADMIN',
  NOW()
);
```

### 3. Open Browser
```
http://localhost:8080/home.html
```

## 📋 Test Scenarios

### Test 1: Player (Instant Access)
1. Click "Sign Up" → Select "Player"
2. Fill form → Submit
3. Login → ✅ Access granted immediately

### Test 2: Pro Player (Needs Approval)
1. Click "Sign Up" → Select "Pro Player"
2. Fill form + Upload CV (PDF) → Submit
3. Try to login → ❌ "Pending approval"
4. Login as admin → Go to admin dashboard
5. Click "Pro Players" tab → Click "Approve"
6. Logout admin → Login as pro → ✅ Access granted

### Test 3: Admin Approval
1. Login with username: `admin`, password: `admin123`
2. Auto-redirect to admin dashboard
3. See pending applications
4. Click "View CV" to download
5. Click "Approve" or "Reject"
6. Done!

## 🔗 Important URLs

| Page | URL |
|------|-----|
| Home | `http://localhost:8080/home.html` |
| Login | `http://localhost:8080/login.html` |
| Sign Up | `http://localhost:8080/role-selector.html` |
| Admin Dashboard | `http://localhost:8080/admin-dashboard.html` |

## 🎯 User Roles

| Role | Registration | Approval | Access |
|------|--------------|----------|--------|
| Player | ✅ Self | ❌ No | Instant |
| Pro Player | ✅ Self + CV | ✅ Admin | After approval |
| Trainer | ✅ Self + CV | ✅ Admin | After approval |
| Parent | ✅ Self + Phone | ❌ No | Instant |
| Moderator | ❌ Admin only | N/A | Instant |
| Admin | ❌ Manual DB | N/A | Instant |

## 🔐 Default Credentials

**Admin:**
- Username: `admin`
- Password: `admin123`
- Dashboard: `admin-dashboard.html`

## 📝 Registration Fields

### Player
- Username (3-30 chars)
- First Name
- Last Name
- Email
- Password (8+ chars)

### Pro Player
- All Player fields
- CV Upload (PDF, max 10MB)
- Phone Number (optional)

### Trainer
- All Player fields
- CV Upload (PDF, max 10MB)

### Parent
- All Player fields
- Phone Number (Saudi format: 05XXXXXXXX)

## 🐛 Quick Fixes

### CORS Error?
Add your URL to `ConfigurationSecurity.java`:
```java
cfg.setAllowedOrigins(List.of("http://localhost:8080", "YOUR_URL"));
```

### CV Upload Fails?
Create directory:
```bash
mkdir -p uploads/cvs
```

### Email Not Sending?
Update `application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Can't Login After Registration?
- Player/Parent: Should work immediately
- Pro/Trainer: Need admin approval first

## 📞 Need Help?

Check these files:
- `COMPLETE_SYSTEM.md` - Full documentation
- `SYSTEM_OVERVIEW.md` - Technical details
- `TESTING.md` - Test checklist
- `README.md` - Setup guide

## ✅ All Done!

Your system is ready. Start testing! 🎉

