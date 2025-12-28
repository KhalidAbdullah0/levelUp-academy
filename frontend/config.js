// API Configuration
// Change this based on your environment

// Only declare CONFIG if it doesn't already exist (prevents duplicate declaration errors)
if (typeof window.CONFIG === 'undefined') {
  window.CONFIG = {
    // Development (default)
    API_BASE_URL: '', // Empty string uses relative URLs (same domain)
    
    // Production example
    // API_BASE_URL: 'https://your-backend.com',
    
    // Local backend on different port
    // API_BASE_URL: 'http://localhost:8080',
    
    // Redirect URLs
    DASHBOARD_URL: 'dashboard.html',
    LOGIN_URL: 'login.html',
    SIGNUP_URL: 'signup.html',
  };

  // Helper function to build API URLs
  window.apiUrl = function(endpoint) {
    return window.CONFIG.API_BASE_URL + endpoint;
  };
}

// Create local reference for backward compatibility
var CONFIG = window.CONFIG;
var apiUrl = window.apiUrl;
