-- Manually add enabled column to user table
-- Run this SQL command in your MySQL database

USE levelup1;

-- Check if column already exists, if not add it
SET @dbname = DATABASE();
SET @tablename = 'user';
SET @columnname = 'enabled';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT "Column already exists" AS result;',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` BOOLEAN DEFAULT TRUE;')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Set all existing users to enabled (true)
UPDATE `user` SET `enabled` = TRUE WHERE `enabled` IS NULL;
