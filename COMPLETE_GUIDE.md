# 🎮 Complete Guide - LevelUp Academy

## 🌟 Welcome to LevelUp Academy!

This is your complete guide to understanding and using the LevelUp Academy platform.

---

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [System Architecture](#system-architecture)
3. [User Roles](#user-roles)
4. [Page Navigation](#page-navigation)
5. [Admin Dashboard](#admin-dashboard)
6. [Approval Workflow](#approval-workflow)
7. [Troubleshooting](#troubleshooting)

---

## 🚀 Quick Start

### For First Time Setup:

```bash
# 1. Start Backend
cd /Users/abdullahalzubaidi/levelUp-academy
./mvnw spring-boot:run

# 2. Create Admin User (in new terminal)
cd frontend
mysql -u root -p levelup_academy < create_admin_user.sql

# 3. Open Browser
# Go to: http://localhost:8080/home.html
```

### Admin Login:
- **URL:** `http://localhost:8080/login.html`
- **Username:** `admin`
- **Password:** `Admin@123`

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    LevelUp Academy Platform                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Frontend (HTML + Vue.js)          Backend (Spring Boot)    │
│  ┌──────────────────┐              ┌──────────────────┐    │
│  │  Home Page       │              │  REST APIs       │    │
│  │  Login Page      │◄────────────►│  Authentication  │    │
│  │  Registration    │              │  Authorization   │    │
│  │  Admin Dashboard │              │  Email Service   │    │
│  └──────────────────┘              └──────────────────┘    │
│           │                                  │              │
│           │                                  │              │
│           ▼                                  ▼              │
│  ┌──────────────────┐              ┌──────────────────┐    │
│  │  localStorage    │              │  MySQL Database  │    │
│  │  - authToken     │              │  - Users         │    │
│  │  - userRole      │              │  - Players       │    │
│  │  - userName      │              │  - Pro Players   │    │
│  └──────────────────┘              │  - Trainers      │    │
│                                    │  - Parents       │    │
│                                    └──────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 👥 User Roles

### 1. 🎮 Player (Regular)
- **Registration:** Self-register
- **Approval:** ❌ Not needed
- **Access:** Instant
- **Features:** 
  - Can login immediately
  - Access to player features
  - No CV required

### 2. ⭐ Pro Player
- **Registration:** Self-register + Upload CV
- **Approval:** ✅ Required by Admin
- **Access:** After approval
- **Features:**
  - Must upload CV
  - Sees "Pending Approval" page
  - Can login after admin approval
  - Receives email notification

### 3. 🎓 Trainer
- **Registration:** Self-register + Upload CV
- **Approval:** ✅ Required by Admin
- **Access:** After approval
- **Features:**
  - Must upload CV
  - Sees "Pending Approval" page
  - Can login after admin approval
  - Receives email notification

### 4. 👨‍👩‍👧 Parent
- **Registration:** Self-register + Phone number
- **Approval:** ❌ Not needed
- **Access:** Instant
- **Features:**
  - Can login immediately
  - Can add children
  - Phone number required

### 5. 🛡️ Admin
- **Registration:** Manual (SQL script)
- **Approval:** N/A
- **Access:** Full system access
- **Features:**
  - Access to admin dashboard
  - Approve/reject Pro Players
  - Approve/reject Trainers
  - View all users
  - Download CVs
  - View statistics

---

## 🗺️ Page Navigation

### Complete Page Flow:

```
                    ┌─────────────┐
                    │  Home Page  │
                    └──────┬──────┘
                           │
              ┌────────────┴────────────┐
              │                         │
              ▼                         ▼
        ┌──────────┐            ┌──────────────┐
        │  Login   │            │ Role Selector│
        └────┬─────┘            └──────┬───────┘
             │                         │
             │         ┌───────────────┼───────────────┐
             │         │               │               │
             │         ▼               ▼               ▼
             │   ┌─────────┐    ┌──────────┐    ┌─────────┐
             │   │ Player  │    │   Pro    │    │ Trainer │
             │   │Register │    │ Register │    │ Register│
             │   └────┬────┘    └────┬─────┘    └────┬────┘
             │        │              │               │
             │        │              ▼               │
             │        │      ┌──────────────┐        │
             │        │      │   Pending    │        │
             │        │      │  Approval    │        │
             │        │      └──────────────┘        │
             │        │                              │
             │        └──────────┬───────────────────┘
             │                   │
             ▼                   ▼
        ┌──────────┐        ┌──────────┐
        │  Admin   │        │   Home   │
        │Dashboard │        │   Page   │
        └──────────┘        └──────────┘
```

### Page Descriptions:

| Page | URL | Purpose | Who Can Access |
|------|-----|---------|----------------|
| **Home** | `/home.html` | Landing page, shows login status | Everyone |
| **Login** | `/login.html` | Authentication | Everyone |
| **Role Selector** | `/role-selector.html` | Choose registration type | Everyone |
| **Register Player** | `/register-player.html` | Player signup | Everyone |
| **Register Pro** | `/register-pro.html` | Pro player signup | Everyone |
| **Register Trainer** | `/register-trainer.html` | Trainer signup | Everyone |
| **Register Parent** | `/register-parent.html` | Parent signup | Everyone |
| **Pending Approval** | `/pending-approval.html` | Wait for admin | Pro/Trainer after signup |
| **Admin Dashboard** | `/admin-dashboard.html` | Control panel | Admin only |

---

## 🎛️ Admin Dashboard

### Dashboard Layout:

```
┌────────────────────────────────────────────────────────────────┐
│  🎮 LevelUp Academy - Admin Panel    Welcome, admin [Logout]   │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  📊 STATISTICS                                                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐         │
│  │    ⏳    │ │    ⏳    │ │    ✅    │ │    ✅    │         │
│  │     5    │ │     3    │ │    12    │ │     8    │         │
│  │ Pending  │ │ Pending  │ │ Approved │ │ Approved │         │
│  │   Pros   │ │ Trainers │ │   Pros   │ │ Trainers │         │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘         │
│                                                                 │
│  ┌──────────┐ ┌──────────┐                                    │
│  │    🎮    │ │   👨‍👩‍👧   │                                    │
│  │    45    │ │    23    │                                    │
│  │  Total   │ │  Total   │                                    │
│  │ Players  │ │ Parents  │                                    │
│  └──────────┘ └──────────┘                                    │
│                                                                 │
├────────────────────────────────────────────────────────────────┤
│  TABS                                                           │
│  [🔔 Pending 8] [⭐ Pros] [🎓 Trainers] [🎮 Players] [👨‍👩‍👧]   │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ⏳ Pending Pro Player Applications                             │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ ID │ Username │ Name      │ Email      │ CV  │ Actions  │ │
│  ├────┼──────────┼───────────┼────────────┼─────┼──────────┤ │
│  │ 1  │ john_pro │ John Doe  │ j@mail.com │ 📄  │ ✓  ✗    │ │
│  │ 2  │ jane_pro │ Jane Smith│ j@mail.com │ 📄  │ ✓  ✗    │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  ⏳ Pending Trainer Applications                                │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ ID │ Username │ Name      │ Email      │ CV  │ Actions  │ │
│  ├────┼──────────┼───────────┼────────────┼─────┼──────────┤ │
│  │ 1  │ mike_t   │ Mike Coach│ m@mail.com │ 📄  │ ✓  ✗    │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### Dashboard Features:

#### 1. Statistics Cards (Top Section)
- **Real-time counts** that update automatically
- **Color-coded** for easy understanding
- **Icon-based** for quick recognition

#### 2. Tab Navigation
- **Pending Approvals** - Badge shows total pending count
- **All Pro Players** - View all pros (pending + approved)
- **All Trainers** - View all trainers (pending + approved)
- **All Players** - View all regular players
- **All Parents** - View all parents

#### 3. Action Buttons
- **📄 View** - Download CV (opens in new tab)
- **✓ Approve** - Approve user (sends email, enables login)
- **✗ Reject** - Reject user (deletes record, sends email)

#### 4. Status Badges
- **⏳ Pending** - Yellow badge, awaiting approval
- **✅ Approved** - Green badge, can login

---

## 🔄 Approval Workflow

### Complete Workflow Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                    USER REGISTRATION                         │
└───────────────────────┬─────────────────────────────────────┘
                        │
                        ▼
              ┌──────────────────┐
              │  Choose Role     │
              └────────┬─────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   ┌────────┐    ┌─────────┐    ┌────────┐
   │ Player │    │   Pro   │    │Trainer │
   │        │    │  Player │    │        │
   └───┬────┘    └────┬────┘    └───┬────┘
       │              │              │
       │              ▼              │
       │      ┌──────────────┐      │
       │      │  Upload CV   │      │
       │      └──────┬───────┘      │
       │             │              │
       │             ▼              │
       │      ┌──────────────┐      │
       │      │   Pending    │      │
       │      │   Approval   │      │
       │      │    Page      │      │
       │      └──────┬───────┘      │
       │             │              │
       │             ▼              │
       │      ┌──────────────┐      │
       │      │    ADMIN     │      │
       │      │   REVIEWS    │      │
       │      └──────┬───────┘      │
       │             │              │
       │      ┌──────┴──────┐       │
       │      │             │       │
       │      ▼             ▼       │
       │  ┌────────┐   ┌────────┐  │
       │  │Approve │   │ Reject │  │
       │  └───┬────┘   └───┬────┘  │
       │      │            │        │
       │      ▼            ▼        │
       │  ┌────────┐   ┌────────┐  │
       │  │ Email  │   │ Email  │  │
       │  │  Sent  │   │  Sent  │  │
       │  └───┬────┘   └───┬────┘  │
       │      │            │        │
       ▼      ▼            ▼        ▼
   ┌────────────────┐  ┌──────────────┐
   │  CAN LOGIN     │  │ MUST RE-     │
   │  IMMEDIATELY   │  │ REGISTER     │
   └────────────────┘  └──────────────┘
```

### Step-by-Step Process:

#### For Player/Parent (Instant Access):
1. ✅ Register on website
2. ✅ Redirected to login page
3. ✅ Can login immediately
4. ✅ Access granted

#### For Pro Player/Trainer (Requires Approval):
1. ✅ Register on website
2. ✅ Upload CV (PDF format)
3. ✅ See "Pending Approval" page
4. ✅ Receive "Application Received" email
5. ⏳ **Wait for admin review**
6. 🛡️ **Admin reviews in dashboard**
7. 🛡️ **Admin downloads and reviews CV**
8. 🛡️ **Admin makes decision:**
   - **If Approved:**
     - ✅ User receives approval email
     - ✅ User can now login
     - ✅ User appears as "Approved" in dashboard
   - **If Rejected:**
     - ❌ User receives rejection email
     - ❌ User record deleted from database
     - ❌ User must re-register to try again

---

## 🔐 Authentication & Security

### How Authentication Works:

```
┌─────────────────────────────────────────────────────────┐
│  1. User enters username + password                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│  2. Backend validates credentials                       │
│     - Checks username exists                            │
│     - Verifies password (BCrypt)                        │
│     - Checks if approved (for Pro/Trainer)              │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│  3. Backend generates JWT token                         │
│     - Contains user info                                │
│     - Contains role                                     │
│     - Has expiration time                               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│  4. Frontend stores token in localStorage               │
│     - authToken: "eyJhbGc..."                           │
│     - userRole: "ADMIN"                                 │
│     - userName: "admin"                                 │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│  5. Frontend redirects based on role                    │
│     - ADMIN → admin-dashboard.html                      │
│     - Others → home.html                                │
└─────────────────────────────────────────────────────────┘
```

### Security Features:
- ✅ **JWT Tokens** - Secure, stateless authentication
- ✅ **BCrypt Hashing** - Passwords never stored in plain text
- ✅ **Role-Based Access** - Different permissions per role
- ✅ **Token Expiration** - Tokens expire after set time
- ✅ **CORS Protection** - Only allowed origins can access API
- ✅ **SQL Injection Prevention** - Parameterized queries
- ✅ **XSS Protection** - Input sanitization

---

## 🐛 Troubleshooting

### Common Issues & Solutions:

#### 1. Can't Access Pages (403 Forbidden)
**Problem:** Getting 403 error when accessing HTML pages

**Solution:**
- ✅ Make sure you're using `http://localhost:8080/` (not `file://`)
- ✅ Ensure backend is running
- ✅ Check Spring Security configuration

#### 2. Admin Can't Login
**Problem:** Admin credentials not working

**Solution:**
```bash
# Re-create admin user
cd frontend
mysql -u root -p levelup_academy < create_admin_user.sql
```

#### 3. Login Redirect Loop
**Problem:** Keeps redirecting to login page

**Solution:**
- ✅ Clear browser localStorage
- ✅ Clear browser cache
- ✅ Try incognito/private mode
- ✅ Check browser console for errors

#### 4. Email Not Sending
**Problem:** Users not receiving emails

**Solution:**
- ✅ Don't worry! Registration still works
- ✅ Check `application.properties` email config
- ✅ Verify Gmail password is correct
- ✅ Enable "Less secure app access" in Gmail

#### 5. CV Download Not Working
**Problem:** Can't download CVs from admin dashboard

**Solution:**
- ✅ Ensure CV was uploaded during registration
- ✅ Check backend logs for errors
- ✅ Verify file storage path exists
- ✅ Check file permissions

#### 6. Statistics Not Updating
**Problem:** Dashboard stats not refreshing

**Solution:**
- ✅ Refresh the page
- ✅ Check browser console for errors
- ✅ Verify API endpoints are working
- ✅ Check backend is running

#### 7. Session Expired
**Problem:** Getting "Session expired" message

**Solution:**
- ✅ This is normal after token expires
- ✅ Simply login again
- ✅ Token lasts for several hours

---

## 📊 Database Schema

### Tables Overview:

```
┌─────────────────────────────────────────────────────────┐
│  USER (Base Table)                                       │
├─────────────────────────────────────────────────────────┤
│  - id (PK)                                              │
│  - username (UNIQUE)                                    │
│  - email (UNIQUE)                                       │
│  - password (BCrypt hashed)                             │
│  - firstName                                            │
│  - lastName                                             │
│  - role (PLAYER, PRO, TRAINER, PARENT, ADMIN)          │
│  - createdAt                                            │
└─────────────────────────────────────────────────────────┘
         │
         ├──────────────┬──────────────┬──────────────┐
         │              │              │              │
         ▼              ▼              ▼              ▼
┌──────────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
│ PLAYER       │ │ PRO      │ │ TRAINER  │ │ PARENT   │
├──────────────┤ ├──────────┤ ├──────────┤ ├──────────┤
│ - id (PK)    │ │ - id     │ │ - id     │ │ - id     │
│ - user_id    │ │ - user_id│ │ - user_id│ │ - user_id│
│              │ │ - cv     │ │ - cv     │ │ - phone  │
│              │ │ - approved│ │ - approved│ │          │
└──────────────┘ └──────────┘ └──────────┘ └──────────┘
```

---

## 🎯 Best Practices

### For Admins:
1. ✅ **Check pending approvals daily**
2. ✅ **Always review CVs before approving**
3. ✅ **Respond to applications promptly**
4. ✅ **Use reject for spam/fake applications**
5. ✅ **Monitor statistics regularly**
6. ✅ **Keep admin credentials secure**

### For Users:
1. ✅ **Use strong passwords**
2. ✅ **Provide valid email address**
3. ✅ **Upload clear, professional CVs (Pro/Trainer)**
4. ✅ **Wait patiently for approval**
5. ✅ **Check email for notifications**
6. ✅ **Logout when done**

### For Developers:
1. ✅ **Keep backend running during development**
2. ✅ **Use `http://localhost:8080/` (not file://)**
3. ✅ **Check browser console for errors**
4. ✅ **Review backend logs regularly**
5. ✅ **Test on multiple browsers**
6. ✅ **Test mobile responsiveness**

---

## 📱 Mobile Experience

The platform is fully responsive and works on all devices:

### Desktop (1920px+):
- Full-width tables
- 6-column stats grid
- All features visible

### Tablet (768px - 1920px):
- Scrollable tables
- 3-column stats grid
- Optimized layout

### Mobile (375px - 768px):
- Horizontal scroll for tables
- 2-column stats grid
- Collapsible navigation
- Touch-friendly buttons

---

## 🚀 Performance Tips

### For Best Performance:
1. ✅ Use modern browser (Chrome, Firefox, Safari, Edge)
2. ✅ Clear cache periodically
3. ✅ Close unused tabs
4. ✅ Use stable internet connection
5. ✅ Keep browser updated

---

## 📞 Support

### Need Help?

1. **Check Documentation:**
   - `START_HERE.md` - Quick setup
   - `ADMIN_DASHBOARD_GUIDE.md` - Admin manual
   - `FIXES_APPLIED.md` - Technical details
   - `WHATS_NEW.md` - Latest changes

2. **Check Logs:**
   - Browser console (F12)
   - Backend terminal output
   - MySQL error logs

3. **Common Solutions:**
   - Restart backend
   - Clear browser cache
   - Re-create admin user
   - Check database connection

---

## 🎉 You're Ready!

Your LevelUp Academy platform is fully set up and ready to use!

### Quick Links:
- **Home:** `http://localhost:8080/home.html`
- **Login:** `http://localhost:8080/login.html`
- **Admin Dashboard:** `http://localhost:8080/admin-dashboard.html`

### Admin Credentials:
- **Username:** `admin`
- **Password:** `Admin@123`

---

**Happy Managing! 🎮**

---

**Version:** 2.0  
**Last Updated:** December 14, 2025  
**Status:** ✅ Production Ready

