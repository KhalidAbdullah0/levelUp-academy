// API Configuration
// Change this based on your environment

const CONFIG = {
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
function apiUrl(endpoint) {
  return CONFIG.API_BASE_URL + endpoint;
}

// Export for use in other files
if (typeof window !== 'undefined') {
  window.CONFIG = CONFIG;
  window.apiUrl = apiUrl;
}

