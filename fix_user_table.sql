-- Fix user table: Add enabled column manually
-- This avoids the "Too many keys" error from Hibernate

USE levelup1;

-- Add enabled column if it doesn't exist
ALTER TABLE `user` 
ADD COLUMN IF NOT EXISTS `enabled` BOOLEAN DEFAULT TRUE;

-- Set all existing users to enabled
UPDATE `user` SET `enabled` = TRUE WHERE `enabled` IS NULL;
