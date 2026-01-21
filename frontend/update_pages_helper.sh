#!/bin/bash
# Helper script to copy frontend files to static resources
# This ensures both locations stay in sync

FRONTEND_DIR="frontend"
STATIC_DIR="src/main/resources/static"

# Copy all HTML files and CSS
cp "$FRONTEND_DIR"/*.html "$STATIC_DIR/" 2>/dev/null || true
cp "$FRONTEND_DIR"/*.css "$STATIC_DIR/" 2>/dev/null || true
cp "$FRONTEND_DIR"/styles.css "$STATIC_DIR/" 2>/dev/null || true

echo "✅ Files synced to static resources"

