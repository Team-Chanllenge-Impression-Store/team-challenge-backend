package com.online_store.security.auth.service;

import com.online_store.utils.constant.Path;
import com.online_store.web.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${com.online_store.security.auth.jwt.secret}")
    private String accessTokenSecret;
    @Value("${com.online_store.security.auth.jwt.accessToken}")
    private int accessTokenExpirationMinutes;
    @Value("${com.online_store.security.auth.jwt.refreshToken}")
    private int refreshTokenExpirationMinutes;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, "access", accessTokenExpirationMinutes);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, "refresh", refreshTokenExpirationMinutes);
    }

    private String generateToken(UserDetails userDetails, String type, int expirationMinutes) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User user) {
            claims.put("type", type);
        }
        return BEARER_PREFIX + buildToken(claims, userDetails, expirationMinutes);
    }

    /**
     * Generates a JWT token based on the given claims, user details and expiration
     * time in minutes.
     *
     * @param claims            the claims to be stored in the JWT token
     * @param userDetails       the user details to be used for generating the token
     * @param expirationMinutes the time in minutes after which the token will expire
     * @return a string representing the generated JWT token
     */
    private String buildToken(Map<String, Object> claims, UserDetails userDetails, int expirationMinutes) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setNotBefore(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
                .setId(UUID.randomUUID().toString())
                .signWith(key(), SignatureAlgorithm.HS256)
                .setHeader(Map.of("type", "JWT"))
                .compact();
    }

    public boolean validateJwtToken(String authToken, UserDetails userDetails) {
        final String email = extractEmailFromToken(authToken);
        return email.equals(userDetails.getUsername());
    }

    public String extractEmailFromToken(String authToken) {
        return extractClaim(authToken, Claims::getSubject);
    }

    <T> T extractClaim(String authToken, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(authToken);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String authToken) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .path(Path.API_PATHNAME)
                .build();
        return cookie;
    }

    public boolean isRefreshToken(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class)).equals("refresh");
    }
}
