package com.seminario.ms_usuarios.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;

@Service
public class JwtService {
    // SECRET KEY: En producción esto va en variables de entorno. 
    // Debe ser larga y compleja (Hexadecimal).
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Guardamos el rol dentro del token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // El "dueño" del token (email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Dura 24 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
