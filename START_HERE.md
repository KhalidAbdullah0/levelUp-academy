# 🚀 START HERE - LevelUp Academy

## Quick Start in 3 Steps

### Step 1: Start the Backend ⚙️
```bash
cd /Users/abdullahalzubaidi/levelUp-academy
./mvnw spring-boot:run
```

Wait for the message: `Started LevelUpAcademyApplication in X seconds`

---

### Step 2: Create Admin User 👤
```bash
cd /Users/abdullahalzubaidi/levelUp-academy/frontend
mysql -u root -p levelup_academy < create_admin_user.sql
```

Enter your MySQL password when prompted.

---

### Step 3: Open the Application 🌐
Open your browser and go to:
```
http://localhost:8080/home.html
```

---

## 🎯 What to Do Next

### For Admin:
1. Click **"Login"** button
2. Enter credentials:
   - **Username:** `admin`
   - **Password:** `Admin@123`
3. You'll be redirected to the **Admin Dashboard**
4. Check the **"Pending Approvals"** tab to approve users

### For Testing User Registration:
1. Click **"Sign Up"** button
2. Choose a role:
   - **Player** - Instant access
   - **Pro Player** - Needs admin approval + CV upload
   - **Trainer** - Needs admin approval + CV upload
   - **Parent** - Instant access
3. Fill the form and submit
4. If you chose Pro/Trainer, you'll see "Pending Approval" page
5. Login as admin to approve them

---

## 🎮 All Pages Available

| Page | URL | Description |
|------|-----|-------------|
| **Home** | `/home.html` | Landing page |
| **Login** | `/login.html` | Login for all users |
| **Role Selector** | `/role-selector.html` | Choose your role |
| **Register Player** | `/register-player.html` | Player registration |
| **Register Pro** | `/register-pro.html` | Pro player registration |
| **Register Trainer** | `/register-trainer.html` | Trainer registration |
| **Register Parent** | `/register-parent.html` | Parent registration |
| **Pending Approval** | `/pending-approval.html` | Waiting for approval |
| **Admin Dashboard** | `/admin-dashboard.html` | Admin control panel |

---

## ✅ Recent Fixes Applied

### Problem 1: Admin Login Loop - FIXED ✅
- Admin now correctly redirects to dashboard
- No more infinite login loops

### Problem 2: User Name Not Showing - FIXED ✅
- Home page now shows logged-in user's name
- Displays role badge and logout button

### Problem 3: Basic Admin Dashboard - FIXED ✅
- **NEW:** Comprehensive admin dashboard with:
  - 📊 Real-time statistics (6 stat cards)
  - 🔔 Pending approvals with badge count
  - ⭐ All pro players management
  - 🎓 All trainers management
  - 🎮 All players view
  - 👨‍👩‍👧 All parents view
  - 📄 CV download functionality
  - ✓ Approve/Reject actions
  - 🎨 Modern, responsive UI

---

## 📚 Documentation

- **`ADMIN_DASHBOARD_GUIDE.md`** - Complete admin guide
- **`FIXES_APPLIED.md`** - Details of all fixes
- **`QUICK_START.md`** - Original setup guide
- **`README.md`** - Project overview

---

## 🐛 Troubleshooting

### Backend won't start?
```bash
# Check if port 8080 is already in use
lsof -i :8080

# Kill the process if needed
kill -9 <PID>
```

### Can't login as admin?
```bash
# Re-run the admin creation script
cd frontend
mysql -u root -p levelup_academy < create_admin_user.sql
```

### Pages showing 403 Forbidden?
- Make sure you're accessing via `http://localhost:8080/` (not `file://`)
- Ensure backend is running

### Email not working?
- Don't worry! Registration still works
- Emails are non-blocking
- Check `application.properties` for email config

---

## 🎉 You're Ready!

Your LevelUp Academy platform is fully set up and ready to use!

**Admin Login:**
- Username: `admin`
- Password: `Admin@123`

**Start URL:** `http://localhost:8080/home.html`

---

**Need Help?** Check the documentation files listed above or review the backend logs.

**Last Updated:** December 14, 2025

