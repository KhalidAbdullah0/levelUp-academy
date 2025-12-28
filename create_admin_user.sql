-- Create Admin User for LevelUp Academy
-- Username: admin
-- Password: admin123

USE levelup1;

-- Delete existing admin if any
DELETE FROM users WHERE username = 'admin';

-- Create admin user with BCrypt hashed password
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

-- Verify the admin was created
SELECT id, username, email, first_name, last_name, role 
FROM users 
WHERE username = 'admin';

