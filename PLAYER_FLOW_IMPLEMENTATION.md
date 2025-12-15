# Player Flow Implementation - Complete Guide

## Overview
This document describes the complete implementation of all 7 player user stories (US-PL-1 through US-PL-7) according to the detailed specifications.

## User Stories Implementation Status

### ✅ US-PL-1: Select Player Role from Landing Page
**Status:** Complete
- Landing page (`home.html`) has "Sign Up" button in header
- Clicking Sign Up redirects to `role-selector.html`
- Role selector shows: Parents, Trainer, Player, Pro Player
- Selecting Player navigates to `register-player.html`
- No backend API call required (pure navigation)

### ✅ US-PL-2: Register as Player
**Status:** Complete
- **Endpoint:** `POST /api/v1/player/register` (public, no JWT)
- **Request Body:**
  ```json
  {
    "firstName": "Sultan",
    "lastName": "Ahmed",
    "username": "sultan1",
    "email": "sultan@example.com",
    "password": "Player@123",
    "confirmPassword": "Player@123"
  }
  ```
- **Response (201 Created):**
  ```json
  {
    "id": 42,
    "username": "sultan1",
    "email": "sultan@example.com",
    "role": "PLAYER"
  }
  ```
- **Backend Behavior:**
  - Validates all fields (non-empty, email format, password match)
  - Checks uniqueness of username and email
  - Creates user record with BCrypt hashed password, role = 'PLAYER'
  - Creates player record linked to user.id
- **Frontend Behavior:**
  - Form validation (client-side)
  - Shows validation errors under fields
  - On success, automatically triggers login (US-PL-3)

### ✅ US-PL-3: Login After Registration & Show Welcome Page
**Status:** Complete
- **Endpoint:** `POST /api/v1/auth/login` (public, no JWT)
- **Request Body:**
  ```json
  {
    "username": "sultan1",
    "password": "Player@123"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "token": "<jwt-token-string>",
    "user": {
      "id": 42,
      "username": "sultan1",
      "role": "PLAYER"
    }
  }
  ```
- **Frontend Behavior:**
  - Auto-login after successful registration
  - Stores JWT token in localStorage
  - Displays welcome page: "Welcome Player Sultan1, You have been successfully registered!"
  - Header shows: `Player_sultan1` (username + role format)
  - All subsequent requests include: `Authorization: Bearer <token>`

### ✅ US-PL-4: Select Subscription Package
**Status:** Complete
- **Endpoints:**
  - `POST /api/v1/subscription/basic` (requires JWT, PLAYER role)
  - `POST /api/v1/subscription/standard` (requires JWT, PLAYER role)
  - `POST /api/v1/subscription/premium` (requires JWT, PLAYER role)
- **Response (200 OK):**
  ```json
  {
    "subscriptionId": 1001,
    "playerId": 42,
    "plan": "BASIC",
    "classesCount": 4,
    "price": 200,
    "currency": "SAR",
    "status": "PENDING_PAYMENT"
  }
  ```
- **Package Details:**
  - **Basic:** SR 200, 4 classes
  - **Standard:** SR 350, 8 classes
  - **Premium:** SR 800, 12 classes
- **Frontend Behavior:**
  - Shows three package cards
  - Clicking a package button creates subscription via API
  - Stores `subscriptionId` and plan in sessionStorage
  - Redirects to Select Class page (US-PL-5)

### ✅ US-PL-5: Select Training Class
**Status:** Complete
- **Endpoint:** `GET /api/v1/session/get` (requires JWT, PLAYER role)
- **Response (200 OK):**
  ```json
  [
    {
      "sessionId": 501,
      "startTime": "2025-12-16T13:00:00",
      "endTime": "2025-12-16T15:00:00",
      "trainerName": "Boshra Ibrahim",
      "availableSeats": 5
    }
  ]
  ```
- **Backend Behavior:**
  - Returns only future sessions with `availableSeats > 0`
- **Frontend Behavior:**
  - Displays sessions as cards showing:
    - Time range (e.g., 01:00–03:00 pm)
    - Trainer name
    - Available seats
  - Disables/hides sessions with `availableSeats == 0`
  - Stores `selectedSessionId` in sessionStorage
  - Redirects to Payment page (US-PL-6)

### ✅ US-PL-6: Enter Payment Details & Process Payment
**Status:** Complete
- **Endpoint:** `POST /api/v1/payments/charge` (requires JWT, PLAYER role)
- **Request Body:**
  ```json
  {
    "subscriptionId": 1001,
    "sessionId": 501,
    "amount": 200,
    "currency": "SAR",
    "card": {
      "cardNumber": "5263484101518471",
      "cardHolderName": "Lucie AYAH",
      "expiryMonth": 6,
      "expiryYear": 2024,
      "cvv": "917"
    }
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "paymentId": 9001,
    "status": "SUCCESS",
    "subscriptionId": 1001,
    "sessionId": 501,
    "bookingId": 7001
  }
  ```
- **Backend Behavior:**
  - Validates JWT and user role
  - Validates subscription and session ownership
  - Validates amount matches subscription price
  - Validates session has available seats
  - Processes payment via Moyasar
  - On success:
    - Creates payment record (SUCCESS status)
    - Creates booking record (CONFIRMED status)
    - Decreases `availableSeats` by 1
    - Updates subscription status to ACTIVE
  - On failure:
    - Records FAILED payment
    - Does not create booking or change seats
- **Frontend Behavior:**
  - Shows order summary (package, class, time, amount)
  - Payment form with validation:
    - Card number (13-16 digits)
    - Name on card
    - Expiration (future date)
    - CVV (3-4 digits)
  - On success: Redirects to Confirm Your Seat page
  - On failure: Shows error message

### ✅ US-PL-7: Confirm Reserved Seat
**Status:** Complete
- **Behavior:**
  - Booking is already confirmed in payment step (Option A)
  - No additional backend call needed
- **Frontend Behavior:**
  - Shows confirmation page after successful payment
  - Displays booking summary:
    - Class time
    - Trainer name
    - Package name
    - Booking ID
  - Shows "✓ Confirmed" status
  - Provides links to dashboard and classes

## Backend Changes

### New Files Created:
1. `PlayerRegistrationResponse.java` - Response DTO for registration
2. `PaymentChargeRequest.java` - Request DTO for payment charge
3. `PaymentChargeResponse.java` - Response DTO for payment charge
4. `SubscriptionResponse.java` - Response DTO for subscription creation
5. `PaymentChargeService.java` - Service for processing payment charges

### Updated Files:
1. `PlayerController.java` - Returns `PlayerRegistrationResponse` (201 Created)
2. `PlayerService.java` - Returns user info after registration
3. `AuthController.java` - Returns user info in login response
4. `AuthResponse.java` - Added `UserInfo` nested class
5. `SubscriptionController.java` - New endpoints for creating subscriptions without payment
6. `SubscriptionService.java` - New `createSubscription()` method
7. `PaymentController.java` - New `/charge` endpoint
8. `Subscription.java` - Updated status pattern to include `PENDING_PAYMENT`
9. `ConfigurationSecurity.java` - Added `/api/v1/payments/charge` to PLAYER access

## Frontend Changes

### Updated Files:
1. `register-player.html` - Auto-login after registration
2. `registration-success.html` - Welcome page with `Player_username` format
3. `player-subscription.html` - Creates subscription on package selection
4. `player-select-class.html` - Filters only available sessions
5. `player-payment-flow.html` - Uses new `/charge` endpoint
6. `seat-confirmation.html` - Shows booking confirmation

## Flow Sequence

```
1. home.html (Landing Page)
   ↓ [Click Sign Up]
2. role-selector.html
   ↓ [Select Player]
3. register-player.html
   ↓ [Submit Registration]
4. Auto-login (POST /api/v1/auth/login)
   ↓ [Success]
5. registration-success.html (Welcome Page)
   ↓ [Continue to Subscription]
6. player-subscription.html
   ↓ [Select Package → Creates Subscription]
7. player-select-class.html
   ↓ [Select Class]
8. player-payment-flow.html
   ↓ [Submit Payment → Creates Booking]
9. seat-confirmation.html (Final Confirmation)
```

## Testing Checklist

- [ ] Register new player → Auto-login works
- [ ] Welcome page shows correct username format
- [ ] Select subscription package → Subscription created
- [ ] Select class → Only available sessions shown
- [ ] Payment processing → Booking created
- [ ] Seat confirmation → Booking details displayed
- [ ] JWT token stored and used in all requests
- [ ] Error handling for validation failures
- [ ] Error handling for payment failures

## Notes

- All endpoints require JWT authentication except registration and login
- Subscription status: `PENDING_PAYMENT` → `ACTIVE` after payment
- Booking status: `CONFIRMED` immediately after payment
- Available seats decreased by 1 on successful booking
- SessionStorage used to pass data between flow pages
- localStorage used for authentication tokens

