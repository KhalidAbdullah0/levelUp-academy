#!/bin/bash

echo "🔧 Restarting LevelUp Academy Backend..."
echo ""
echo "This will:"
echo "1. Stop any running backend"
echo "2. Start fresh backend with new security configuration"
echo ""

cd /Users/abdullahalzubaidi/levelUp-academy

# Kill any existing Spring Boot process
echo "📛 Stopping any running backend..."
pkill -f "spring-boot:run" 2>/dev/null || true
pkill -f "levelup_academy" 2>/dev/null || true
sleep 2

echo ""
echo "🚀 Starting backend..."
echo ""
echo "⏳ Please wait for 'Started LevelUpAcademyApplication' message..."
echo ""

./mvnw spring-boot:run

