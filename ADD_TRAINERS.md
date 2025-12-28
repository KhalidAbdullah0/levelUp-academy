# Add Two Trainers to LevelUp Academy

## Trainers to Add:
1. **Ahmed Al-Saud** - Username: `trainer_ahmed`
2. **Sarah Al-Mansouri** - Username: `trainer_sarah`

## Login Credentials:
- **Username**: `trainer_ahmed` or `trainer_sarah`
- **Password**: `Trainer123!` (for both)

## Method 1: Using SQL Script (Recommended)

### Step 1: Run the SQL script

Open your MySQL terminal or MySQL Workbench and run:

```bash
mysql -u root -p levelup1 < frontend/add_trainers.sql
```

Or manually execute the SQL:

```sql
USE levelup1;

-- Trainer 1: Ahmed Al-Saud
INSERT INTO user (username, password, email, first_name, last_name, role)
VALUES (
    'trainer_ahmed',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ahmed.trainer@levelup.com',
    'Ahmed',
    'Al-Saud',
    'TRAINER'
);

SET @trainer1_user_id = LAST_INSERT_ID();

INSERT INTO trainer (user_id, cv_path, is_available, is_approved)
VALUES (
    @trainer1_user_id,
    'uploads/cvs/trainer_ahmed_cv.pdf',
    true,
    true
);

-- Trainer 2: Sarah Al-Mansouri
INSERT INTO user (username, password, email, first_name, last_name, role)
VALUES (
    'trainer_sarah',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'sarah.trainer@levelup.com',
    'Sarah',
    'Al-Mansouri',
    'TRAINER'
);

SET @trainer2_user_id = LAST_INSERT_ID();

INSERT INTO trainer (user_id, cv_path, is_available, is_approved)
VALUES (
    @trainer2_user_id,
    'uploads/cvs/trainer_sarah_cv.pdf',
    true,
    true
);
```

### Step 2: Verify trainers were added

```sql
SELECT 
    u.id,
    u.username,
    u.first_name,
    u.last_name,
    u.email,
    u.role,
    t.id as trainer_id,
    t.is_approved,
    t.is_available
FROM user u
JOIN trainer t ON u.id = t.user_id
WHERE u.username IN ('trainer_ahmed', 'trainer_sarah');
```

## Method 2: Using Registration Form

You can also register trainers through the registration form at:
- `http://localhost:8080/register-trainer.html`

**Note**: This method requires uploading a CV file and will require admin approval.

## Trainer Details

| Trainer | Username | Email | Status |
|---------|----------|-------|--------|
| Ahmed Al-Saud | trainer_ahmed | ahmed.trainer@levelup.com | Approved & Available |
| Sarah Al-Mansouri | trainer_sarah | sarah.trainer@levelup.com | Approved & Available |

## After Adding Trainers

1. **Restart your Spring Boot backend** (if running)
2. **Login as trainer**:
   - Go to `http://localhost:8080/login.html`
   - Use username: `trainer_ahmed` or `trainer_sarah`
   - Password: `Trainer123!`
3. **View trainers**:
   - Visit `http://localhost:8080/player-trainers.html` to see the trainers
   - They will appear in the admin dashboard

## Notes

- Both trainers are set as **approved** (`is_approved = true`) so they can login immediately
- Both trainers are set as **available** (`is_available = true`) for sessions
- CV paths are placeholders - you can update them later if needed
- The password hash uses BCrypt encryption (same as admin user)

## Troubleshooting

If you get an error about duplicate username/email:
- The trainers might already exist
- Check with: `SELECT * FROM user WHERE username IN ('trainer_ahmed', 'trainer_sarah');`
- Delete and re-add if needed

