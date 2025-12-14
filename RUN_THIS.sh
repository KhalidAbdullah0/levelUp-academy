#!/bin/bash
# Quick script to create admin user in MySQL

echo "🔧 Creating Admin User for LevelUp Academy..."
echo ""
echo "Username: admin"
echo "Password: admin123"
echo ""
echo "Please enter your MySQL root password when prompted:"
echo ""

# Run the SQL file
mysql -u root -p levelup1 < create_admin_user.sql

echo ""
echo "✅ Done! Try logging in now:"
echo "   http://localhost:8080/login.html"
echo ""
echo "   Username: admin"
echo "   Password: admin123"

