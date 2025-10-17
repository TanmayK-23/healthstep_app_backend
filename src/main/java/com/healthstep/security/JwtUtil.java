package com.healthstep.security;

import com.healthstep.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final JwtProperties props;
    public JwtUtil(JwtProperties props) { this.props = props; }

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    private byte[] getSecretBytes() {
        return props.getSecret().getBytes(StandardCharsets.UTF_8);
    }

    private javax.crypto.SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(getSecretBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(props.getIssuer())
                .setAudience(props.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generate(String username, Long uid) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(props.getIssuer())
                .setAudience(props.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("uid", uid)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return parse(token).getSubject();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}