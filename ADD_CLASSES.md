# Add Training Classes to Database

This guide explains how to add training classes (sessions) to the LevelUp Academy database.

## Prerequisites

Before adding classes, make sure you have:
1. ✅ Games added to the database (run `add_games.sql`)
2. ✅ Trainers added to the database (run `add_trainers.sql`)

## SQL Scripts

### Option 1: Dynamic Dates (`add_classes.sql`)
This script creates classes with dates relative to the current date:
- Classes are scheduled 1-6 days from when you run the script
- Automatically calculates future dates

### Option 2: Fixed Dates (`add_classes_simple.sql`)
This script creates classes with specific fixed dates:
- Dates are set to December 2025
- You can easily modify the dates in the script

## How to Run

### Using MySQL Command Line:
```bash
mysql -u your_username -p levelup1 < frontend/add_classes.sql
```

### Using MySQL Workbench or phpMyAdmin:
1. Open the SQL script file
2. Copy the contents
3. Paste into the SQL query window
4. Execute the script

### Using Terminal (if MySQL is in PATH):
```bash
mysql -u root -p
```
Then:
```sql
USE levelup1;
SOURCE /path/to/add_classes.sql;
```

## What Gets Added

The script adds **6 training classes**:

1. **Advanced FC26 Training** - Ahmed Al-Saud - 10 seats
2. **LOL Basics Training** - Sarah Al-Mansouri - 8 seats
3. **Call of Duty Advanced** - Ahmed Al-Saud - 12 seats
4. **FC26 Intermediate** - Sarah Al-Mansouri - 15 seats
5. **LOL Team Strategy** - Ahmed Al-Saud - 6 seats
6. **Call of Duty Beginner** - Sarah Al-Mansouri - 20 seats

## Customizing the Script

### To add more classes:
Copy one of the INSERT statements and modify:
- `name`: Class name
- `start_date`: Start date and time (format: 'YYYY-MM-DD HH:MM:SS')
- `end_date`: End date and time
- `available_sets`: Number of available seats
- `time`: Time display string (e.g., '01:00-03:00 pm')
- `trainer_id`: ID of the trainer (use @trainer_ahmed_id or @trainer_sarah_id)
- `game_id`: ID of the game (use @fc26_id, @lol_id, or @cod_id)

### Example:
```sql
INSERT INTO session (name, start_date, end_date, available_sets, time, trainer_id, game_id)
VALUES (
    'My Custom Class',
    '2025-12-26 14:00:00',
    '2025-12-26 16:00:00',
    10,
    '02:00-04:00 pm',
    @trainer_ahmed_id,
    @fc26_id
);
```

## Verifying Classes

After running the script, you can verify the classes were added by running the SELECT query at the end of the script, or manually:

```sql
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
```

## Troubleshooting

### Error: "Unknown column 'trainer_id'"
- Make sure the session table has the correct foreign key columns
- Check that trainers and games exist first

### Error: "Cannot find trainer/game"
- Run `add_trainers.sql` and `add_games.sql` first
- Or manually set the IDs in the script

### Dates are in the past
- Use `add_classes_simple.sql` and update the dates to future dates
- Or modify `add_classes.sql` to use DATE_ADD with larger intervals

## Notes

- All classes are created with future dates
- Available seats can be booked by players
- Classes are linked to trainers and games via foreign keys
- The `time` field is a display string and doesn't need to match the actual datetime

