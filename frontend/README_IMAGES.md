# Image Assets Setup

## Required Images

Please place the following images in the specified directories:

### Logo
- **Path:** `/assets/logo/levelup-logo.png`
- **Description:** The LevelUp Academy logo (orange gradient logo with gaming controller)
- **Recommended Size:** 200x80px (will scale to 44px height in header, 28px in footer)

### Character Image
- **Path:** `/assets/characters/main-hero.png`
- **Description:** Main character artwork (fighter with red cap and vest)
- **Recommended Size:** 800x1200px (will scale responsively)

## Directory Structure

```
src/main/resources/static/
├── assets/
│   ├── logo/
│   │   └── levelup-logo.png
│   └── characters/
│       └── main-hero.png
```

## Fallback

If images are not found, the pages will:
- Show a fallback text with gradient styling
- Hide broken image placeholders
- Still function correctly without images

## To Add Images

1. Create the directories:
   ```bash
   mkdir -p src/main/resources/static/assets/logo
   mkdir -p src/main/resources/static/assets/characters
   ```

2. Place your images:
   - `levelup-logo.png` → `src/main/resources/static/assets/logo/`
   - `main-hero.png` → `src/main/resources/static/assets/characters/`

3. Restart the Spring Boot server to serve the new assets.




