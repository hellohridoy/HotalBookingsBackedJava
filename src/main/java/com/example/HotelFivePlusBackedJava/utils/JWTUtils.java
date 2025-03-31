package com.example.HotelFivePlusBackedJava.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
    private final SecretKey secretKey;

    // Generate a secure secret key
    public JWTUtils() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Generate a JWT token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername()) // ✅ FIXED: setSubject instead of subject
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ FIXED: Corrected signWith
            .compact();
    }

    // Extract claims from token
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey) // ✅ FIXED: setSigningKey instead of verifyWith
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claimsResolver.apply(claims);
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    // Validate token
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
