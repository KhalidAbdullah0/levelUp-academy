# Design System Update - Implementation Guide

## ✅ Completed
1. Created global CSS file (`styles.css`) with new color system
2. Updated `home.html` with hero section and character artwork
3. Updated `login.html` with logo header/footer and new colors

## 📋 Remaining Updates Needed

### Files Requiring Logo Header + Footer + Color Updates:

#### Critical Pages (High Priority):
- [ ] `role-selector.html`
- [ ] `register-player.html`
- [ ] `register-pro.html`
- [ ] `register-trainer.html`
- [ ] `register-parent.html`
- [ ] `admin-dashboard.html` (needs new colors applied)
- [ ] `pending-approval.html`

#### Player Flow Pages:
- [ ] `player-dashboard.html`
- [ ] `player-games.html`
- [ ] `player-trainers.html`
- [ ] `player-classes.html`
- [ ] `player-profile.html`
- [ ] `player-payment.html`
- [ ] `player-payment-flow.html`
- [ ] `player-subscription.html`
- [ ] `player-select-class.html`
- [ ] `registration-success.html`
- [ ] `seat-confirmation.html`

#### Other Pages:
- [ ] `dashboard.html`
- [ ] `signup.html`
- [ ] `add-training-class.html`

## 🎨 Design System Components

### Header Template:
```html
<!-- Header -->
<nav class="lu-header">
  <a href="home.html" class="lu-logo-link">
    <img src="/assets/logo/levelup-logo.png" alt="LevelUp Academy" class="lu-logo-img" onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
    <span style="display:none; font-size: 24px; font-weight: 700; background: var(--lu-gradient-brand); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">🎮 LevelUp Academy</span>
  </a>
  <!-- Additional nav items here -->
</nav>
```

### Footer Template:
```html
<!-- Footer -->
<footer class="lu-footer">
  <div class="lu-footer-inner">
    <a href="home.html" class="lu-footer-logo-link">
      <img src="/assets/logo/levelup-logo.png" alt="LevelUp Academy" class="lu-footer-logo-img" onerror="this.style.display='none'; this.nextElementSibling.style.display='inline';">
      <span style="display:none; font-size: 18px; font-weight: 700; background: var(--lu-gradient-brand); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">🎮 LevelUp Academy</span>
    </a>
    <p class="lu-footer-text">© 2024 LevelUp Academy. All rights reserved.</p>
  </div>
</footer>
```

### CSS Updates:
1. Add `<link rel="stylesheet" href="styles.css">` in `<head>`
2. Replace old color variables with new ones:
   - `--bg` → Use `var(--lu-bg)`
   - `--card` → Use `var(--lu-surface)`
   - `--accent` → Use `var(--lu-primary-light)`
   - `--text` → Use `var(--lu-text-primary)`
   - `--muted` → Use `var(--lu-text-muted)`
3. Update button gradients to use `var(--lu-gradient-brand)`

### Key Color Mappings:
- Primary actions: `var(--lu-gradient-brand)` (orange to gold)
- Success states: `var(--lu-success)` (green)
- Warning/Pending: `var(--lu-warning)` (yellow)
- Error/Danger: `var(--lu-danger)` (red)
- Background: `var(--lu-bg)` (near-black)
- Cards/Panels: `var(--lu-surface)` (dark gray)

## 📝 Implementation Steps per File:

1. Add `styles.css` link in `<head>`
2. Add header with logo at top of `<body>`
3. Update existing styles to use new color variables
4. Add footer at bottom of `<body>` (before closing `</body>` tag)
5. Ensure responsive design works (logo scales down on mobile)
6. Test that logo fallback text appears if image missing

## 🖼️ Asset Paths Required:
- Logo: `/assets/logo/levelup-logo.png`
- Character: `/assets/characters/main-hero.png`

If these assets don't exist yet, the fallback emoji/text will display.

