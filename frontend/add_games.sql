-- Add games to LevelUp Academy database
-- Run this script in your MySQL database

USE levelup1;

-- Insert FC26 (FIFA/Football Club 26)
INSERT INTO game (name, age, category) 
VALUES ('FC26', 10, 'Sports');

-- Insert LOL (League of Legends)
INSERT INTO game (name, age, category) 
VALUES ('LOL', 13, 'MOBA');

-- Insert CALL OF DUTY
INSERT INTO game (name, age, category) 
VALUES ('CALL OF DUTY', 18, 'FPS');

-- Verify the games were added
SELECT * FROM game WHERE name IN ('FC26', 'LOL', 'CALL OF DUTY');

