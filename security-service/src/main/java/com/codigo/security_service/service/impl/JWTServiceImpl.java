package com.codigo.security_service.service.impl;

import com.codigo.security_service.service.JWTService;
import com.codigo.security_service.service.PersonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl  implements JWTService {
    private final PersonService personService;
    @Value("${key.token}")
    private String keytoken;
    @Override
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return null;
    }

    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode(keytoken.trim());
        return Keys.hmacShaKeyFor(key);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token.trim()).getBody();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResult){
        final Claims claims = extractAllClaims(token);
        return claimsResult.apply(claims);
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    public boolean validate(String token) {
        try {
            System.out.println(token.toString());
            final String userEmail;
//            userEmail = extractUsername("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcnVlYmFAZXhhbXBsZS5jb20iLCJpYXQiOjE3MTcyMjQwNjMsImV4cCI6MTcxNzIyNDE4M30.nCe5j1cZ7NqrwcU3g1442atUq2ndmlUUA9PpYhw3Vy0");
            userEmail = extractUsername(token);
            if(Objects.nonNull(userEmail)){
                UserDetails userDetails = personService.userDetailService().loadUserByUsername(userEmail);
                return validateToken(token,userDetails);
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
