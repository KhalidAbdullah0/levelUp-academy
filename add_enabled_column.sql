-- Add enabled column to user table
ALTER TABLE `user` ADD COLUMN `enabled` BOOLEAN DEFAULT TRUE;

-- Set all existing users to enabled (true)
UPDATE `user` SET `enabled` = TRUE WHERE `enabled` IS NULL;
