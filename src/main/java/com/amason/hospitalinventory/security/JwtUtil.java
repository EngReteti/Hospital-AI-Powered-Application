package com.amason.hospitalinventory.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

// @Component tells Spring: "create and manage one of these, make it 
// available anywhere else in the app that needs it" - same idea as 
// @Service, just used for general-purpose utility classes like this one
@Component
public class JwtUtil {

    // This is the SECRET KEY used to sign every token - anyone with 
    // this key could forge a valid-looking token, so in a real deployed 
    // app this would come from an environment variable, NOT be hardcoded 
    // like this. We'll fix that before deployment (Phase 17) - for now, 
    // during local development, this is fine
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "ThisIsATemporaryDevelopmentSecretKeyChangeThisLater123456".getBytes()
    );

    // How long a token stays valid before the user must log in again - 
    // here, 24 hours, written out in milliseconds (the unit Java uses)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * Creates a new signed token containing the user's email and role.
     * This token is what the frontend will store and send back on 
     * every future request to prove who's making it.
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
            .subject(email)                 // WHO this token belongs to
            .claim("role", role)            // extra info we want packed in - their role
            .issuedAt(new Date())           // WHEN it was created
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(secretKey)            // seals it - tampering breaks this seal
            .compact();                     // turns it into the final text string
    }

    /**
     * Reads a token and pulls out the email it belongs to - 
     * but ONLY if the token's seal is genuine and it hasn't expired.
     * If someone tampered with it, this throws an error automatically.
     */
    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    /**
     * Same idea, but pulls out the role instead of the email.
     */
    public String extractRole(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
    }
}
