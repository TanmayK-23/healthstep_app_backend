package com.healthstep.security;

import com.healthstep.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final JwtProperties props;
    public JwtUtil(JwtProperties props) { this.props = props; }

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(props.getIssuer())
                .setAudience(props.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, props.getSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(props.getSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(props.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generate(String username, Long uid) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(props.getIssuer())
                .setAudience(props.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("uid", uid)
                .signWith(SignatureAlgorithm.HS256, props.getSecret())
                .compact();
    }

    public Claims parse(String token) {
      return Jwts.parser()
          .setSigningKey(props.getSecret())
          .parseClaimsJws(token)
          .getBody();
    }
}