#!/usr/bin/env python3
"""
Script to update all HTML pages with new design system (header, footer, colors)
"""
import os
import re
from pathlib import Path

# Header template
HEADER = '''  <!-- Header -->
  <nav class="lu-header">
    <a href="home.html" class="lu-logo-link">
      <img src="/assets/logo/logo.png" alt="LevelUp Academy" class="lu-logo-img" onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
      <span style="display:none; font-size: 24px; font-weight: 700; background: var(--lu-gradient-brand); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">🎮 LevelUp Academy</span>
    </a>
  </nav>

'''

# Footer template  
FOOTER = '''  <!-- Footer -->
  <footer class="lu-footer">
    <div class="lu-footer-inner">
      <a href="home.html" class="lu-footer-logo-link">
        <img src="/assets/logo/logo.png" alt="LevelUp Academy" class="lu-footer-logo-img" onerror="this.style.display='none'; this.nextElementSibling.style.display='inline';">
        <span style="display:none; font-size: 18px; font-weight: 700; background: var(--lu-gradient-brand); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">🎮 LevelUp Academy</span>
      </a>
      <p class="lu-footer-text">© 2024 LevelUp Academy. All rights reserved.</p>
    </div>
  </footer>
'''

def update_file(filepath):
    """Update a single HTML file with header/footer and styles"""
    print(f"Processing: {filepath}")
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Skip if already has header
    if 'lu-header' in content:
        print(f"  ✓ Already has header")
        return False
    
    # Add styles.css link if not present
    if 'rel="stylesheet" href="styles.css"' not in content:
        # Find the last link or style tag before closing head
        pattern = r'(</head>)'
        replacement = r'  <link rel="stylesheet" href="styles.css">\n\1'
        content = re.sub(pattern, replacement, content)
    
    # Replace old color variables with new ones
    color_replacements = [
        (r'--bg:\s*#0f172a', '--bg: var(--lu-bg)'),
        (r'--card:\s*#111827', '--card: var(--lu-surface)'),
        (r'--accent:\s*#22d3ee', '--accent: var(--lu-primary-light)'),
        (r'--accent-2:\s*#a855f7', '--accent-2: var(--lu-accent)'),
        (r'--text:\s*#e2e8f0', '--text: var(--lu-text-primary)'),
        (r'--muted:\s*#94a3b8', '--muted: var(--lu-text-muted)'),
    ]
    for old, new in color_replacements:
        content = re.sub(old, new, content)
    
    # Add header after <body>
    if '<body>' in content and HEADER.strip() not in content:
        content = re.sub(r'(<body[^>]*>)', r'\1\n' + HEADER, content, count=1)
    
    # Add footer before </body>
    if '</body>' in content and FOOTER.strip() not in content:
        content = re.sub(r'(</body>)', FOOTER + '\n\1', content, count=1)
    
    # Add padding-top for fixed header
    if 'padding-top:' not in content and 'body {' in content:
        content = re.sub(r'(body\s*\{[^}]*)(min-height:)', r'\1padding-top: 80px;\n    \2', content)
    
    # Write back
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"  ✅ Updated")
    return True

def main():
    """Update all HTML files"""
    dirs = ['frontend', 'src/main/resources/static']
    files_updated = 0
    
    for dir_path in dirs:
        if not os.path.exists(dir_path):
            continue
            
        for html_file in Path(dir_path).glob('*.html'):
            # Skip certain files
            if 'admin-dashboard' in str(html_file) or 'dashboard' in str(html_file):
                # These might have custom headers, skip for now
                continue
                
            if update_file(html_file):
                files_updated += 1
    
    print(f"\n✅ Updated {files_updated} files")

if __name__ == '__main__':
    main()




