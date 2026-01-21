/**
 * Authentication Utilities
 * Reusable functions for authentication state management
 */

const AuthUtils = {
    /**
     * Get authentication token from localStorage
     */
    getToken() {
        return localStorage.getItem('authToken');
    },

    /**
     * Check if user is authenticated
     */
    isAuthenticated() {
        return !!this.getToken();
    },

    /**
     * Get current user info from API
     */
    async getCurrentUser() {
        const token = this.getToken();
        if (!token) {
            return null;
        }

        try {
            const apiEndpoint = window.apiUrl ? window.apiUrl('/api/v1/auth/me') : '/api/v1/auth/me';
            const res = await fetch(apiEndpoint, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (res.ok) {
                return await res.json();
            } else if (res.status === 401) {
                // Token invalid, clear auth state
                this.clearAuth();
                return null;
            }
            return null;
        } catch (error) {
            console.error('Error fetching user info:', error);
            return null;
        }
    },

    /**
     * Clear authentication state
     */
    clearAuth() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRole');
        localStorage.removeItem('tokenType');
        localStorage.removeItem('userName');
        localStorage.removeItem('userFullName');
        localStorage.removeItem('userId');
        localStorage.removeItem('userEmail');
    },

    /**
     * Logout user - redirects to home page
     */
    logout() {
        this.clearAuth();
        // Redirect to home page (guest landing page)
        window.location.href = 'home.html';
    },

    /**
     * Make authenticated API request
     */
    async apiRequest(url, options = {}) {
        const token = this.getToken();
        if (!token) {
            throw new Error('Not authenticated');
        }

        const defaultHeaders = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        const mergedOptions = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...(options.headers || {})
            }
        };

        const apiEndpoint = window.apiUrl ? window.apiUrl(url) : url;
        const res = await fetch(apiEndpoint, mergedOptions);

        if (res.status === 401) {
            this.clearAuth();
            window.location.href = 'login.html';
            throw new Error('Unauthorized');
        }

        return res;
    },

    /**
     * Require authentication - redirect to login if not authenticated
     */
    requireAuth() {
        if (!this.isAuthenticated()) {
            window.location.href = 'login.html';
            return false;
        }
        return true;
    },

    /**
     * Format role name for display
     */
    formatRoleName(role) {
        const roleMap = {
            'PLAYER': 'Player',
            'PRO': 'Pro Player',
            'TRAINER': 'Trainer',
            'PARENTS': 'Parent',
            'MODERATOR': 'Moderator',
            'ADMIN': 'Admin'
        };
        return roleMap[role] || role;
    }
};

