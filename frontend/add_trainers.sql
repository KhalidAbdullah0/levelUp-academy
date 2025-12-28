-- Add two trainers to LevelUp Academy database
-- Run this script in your MySQL database
-- Password for both trainers: Trainer123!

USE levelup1;

-- Trainer 1: Ahmed Al-Saud
-- First, create the User account
-- Password hash for "Trainer123!" using BCrypt
INSERT INTO user (username, password, email, first_name, last_name, role)
VALUES (
    'trainer_ahmed',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- Password: Trainer123!
    'ahmed.trainer@levelup.com',
    'Ahmed',
    'Al-Saud',
    'TRAINER'
);

-- Get the user ID
SET @trainer1_user_id = LAST_INSERT_ID();

-- Create the Trainer record
INSERT INTO trainer (user_id, cv_path, is_available, is_approved)
VALUES (
    @trainer1_user_id,
    'uploads/cvs/trainer_ahmed_cv.pdf', -- Placeholder CV path (you can update this later)
    true,  -- Available for sessions
    true   -- Approved (set to false if you want admin to approve manually)
);

-- Trainer 2: Sarah Al-Mansouri
-- Create the User account
INSERT INTO user (username, password, email, first_name, last_name, role)
VALUES (
    'trainer_sarah',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- Password: Trainer123!
    'sarah.trainer@levelup.com',
    'Sarah',
    'Al-Mansouri',
    'TRAINER'
);

-- Get the user ID
SET @trainer2_user_id = LAST_INSERT_ID();

-- Create the Trainer record
INSERT INTO trainer (user_id, cv_path, is_available, is_approved)
VALUES (
    @trainer2_user_id,
    'uploads/cvs/trainer_sarah_cv.pdf', -- Placeholder CV path (you can update this later)
    true,  -- Available for sessions
    true   -- Approved (set to false if you want admin to approve manually)
);

-- Verify the trainers were added
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

