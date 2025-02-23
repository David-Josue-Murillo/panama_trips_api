package com.app.panama_trips.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class JwtUtil {
    @Value("${security.jwt.key.secret}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String useGenerator;

    public String generateToken(Authentication authentication) {
        // Algorithm
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        String username = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.useGenerator) // Emisor
                .withSubject(username) // Usuario
                .withClaim("authorities", authorities) // Permisos
                .withIssuedAt(new Date()) // Fecha de emisión{
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // 30 minutes
                .withJWTId(UUID.randomUUID().toString()) // Identificador único
                .withNotBefore(new Date(System.currentTimeMillis())) // Entra en vigencia inmediatamente
                .sign(algorithm); // Firma
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.useGenerator)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Invalid token, not authorized");
        }
    }

    public String getUsernameFromToken(DecodedJWT decodedJWT) {
        return  decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }
}