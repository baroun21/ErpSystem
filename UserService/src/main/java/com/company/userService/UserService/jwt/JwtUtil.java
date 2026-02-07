package com.company.userService.UserService.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // IMPORTANT: In production move this to application properties / env var
    // Must be long enough for HS512 (at least 64 chars is good)
    private static final String SECRET =
            "THIS_IS_A_VERY_LONG_SECRET_KEY_FOR_HS512_AT_LEAST_64_CHARS_1234567890_ABCDEF";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_MS = 10L * 60 * 100L; // 10 minutes

    public String generateToken(String username, Long companyId) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (companyId == null) {
            throw new IllegalArgumentException("companyId is required");
        }

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(username)
                .claim("companyId", companyId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractCompanyId(String token) {
        Claims claims = getClaims(token);

        Object value = claims.get("companyId");
        if (value == null) return null;

        // safer parsing (sometimes comes as Integer depending on serializer)
        if (value instanceof Long l) return l;
        if (value instanceof Integer i) return i.longValue();
        if (value instanceof String s) return Long.parseLong(s);

        throw new IllegalArgumentException("Invalid companyId type in token: " + value.getClass());
    }

    public boolean validateToken(String token, String expectedUsername) {
        String username = extractUsername(token);
        return username != null
                && username.equals(expectedUsername)
                && !isTokenExpired(token);
    }

    public boolean validateToken(String token, String expectedUsername, Long expectedCompanyId) {
        if (!validateToken(token, expectedUsername)) return false;
        Long tokenCompanyId = extractCompanyId(token);
        return tokenCompanyId != null && tokenCompanyId.equals(expectedCompanyId);
    }

    private boolean isTokenExpired(String token) {
        Date exp = getClaims(token).getExpiration();
        return exp.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}