package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final long JWT_TOKEN_VALIDITY = 7 * 60 * 60;
    private static final String secret = "hacker";

    public String generateToken (String username) {
        return doGenerateToken(username);
    }

    private String doGenerateToken (String username) {
        return Jwts.builder().setClaims(new HashMap<>()).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getUsernameFromToken (String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}
