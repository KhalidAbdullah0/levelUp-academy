# Player Registration and Subscription Flow - Complete Guide

## Overview
This document describes the complete player flow from registration to seat confirmation, implementing all 7 user stories.

## Flow Diagram

```
Landing Page (home.html)
    ↓
[Click Sign Up]
    ↓
Role Selector (role-selector.html)
    ↓
[Select Player]
    ↓
Player Registration (register-player.html)
    ↓
[Successful Registration]
    ↓
Registration Success (registration-success.html)
    ↓
[Continue to Subscription]
    ↓
Select Package (player-subscription.html)
    ↓
[Select Package & Continue]
    ↓
Select Class (player-select-class.html)
    ↓
[Select Class & Continue]
    ↓
Payment (player-payment-flow.html)
    ↓
[Payment Successful]
    ↓
Seat Confirmation (seat-confirmation.html)
    ↓
[Done!]
```

## User Stories Implementation

### US-1: Choose Role from Landing Page ✅
**File:** `home.html`
- Landing page shows "Sign Up" button
- Clicking Sign Up redirects to `role-selector.html`
- Role selector shows: Parents, Trainer, Player, Pro Player
- Selecting Player redirects to `register-player.html`

### US-2: Register as Player ✅
**File:** `register-player.html`
- Form fields: First name, Last name, Username, Email, Password, Re-password
- All fields are required
- Client-side validation:
  - Username: 3-30 characters
  - Email: Valid email format
  - Password: Minimum 8 characters
  - Re-password: Must match password
- Server-side validation:
  - Username must be unique
  - Email must be unique
  - Error messages displayed for validation failures
- On success:
  - User record created with role PLAYER
  - Player record created and linked to user
  - Redirects to `registration-success.html?username={username}`

### US-3: See Successful Registration Message ✅
**File:** `registration-success.html`
- Displays: "Welcome Player {username}, You have been successfully registered!"
- Username appears in header (e.g., "Player_sultan1")
- User is considered authenticated (JWT stored in localStorage)
- "Continue to Subscription" button → `player-subscription.html`

### US-4: Select Subscription Package ✅
**File:** `player-subscription.html`
- Three packages displayed:
  - **Basic**: 200 SAR/month, 4 classes
  - **Standard**: 300 SAR/month, 8 classes (Popular badge)
  - **Premium**: 600 SAR/month, 12 classes
- Each package has a "Select Package" button
- Selected package is highlighted
- "Continue to Select Class" button → `player-select-class.html`
- Selected package stored in sessionStorage

### US-5: Select Training Class ✅
**File:** `player-select-class.html`
- Displays list of available classes as cards
- Each class card shows:
  - Class name
  - Game name
  - Time range (e.g., "01:00–03:00 pm")
  - Trainer name
  - Available seats
- Only classes with at least 1 available seat are selectable
- Clicking a class card selects it (stored in sessionStorage)
- "Continue to Payment" button → `player-payment-flow.html`

### US-6: Enter Payment Details ✅
**File:** `player-payment-flow.html`
- Order summary shows:
  - Selected package
  - Selected class
  - Class time
  - Total amount
- Payment form fields:
  - Card number (13-16 digits)
  - Name on card
  - Expiration month (1-12)
  - Expiration year (must be future)
  - CVV (3-4 digits)
- Validation:
  - All fields required
  - Card number format checked
  - Expiry date must be in future
  - CVV must be numeric
- On valid input:
  - Creates subscription via `/api/v1/subscription/{packageType}`
  - Creates booking via `/api/v1/booking/add/{sessionId}/{subscriptionId}`
  - Decreases available seats
  - Redirects to `seat-confirmation.html` on success

### US-7: Confirm Reserved Seat ✅
**File:** `seat-confirmation.html`
- Shows confirmation message
- Displays booking details:
  - Package name
  - Class name
  - Class time
  - Booking status: "✓ Confirmed"
- "Reserve Seat" button (booking already confirmed in payment step)
- "Go to Dashboard" button → `player-dashboard.html`
- "View All Classes" button → `player-classes.html`

## Backend Changes

### Subscription Service
- Updated to return subscription ID in JSON response:
  ```json
  {
    "message": "Subscription created successfully",
    "subscriptionId": 123
  }
  ```

### Player Service
- Updated `getMyStatisticsByPlayerId()` to:
  - Accept User ID (not Player ID)
  - Find Player by User ID
  - Create default statistics if none exist

### Trainer Controller
- Updated to handle PLAYER role for viewing trainers

### Security Configuration
- Players can access:
  - `/api/v1/game/get`
  - `/api/v1/trainer/get`
  - `/api/v1/session/get`
  - `/api/v1/player-statistic/leaderboard`
  - `/api/v1/payments/card`
  - `/api/v1/subscription/*`

## Files Created/Updated

### New Files:
1. `registration-success.html` - US-3
2. `player-subscription.html` - US-4
3. `player-select-class.html` - US-5
4. `player-payment-flow.html` - US-6
5. `seat-confirmation.html` - US-7

### Updated Files:
1. `register-player.html` - Updated redirect to success page
2. `home.html` - Already has Sign Up button
3. `SubscriptionService.java` - Returns subscription ID
4. `PlayerService.java` - Fixed statistics retrieval
5. `TrainerController.java` - Added PLAYER access

## Testing the Flow

1. **Start Backend:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Test Flow:**
   - Go to `http://localhost:8080/home.html`
   - Click "Sign Up" → Select "Player"
   - Fill registration form → Submit
   - See success message
   - Select subscription package
   - Select a class
   - Enter payment details
   - See seat confirmation

## Notes

- All pages require authentication (except registration)
- SessionStorage is used to pass data between flow pages
- JWT token stored in localStorage for authentication
- Payment uses Moyasar API (configure API key in `application.properties`)
- Default statistics are created automatically for new players
- Available seats are decreased when booking is created

## Error Handling

- Form validation errors shown in real-time
- API errors displayed to user
- 401/403 errors redirect to login
- Payment failures show error message
- Missing selections redirect to previous step

