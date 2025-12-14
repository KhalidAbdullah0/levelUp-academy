# 🔐 Admin Login Guide

## Quick Steps

### 1. Create Admin User in Database
```bash
cd /Users/abdullahalzubaidi/levelUp-academy
./RUN_THIS.sh
```

Enter your MySQL password when prompted.

### 2. Start Spring Boot
Make sure your application is running:
```bash
./mvnw spring-boot:run
```

### 3. Login as Admin
Go to: `http://localhost:8080/login.html`

**Credentials:**
- **Username:** `admin`
- **Password:** `admin123`

### 4. Automatic Redirect
After successful login, you'll be **automatically redirected** to:
```
http://localhost:8080/admin-dashboard.html
```

## 🎯 Admin Dashboard Features

- ⏳ View pending Pro Players
- ⏳ View pending Trainers
- ✅ Approve Pro Players
- ❌ Reject Pro Players
- ✅ Approve Trainers
- ❌ Reject Trainers
- 📄 Download CVs
- 👥 View all Players
- 👨‍👩‍👧 View all Parents

## 🔄 Login Flow

```
Login Page
    ↓
Enter: admin / admin123
    ↓
Check Role → ADMIN
    ↓
Redirect → admin-dashboard.html ✅
```

## 🐛 Troubleshooting

### Can't Login?
1. Check if admin exists:
   ```bash
   mysql -u root -p -e "SELECT username, role FROM levelup1.users WHERE username='admin';"
   ```

2. Verify password hash:
   ```bash
   mysql -u root -p -e "SELECT username, LEFT(password, 20) FROM levelup1.users WHERE username='admin';"
   ```
   Should start with: `$2a$10$N9qo8uLOickgx...`

3. Check application logs for errors

### Goes to Wrong Page?
- Clear browser cache and localStorage
- Check browser console for errors
- Verify role in localStorage: `localStorage.getItem('userRole')`

### Database Connection Failed?
- Make sure MySQL is running
- Check database name: `levelup1`
- Verify credentials in `application.properties`

## 📊 Test Flow

1. **Login as Admin** → `http://localhost:8080/login.html`
2. **See Admin Dashboard** → Statistics, pending lists
3. **Try accessing regular dashboard** → `http://localhost:8080/dashboard.html`
4. **Should redirect back** → To admin dashboard ✅

---

**All set! You should now be able to login as admin and access the admin dashboard! 🎉**

