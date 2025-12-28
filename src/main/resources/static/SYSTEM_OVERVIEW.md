# LevelUp Academy - Complete System Overview

## 🎯 System Architecture

### User Roles & Access

| Role | Registration | Approval Required | Dashboard | Special Features |
|------|--------------|-------------------|-----------|------------------|
| **Player** | Self-register | ❌ Instant | `dashboard.html` | Track progress, book sessions |
| **Pro Player** | Self-register + CV | ✅ Admin approval | `dashboard.html` | Train others, earn money |
| **Trainer** | Self-register + CV | ✅ Admin approval | `dashboard.html` | Teach sessions, manage bookings |
| **Parent** | Self-register + Phone | ❌ Instant | `dashboard.html` | Add children, monitor progress |
| **Moderator** | Admin creates only | N/A | `dashboard.html` | Manage users, view reports |
| **Admin** | Pre-created | N/A | `admin-dashboard.html` | Approve Pro/Trainer, full control |

## 📁 File Structure

```
frontend/
├── home.html                    # Landing page (entry point)
├── index.html                   # Redirects to home.html
├── role-selector.html           # Choose role for signup
│
├── register-player.html         # Player registration
├── register-pro.html            # Pro Player registration (with CV upload)
├── register-trainer.html        # Trainer registration (with CV upload)
├── register-parent.html         # Parent registration (with phone)
│
├── login.html                   # Universal login (all roles)
│
├── dashboard.html               # General dashboard (redirects based on role)
├── admin-dashboard.html         # Admin approval interface
│
├── config.js                    # API configuration
├── README.md                    # Setup guide
├── TESTING.md                   # Testing checklist
└── SYSTEM_OVERVIEW.md           # This file
```

## 🔄 User Flow Diagrams

### Registration Flow

```
Home Page (home.html)
    │
    ├─→ Login (login.html) ────→ Dashboard
    │
    └─→ Sign Up
         │
         └─→ Role Selector (role-selector.html)
              │
              ├─→ Player Registration ────────→ Instant Access → Login
              ├─→ Pro Registration (+ CV) ────→ Pending Approval → Wait for email
              ├─→ Trainer Registration (+ CV) ─→ Pending Approval → Wait for email
              └─→ Parent Registration ────────→ Instant Access → Login
```

### Admin Approval Flow

```
Pro/Trainer Registers
    │
    ↓
Backend creates user with isApproved = false
    │
    ↓
Admin Dashboard shows in "Pending" list
    │
    ├─→ Admin Approves
    │    │
    │    ├─→ isApproved = true
    │    ├─→ Email sent to user
    │    └─→ User can now login
    │
    └─→ Admin Rejects
         │
         ├─→ User deleted from database
         └─→ Rejection email sent
```

### Login Flow

```
Login Page (login.html)
    │
    ├─→ Valid credentials
    │    │
    │    ├─→ Check if Pro/Trainer
    │    │    │
    │    │    ├─→ isApproved = true → Success → Dashboard
    │    │    └─→ isApproved = false → Error: "Pending approval"
    │    │
    │    └─→ Other roles → Success → Dashboard
    │
    └─→ Invalid credentials → Error message
```

## 🔌 Backend API Endpoints

### Public Endpoints (No Auth Required)

| Endpoint | Method | Purpose | Request Body |
|----------|--------|---------|--------------|
| `/api/v1/auth/login` | POST | Login | `{ username, password }` |
| `/api/v1/player/register` | POST | Register Player | `{ username, firstName, lastName, email, password }` |
| `/api/v1/pro/register` | POST | Register Pro | FormData: `pro` (JSON) + `file` (PDF) |
| `/api/v1/trainer/register` | POST | Register Trainer | FormData: `trainer` (JSON) + `file` (PDF) |
| `/api/v1/parent/register` | POST | Register Parent | `{ username, firstName, lastName, email, phoneNumber, password }` |

### Admin Endpoints (ADMIN role required)

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/v1/pro/get` | GET | List all Pro players |
| `/api/v1/pro/approve/{proId}` | POST | Approve Pro player |
| `/api/v1/pro/reject/{proId}` | PUT | Reject Pro player |
| `/api/v1/pro/cv/{proId}` | GET | Download Pro CV |
| `/api/v1/trainer/get` | GET | List all Trainers |
| `/api/v1/trainer/approve-trainer/{trainerId}` | POST | Approve Trainer |
| `/api/v1/trainer/reject-trainer/{trainerId}` | PUT | Reject Trainer |
| `/api/v1/trainer/cv/{trainerId}` | GET | Download Trainer CV |
| `/api/v1/moderator/register` | POST | Create Moderator |
| `/api/v1/user/get-all` | GET | Get all users |

## 🎨 Design System

### Colors

```css
--accent: #22d3ee (Cyan)
--accent-2: #a855f7 (Purple)
--success: #4ade80 (Green)
--error: #f87171 (Red)
--warning: #fbbf24 (Yellow)
--text: #e2e8f0 (Light Gray)
--muted: #94a3b8 (Muted Gray)
```

### Role Colors

- **Player**: Cyan (#22d3ee)
- **Pro Player**: Purple (#a855f7)
- **Trainer**: Cyan (#22d3ee)
- **Parent**: Cyan (#22d3ee)
- **Admin**: Purple (#a855f7)

## 🔐 Security & Authentication

### JWT Token Storage

```javascript
localStorage.setItem('authToken', token);
localStorage.setItem('userRole', role);
localStorage.setItem('tokenType', 'Bearer');
```

### Protected API Calls

```javascript
fetch('/api/v1/endpoint', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
```

### Role-Based Redirects

After login, users are redirected based on role:
- **ADMIN** → `admin-dashboard.html`
- **Other roles** → `dashboard.html` (can be extended for role-specific dashboards)

## 📊 Database Models

### User (Base)
```
- id
- username (unique, 3-30 chars)
- password (hashed, min 8 chars)
- email (unique, valid format)
- firstName
- lastName
- role (ADMIN, MODERATOR, PLAYER, PRO, PARENTS, TRAINER)
- createdAt
```

### Pro (extends User)
```
- id
- user (OneToOne)
- cvPath (PDF file path)
- isApproved (boolean, default: false)
- phoneNumber (optional)
- statistics
```

### Trainer (extends User)
```
- id
- user (OneToOne)
- cvPath (PDF file path)
- isApproved (boolean, default: false)
- isAvailable (boolean)
```

### Parent (extends User)
```
- id
- user (OneToOne)
- phoneNumber (Saudi format: 05XXXXXXXX)
- children (OneToMany)
```

### Player (extends User)
```
- id
- user (OneToOne)
- statistics
```

## 🚀 Deployment Checklist

### Frontend Setup

1. **Copy files to Spring Boot static folder:**
   ```bash
   cp -r frontend/* src/main/resources/static/
   ```

2. **Update config.js for production:**
   ```javascript
   API_BASE_URL: 'https://your-domain.com'
   ```

3. **Test all registration flows**

4. **Test admin approval workflow**

### Backend Configuration

1. **Ensure CORS allows frontend origin:**
   ```java
   cfg.setAllowedOrigins(List.of("https://your-frontend-domain.com"));
   ```

2. **Configure JWT secret (production):**
   ```properties
   jwt.secret=your-production-secret-key
   jwt.expiration=86400000
   ```

3. **Set up email service:**
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

4. **Create initial admin user** (via database or seed script)

## 🧪 Testing Scenarios

### 1. Player Registration & Login
- Register as Player → Instant access
- Login → See dashboard
- No approval needed

### 2. Pro Player Registration & Approval
- Register as Pro with CV → Pending status
- Try to login → Error: "Pending approval"
- Admin approves → Email sent
- Login → Success → Dashboard

### 3. Trainer Registration & Approval
- Register as Trainer with CV → Pending status
- Try to login → Error: "Pending approval"
- Admin approves → Email sent
- Login → Success → Dashboard

### 4. Parent Registration
- Register as Parent with phone → Instant access
- Login → See dashboard
- Can add children

### 5. Admin Approval Workflow
- Login as Admin → Admin dashboard
- See pending Pro/Trainer list
- View CV (download PDF)
- Approve or Reject
- Verify email sent to applicant

## 📝 Important Notes

### CV Upload Requirements
- **Format**: PDF only
- **Max Size**: 10MB
- **Required for**: Pro Player, Trainer
- **Storage**: `uploads/cvs/` directory

### Phone Number Format (Parent)
- Saudi format only
- Patterns: `05XXXXXXXX`, `+9665XXXXXXXX`, `9665XXXXXXXX`
- Validated on frontend and backend

### Moderator Creation
- **Only Admin can create Moderators**
- No self-registration
- Created via admin interface or API

### Email Notifications
- Welcome email on registration (Player, Parent)
- Approval email (Pro, Trainer)
- Rejection email (Pro, Trainer)
- Requires email configuration in `application.properties`

## 🔧 Customization Guide

### Adding New Role

1. **Backend**: Create DTO, Model, Repository, Service, Controller
2. **Frontend**: Create `register-{role}.html`
3. **Update**: `role-selector.html` to include new role
4. **Security**: Add role to `ConfigurationSecurity.java`
5. **Dashboard**: Create role-specific dashboard if needed

### Changing Approval Workflow

1. **Backend**: Modify `ProService.approveProByAdmin()` or `TrainerService.approveTrainerByAdmin()`
2. **Frontend**: Update `admin-dashboard.html` approval buttons
3. **Email**: Customize email templates in service methods

### Adding More Admin Features

1. **Backend**: Add endpoints in respective controllers
2. **Frontend**: Add tabs/sections in `admin-dashboard.html`
3. **Security**: Ensure endpoints require ADMIN role

## 🐛 Common Issues & Solutions

### Issue: CORS Error
**Solution**: Add frontend origin to `ConfigurationSecurity.java`

### Issue: CV Upload Fails
**Solution**: 
- Check file size < 10MB
- Ensure `uploads/cvs/` directory exists
- Verify multipart config in `application.properties`

### Issue: Admin Can't See Pro/Trainer List
**Solution**: 
- Endpoints `/api/v1/pro/get` and `/api/v1/trainer/get` require MODERATOR role
- Either grant MODERATOR role to ADMIN or create admin-specific endpoints

### Issue: Email Not Sending
**Solution**:
- Verify Gmail credentials in `application.properties`
- Enable "Less secure app access" or use App Password
- Check spam folder

## 📞 Support & Maintenance

### Regular Tasks
- Monitor pending approvals
- Review rejected applications
- Check email delivery logs
- Backup CV files
- Monitor user registrations

### Monitoring Endpoints
- Health check: `/actuator/health` (if enabled)
- User count: `/api/v1/user/get-all` (ADMIN only)

---

**Built with ❤️ for LevelUp Academy**  
Version 1.0 | December 2024

