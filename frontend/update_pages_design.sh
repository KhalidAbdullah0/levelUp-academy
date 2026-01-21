#!/bin/bash
# Helper script to add header and footer to HTML pages
# This script adds the standard header and footer to HTML files that don't have them

add_header_footer() {
  local file=$1
  
  if [ ! -f "$file" ]; then
    echo "File not found: $file"
    return
  fi

  # Check if file already has header
  if grep -q "lu-header" "$file"; then
    echo "✓ $file already has header"
    return
  fi

  echo "Updating $file..."
  
  # Create backup
  cp "$file" "$file.bak"
  
  # This is a placeholder - manual updates needed for each file
  echo "⚠ Manual update needed for $file"
}

# Update files (this is a template - run manually for each)
echo "Please update each HTML file manually or use a more sophisticated script"
echo "Files that need header/footer:"
find frontend -name "*.html" -type f ! -name "index.html" ! -name "home.html" ! -name "login.html" ! -name "admin-dashboard*.html" | head -20




