# Add Games to LevelUp Academy

## Games to Add:
1. **FC26** - Sports game (Age 10+)
2. **LOL** - MOBA game (Age 13+)
3. **CALL OF DUTY** - FPS game (Age 18+)

## Method 1: Using SQL Script (Recommended)

### Step 1: Run the SQL script

Open your MySQL terminal or MySQL Workbench and run:

```bash
mysql -u root -p levelup1 < frontend/add_games.sql
```

Or manually execute the SQL:

```sql
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
```

### Step 2: Verify games were added

```sql
SELECT * FROM game WHERE name IN ('FC26', 'LOL', 'CALL OF DUTY');
```

## Method 2: Using Admin Dashboard (If you have MODERATOR access)

If you have a MODERATOR account, you can add games through the API:

```bash
# Login as MODERATOR first to get token
# Then use the token to add games:

curl -X POST http://localhost:8080/api/v1/game/add \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "FC26",
    "age": 10,
    "category": "Sports"
  }'

curl -X POST http://localhost:8080/api/v1/game/add \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "LOL",
    "age": 13,
    "category": "MOBA"
  }'

curl -X POST http://localhost:8080/api/v1/game/add \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "CALL OF DUTY",
    "age": 18,
    "category": "FPS"
  }'
```

## Method 3: Quick SQL Commands

If you're already in MySQL:

```sql
USE levelup1;
INSERT INTO game (name, age, category) VALUES 
('FC26', 10, 'Sports'),
('LOL', 13, 'MOBA'),
('CALL OF DUTY', 18, 'FPS');
```

## After Adding Games

1. Restart your Spring Boot backend (if running)
2. Visit `http://localhost:8080/player-games.html` to see the new games
3. Games will appear in the player dashboard games page

## Game Details

| Game Name | Category | Age Requirement |
|-----------|----------|-----------------|
| FC26 | Sports | 10+ |
| LOL | MOBA | 13+ |
| CALL OF DUTY | FPS | 18+ |

