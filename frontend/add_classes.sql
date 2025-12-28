-- Add training classes (sessions) to LevelUp Academy database
-- Run this script in your MySQL database
-- Make sure you have trainers and games in the database first

USE levelup1;

-- Get trainer IDs (assuming trainers from add_trainers.sql exist)
SET @trainer_ahmed_id = (SELECT id FROM trainer WHERE user_id = (SELECT id FROM user WHERE username = 'trainer_ahmed'));
SET @trainer_sarah_id = (SELECT id FROM trainer WHERE user_id = (SELECT id FROM user WHERE username = 'trainer_sarah'));

-- Get game IDs (assuming games from add_games.sql exist)
SET @fc26_id = (SELECT id FROM game WHERE name = 'FC26');
SET @lol_id = (SELECT id FROM game WHERE name = 'LOL');
SET @cod_id = (SELECT id FROM game WHERE name = 'CALL OF DUTY');

-- Class 1: FC26 Training with Ahmed
-- Start: Tomorrow at 1:00 PM, End: Tomorrow at 3:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Advanced FC26 Training',
    DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 13 HOUR,  -- Tomorrow 1:00 PM
    DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 15 HOUR,  -- Tomorrow 3:00 PM
    10,  -- 10 available seats
    '01:00-03:00 pm',
    @trainer_ahmed_id,
    @fc26_id
);

-- Class 2: League of Legends Basics with Sarah
-- Start: Day after tomorrow at 2:00 PM, End: Day after tomorrow at 4:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'LOL Basics Training',
    DATE_ADD(NOW(), INTERVAL 2 DAY) + INTERVAL 14 HOUR,  -- Day after tomorrow 2:00 PM
    DATE_ADD(NOW(), INTERVAL 2 DAY) + INTERVAL 16 HOUR,  -- Day after tomorrow 4:00 PM
    8,  -- 8 available seats
    '02:00-04:00 pm',
    @trainer_sarah_id,
    @lol_id
);

-- Class 3: Call of Duty Advanced with Ahmed
-- Start: 3 days from now at 3:00 PM, End: 3 days from now at 5:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Call of Duty Advanced',
    DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 15 HOUR,  -- 3 days from now 3:00 PM
    DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 17 HOUR,  -- 3 days from now 5:00 PM
    12,  -- 12 available seats
    '03:00-05:00 pm',
    @trainer_ahmed_id,
    @cod_id
);

-- Class 4: FC26 Intermediate with Sarah
-- Start: 4 days from now at 10:00 AM, End: 4 days from now at 12:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'FC26 Intermediate',
    DATE_ADD(NOW(), INTERVAL 4 DAY) + INTERVAL 10 HOUR,  -- 4 days from now 10:00 AM
    DATE_ADD(NOW(), INTERVAL 4 DAY) + INTERVAL 12 HOUR,  -- 4 days from now 12:00 PM
    15,  -- 15 available seats
    '10:00-12:00 am',
    @trainer_sarah_id,
    @fc26_id
);

-- Class 5: LOL Team Strategy with Ahmed
-- Start: 5 days from now at 6:00 PM, End: 5 days from now at 8:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'LOL Team Strategy',
    DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 18 HOUR,  -- 5 days from now 6:00 PM
    DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 20 HOUR,  -- 5 days from now 8:00 PM
    6,  -- 6 available seats
    '06:00-08:00 pm',
    @trainer_ahmed_id,
    @lol_id
);

-- Class 6: Call of Duty Beginner with Sarah
-- Start: 6 days from now at 11:00 AM, End: 6 days from now at 1:00 PM
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Call of Duty Beginner',
    DATE_ADD(NOW(), INTERVAL 6 DAY) + INTERVAL 11 HOUR,  -- 6 days from now 11:00 AM
    DATE_ADD(NOW(), INTERVAL 6 DAY) + INTERVAL 13 HOUR,  -- 6 days from now 1:00 PM
    20,  -- 20 available seats
    '11:00-01:00 pm',
    @trainer_sarah_id,
    @cod_id
);

-- Verify the classes were added
SELECT 
    s.id,
    s.name,
    s.start_date,
    s.end_date,
    s.time,
    s.available_sets,
    u.first_name AS trainer_first_name,
    u.last_name AS trainer_last_name,
    g.name AS game_name
FROM session s
JOIN trainer t ON s.trainer_id = t.id
JOIN user u ON t.user_id = u.id
JOIN game g ON s.game_id = g.id
ORDER BY s.start_date;

