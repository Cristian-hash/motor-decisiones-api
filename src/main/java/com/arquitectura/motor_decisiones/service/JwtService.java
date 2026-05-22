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
import java.util.function.Function;

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
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    // 2. Validar si el token pertenece a este usuario y no ha expirado
    public boolean isTokenValid(String token,Usuario usuario){
        final String username = extractUsername(token);
        return (username.equals((usuario.getEmail()))&& !isTokenExpired(token));
    }
    // 3-Revisar si la fecha actual es mayor a la fecha de expiración
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    // Método maestro para decodificar cualquier dato del Payload
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}