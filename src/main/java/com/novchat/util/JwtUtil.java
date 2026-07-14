package com.novchat.util;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Secret key - production mein isko application.properties se lena chahiye
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "novchat-super-secret-key-change-this-in-production-12345678".getBytes()
    );

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    // Token generate karna
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // Token se username nikalna
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token se expiry date nikalna
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Token expire hua ya nahi check karna
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token validate karna
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}