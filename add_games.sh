#!/bin/bash

# Script to add games to LevelUp Academy database
# Usage: ./add_games.sh

echo "Adding games to LevelUp Academy database..."

mysql -u root -p levelup1 < frontend/add_games.sql

echo "Games added successfully!"
echo ""
echo "Added games:"
echo "  - FC26 (Sports, Age 10+)"
echo "  - LOL (MOBA, Age 13+)"
echo "  - CALL OF DUTY (FPS, Age 18+)"
