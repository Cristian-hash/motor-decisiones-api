package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
@Service
public class JwtService {

    @Value ("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String generateToken(Usuario usuario){
        Map<String,Object> extraClaims= new HashMap<>();
        extraClaims.put("id",usuario.getId());

        return Jwts.builder()
                .setClaims(extraClaims) // Agrega los datos extra
                .setSubject(usuario.getEmail()) // El "sujeto" principal del token (el email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de caducidad
                .signWith(getSignInKey(), SignatureAlgorithm.HS512) // La firma criptográfica
                .compact(); // Ensambla todo en un String (Header.Payload.Signature)
    }

    private Key getSignInKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Extraer el email (Subject) del token
    public String extracUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

}