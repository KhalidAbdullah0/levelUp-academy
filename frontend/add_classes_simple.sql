-- Add training classes (sessions) to LevelUp Academy database - Simple Version
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

-- If trainers or games don't exist, you can manually set IDs:
-- SET @trainer_ahmed_id = 1;
-- SET @trainer_sarah_id = 2;
-- SET @fc26_id = 1;
-- SET @lol_id = 2;
-- SET @cod_id = 3;

-- Class 1: FC26 Training with Ahmed
-- Start: 2025-12-20 13:00:00, End: 2025-12-20 15:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Advanced FC26 Training',
    '2025-12-20 13:00:00',
    '2025-12-20 15:00:00',
    10,
    '01:00-03:00 pm',
    @trainer_ahmed_id,
    @fc26_id
);

-- Class 2: League of Legends Basics with Sarah
-- Start: 2025-12-21 14:00:00, End: 2025-12-21 16:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'LOL Basics Training',
    '2025-12-21 14:00:00',
    '2025-12-21 16:00:00',
    8,
    '02:00-04:00 pm',
    @trainer_sarah_id,
    @lol_id
);

-- Class 3: Call of Duty Advanced with Ahmed
-- Start: 2025-12-22 15:00:00, End: 2025-12-22 17:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Call of Duty Advanced',
    '2025-12-22 15:00:00',
    '2025-12-22 17:00:00',
    12,
    '03:00-05:00 pm',
    @trainer_ahmed_id,
    @cod_id
);

-- Class 4: FC26 Intermediate with Sarah
-- Start: 2025-12-23 10:00:00, End: 2025-12-23 12:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'FC26 Intermediate',
    '2025-12-23 10:00:00',
    '2025-12-23 12:00:00',
    15,
    '10:00-12:00 am',
    @trainer_sarah_id,
    @fc26_id
);

-- Class 5: LOL Team Strategy with Ahmed
-- Start: 2025-12-24 18:00:00, End: 2025-12-24 20:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'LOL Team Strategy',
    '2025-12-24 18:00:00',
    '2025-12-24 20:00:00',
    6,
    '06:00-08:00 pm',
    @trainer_ahmed_id,
    @lol_id
);

-- Class 6: Call of Duty Beginner with Sarah
-- Start: 2025-12-25 11:00:00, End: 2025-12-25 13:00:00
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'Call of Duty Beginner',
    '2025-12-25 11:00:00',
    '2025-12-25 13:00:00',
    20,
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

