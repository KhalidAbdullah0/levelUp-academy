package com.levelup.levelup_academy.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationInMs;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return (Integer) userId;
        } else if (userId instanceof Number) {
            return ((Number) userId).intValue();
        }
        return null;
    }

    public String extractFullName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("fullName", String.class);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        Object role = claims.get("role");
        if (role instanceof String) {
            return (String) role;
        } else if (role instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<String> roles = (java.util.List<String>) role;
            return roles.isEmpty() ? null : roles.get(0);
        }
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);
        
        // Extract User entity to get additional info
        com.levelup.levelup_academy.Model.User user = null;
        if (userDetails instanceof com.levelup.levelup_academy.Model.User) {
            user = (com.levelup.levelup_academy.Model.User) userDetails;
        }

        var builder = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .findFirst()
                        .orElse(""))
                .setIssuedAt(now)
                .setExpiration(expiryDate);
        
        // Add userId and fullName if available
        if (user != null) {
            builder.claim("userId", user.getId());
            builder.claim("fullName", user.getFirstName() + " " + user.getLastName());
        }
        
        return builder.signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public long getExpirationInMs() {
        return expirationInMs;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
