package com.example.galileo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Proporciona operaciones para generar y validar tokens JWT.
 * <p>
 * Usa una clave HMAC generada a partir de la propiedad {@code security.jwt.secret}
 * y un tiempo de expiración de token configurable.
 */
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms:3600000}")
    private long expirationMillis;

    private Key signingKey;

    /**
     * Inicializa la clave de firma del token después de que se inyecten las propiedades.
     */
    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Genera un token JWT para el usuario autenticado.
     *
     * @param authentication la autenticación del usuario que contiene el nombre y los roles
     * @return el token JWT firmado como cadena
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expirationMillis);
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida que el token JWT sea correcto y esté firmado con la clave esperada.
     *
     * @param token el token JWT a validar
     * @return {@code true} si el token es válido; {@code false} en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception _) {
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     *
     * @param token el token JWT del que obtener el nombre de usuario
     * @return el nombre de usuario contenido en el token
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Extrae el identificador del token JWT.
     *
     * @param token el token JWT del que obtener el identificador
     * @return el identificador único del token
     */
    public String getTokenId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getId();
    }

    /**
     * Obtiene la fecha y hora de expiración del token JWT.
     *
     * @param token el token JWT del que obtener la expiración
     * @return la fecha de expiración como {@link Instant}
     */
    public Instant getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().toInstant();
    }

    /**
     * Devuelve el tiempo en milisegundos usado para calcular la expiración de los tokens.
     *
     * @return el tiempo de expiración configurado en milisegundos
     */
    public long getExpirationMillis() {
        return expirationMillis;
    }
}
