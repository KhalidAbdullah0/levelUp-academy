# 🎮 Admin Dashboard Guide - LevelUp Academy

## 🚀 Quick Start

### 1. Login as Admin
- **URL:** `http://localhost:8080/login.html`
- **Username:** `admin`
- **Password:** `Admin@123`

After successful login, you'll be automatically redirected to the Admin Dashboard.

---

## ✨ Features Overview

### 📊 Real-Time Statistics Dashboard
The dashboard displays live statistics at the top:

- **⏳ Pending Pro Players** - Number of pro player applications awaiting approval
- **⏳ Pending Trainers** - Number of trainer applications awaiting approval  
- **✅ Approved Pros** - Total approved pro players
- **✅ Approved Trainers** - Total approved trainers
- **🎮 Total Players** - All registered players
- **👨‍👩‍👧 Total Parents** - All registered parents

### 🔔 Pending Approvals Tab
**Most Important Tab** - Shows all users waiting for your approval:

#### Pro Player Applications
- View all pending pro player registrations
- See their details: ID, Username, Name, Email, Phone
- **Download CV** - Click "📄 View" to download their CV
- **Approve** - Click "✓ Approve" to approve (sends approval email, enables login)
- **Reject** - Click "✗ Reject" to reject and delete application

#### Trainer Applications  
- View all pending trainer registrations
- See their details: ID, Username, Name, Email
- **Download CV** - Click "📄 View" to download their CV
- **Approve** - Click "✓ Approve" to approve (sends approval email, enables login)
- **Reject** - Click "✗ Reject" to reject and delete application

> **Note:** The tab shows a badge with the total number of pending approvals (e.g., 🔔 Pending Approvals **5**)

### ⭐ All Pro Players Tab
- View complete list of all pro players (pending + approved)
- See approval status for each
- Quick approve/reject actions for pending ones
- Download CVs

### 🎓 All Trainers Tab
- View complete list of all trainers (pending + approved)
- See approval status for each
- Quick approve/reject actions for pending ones
- Download CVs

### 🎮 All Players Tab
- View all registered regular players
- See username, name, email, registration date
- Players have instant access (no approval needed)

### 👨‍👩‍👧 All Parents Tab
- View all registered parents
- See username, name, email, phone, number of children
- Parents have instant access (no approval needed)

---

## 🔐 Access Control

### What Admin Can Do:
1. ✅ **Approve/Reject Pro Players** - Control who gets pro player access
2. ✅ **Approve/Reject Trainers** - Control who gets trainer access
3. ✅ **Download CVs** - Review uploaded CVs before approval
4. ✅ **View All Users** - See complete user database across all roles
5. ✅ **Real-Time Stats** - Monitor platform growth and pending requests
6. ✅ **Email Notifications** - System automatically sends approval/rejection emails

### What Happens When You Approve:
1. User's `isApproved` status changes to `true` in database
2. User receives approval email notification
3. User can now login successfully
4. User appears in "Approved" section with ✓ badge

### What Happens When You Reject:
1. User record is deleted from database
2. User receives rejection email notification
3. User cannot login
4. User must re-register if they want to try again

---

## 🎯 Typical Admin Workflow

### Daily Routine:
1. **Login** to admin dashboard
2. **Check Pending Approvals** tab (look for badge number)
3. **Review Applications:**
   - Click "📄 View" to download and review CVs
   - Check user details (name, email, etc.)
4. **Make Decisions:**
   - Click "✓ Approve" for qualified applicants
   - Click "✗ Reject" for unqualified applicants
5. **Monitor Stats** - Keep track of platform growth

### Best Practices:
- ✅ Always review CVs before approving
- ✅ Respond to applications promptly (users are waiting)
- ✅ Check email validity before approving
- ✅ Use reject for spam/fake applications
- ✅ Monitor statistics regularly

---

## 🐛 Troubleshooting

### Problem: Can't Login as Admin
**Solution:**
```bash
# Run this SQL script to create admin user
cd /Users/abdullahalzubaidi/levelUp-academy/frontend
mysql -u root -p levelup_academy < create_admin_user.sql
```

### Problem: "Session expired" message
**Solution:** Your login token expired. Simply login again.

### Problem: Can't download CV
**Solution:** 
- Ensure backend is running (`./mvnw spring-boot:run`)
- Check that CV was uploaded during registration
- Verify you're logged in as admin

### Problem: Approval button not working
**Solution:**
- Check browser console for errors
- Ensure backend API is running
- Verify your admin token is valid

### Problem: No pending applications showing
**Solution:**
- This is normal if no one has registered yet
- Ask users to register as Pro Player or Trainer
- Check other tabs to see if they're already approved

---

## 🔄 User Registration Flow

### For Pro Players & Trainers (Require Approval):
1. User registers → Uploads CV
2. User sees "Pending Approval" page
3. **Admin reviews in dashboard** ← YOU ARE HERE
4. Admin approves/rejects
5. User receives email notification
6. If approved: User can login
7. If rejected: User must re-register

### For Players & Parents (Instant Access):
1. User registers
2. User redirected to login
3. User can login immediately (no admin approval needed)

---

## 📧 Email Notifications

The system automatically sends emails for:
- ✅ **Welcome emails** - When users register
- ✅ **Approval emails** - When you approve Pro/Trainer
- ✅ **Rejection emails** - When you reject Pro/Trainer
- ✅ **Pending notification** - Reminds users to wait for approval

> **Note:** If email fails, registration still completes. Users can still be approved/rejected.

---

## 🎨 Dashboard Features

### Visual Indicators:
- **⏳ Yellow Badge** = Pending approval
- **✅ Green Badge** = Approved
- **🔔 Red Badge** = Number of pending requests
- **📄 Blue Button** = Download CV
- **✓ Green Button** = Approve
- **✗ Red Button** = Reject

### Smart Features:
- **Auto-refresh** - Data updates when you approve/reject
- **Confirmation dialogs** - Prevents accidental approvals/rejections
- **Toast notifications** - Shows success/error messages
- **Responsive design** - Works on desktop, tablet, mobile
- **Loading states** - Shows spinner while fetching data

---

## 🔧 Technical Details

### API Endpoints Used:
- `GET /api/v1/pro/get` - Fetch all pro players
- `GET /api/v1/trainer/get` - Fetch all trainers
- `POST /api/v1/pro/approve/{id}` - Approve pro player
- `PUT /api/v1/pro/reject/{id}` - Reject pro player
- `POST /api/v1/trainer/approve-trainer/{id}` - Approve trainer
- `PUT /api/v1/trainer/reject-trainer/{id}` - Reject trainer
- `GET /api/v1/pro/cv/{id}` - Download pro player CV
- `GET /api/v1/trainer/cv/{id}` - Download trainer CV

### Authentication:
- Uses JWT token stored in `localStorage`
- Token sent in `Authorization: Bearer <token>` header
- Automatically redirects to login if token expires
- Only users with `ADMIN` role can access

### Data Storage:
- `authToken` - JWT authentication token
- `userRole` - User's role (must be "ADMIN")
- `userName` - Admin username for display
- `tokenType` - Token type (usually "Bearer")

---

## 📱 Mobile Support

The dashboard is fully responsive and works on:
- 💻 Desktop computers
- 📱 Mobile phones
- 📱 Tablets

On mobile:
- Tables scroll horizontally
- Stats grid adjusts to 2 columns
- Buttons remain accessible
- All features work the same

---

## 🎯 Quick Reference

| Action | Button | Result |
|--------|--------|--------|
| Approve Pro | ✓ Approve | User can login, gets email |
| Reject Pro | ✗ Reject | User deleted, gets email |
| Approve Trainer | ✓ Approve | User can login, gets email |
| Reject Trainer | ✗ Reject | User deleted, gets email |
| Download CV | 📄 View | CV file downloads |
| Logout | Logout | Returns to login page |

---

## 🚀 Getting Started Checklist

- [ ] Backend is running (`./mvnw spring-boot:run`)
- [ ] Admin user is created (run `create_admin_user.sql`)
- [ ] Navigate to `http://localhost:8080/login.html`
- [ ] Login with `admin` / `Admin@123`
- [ ] Check "Pending Approvals" tab
- [ ] Review and approve/reject applications
- [ ] Monitor statistics

---

## 💡 Tips & Tricks

1. **Bookmark the dashboard** - Save `http://localhost:8080/admin-dashboard.html` for quick access
2. **Check daily** - Users are waiting for approval, check at least once per day
3. **Review CVs carefully** - Download and review before approving
4. **Use reject wisely** - Rejection deletes the user permanently
5. **Watch the badge** - The number on "Pending Approvals" tab shows how many need review
6. **Stay logged in** - Token lasts for hours, no need to login constantly
7. **Mobile friendly** - You can approve from your phone if needed

---

## 🎉 You're All Set!

Your admin dashboard is now ready to use. Login and start managing your LevelUp Academy platform! 🚀

**Need Help?** Check the troubleshooting section above or review the backend logs.

---

**Last Updated:** December 2024  
**Version:** 2.0 - Comprehensive Admin Dashboard

