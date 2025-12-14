-- Create Admin User for LevelUp Academy
-- Username: admin
-- Password: admin123

-- First, check if admin already exists and delete if needed
DELETE FROM users WHERE username = 'admin';

-- Insert admin user with BCrypt hashed password
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

-- Verify the admin was created
SELECT id, username, email, first_name, last_name, role, created_at 
FROM users 
WHERE username = 'admin';

