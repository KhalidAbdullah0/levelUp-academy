-- Fix Database Schema Issues
-- Run this in MySQL to fix the schema and create admin user

USE levelup1;

-- 1. Drop all problematic indexes on user table
ALTER TABLE users DROP INDEX IF EXISTS username;
ALTER TABLE users DROP INDEX IF EXISTS email;

-- 2. Recreate the table constraints properly
ALTER TABLE users 
  ADD UNIQUE KEY `idx_username` (`username`),
  ADD UNIQUE KEY `idx_email` (`email`);

-- 3. Delete existing admin if any
DELETE FROM users WHERE username = 'admin';

-- 4. Create admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role, registration)
VALUES (
  'admin',
  '$2a$10$N9qo8uLOickgx2ZrVzY45.1Ey67YkSal4/Z0GZtU6WBOvyWzLg9CO',
  'admin@levelupacademy.com',
  'Admin',
  'User',
  'ADMIN',
  CURDATE()
);

-- 5. Verify admin was created
SELECT id, username, email, first_name, last_name, role FROM users WHERE username = 'admin';

-- 6. Show all indexes on users table (should be clean now)
SHOW INDEXES FROM users;

