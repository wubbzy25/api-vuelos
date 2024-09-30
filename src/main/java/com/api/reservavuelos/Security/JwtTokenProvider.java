package com.api.reservavuelos.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {


    public String generateToken(Authentication authentication) {
        // Implementación JWT Token generación
        String username = authentication.getName();
        Date tiempoactual = new Date();
        Date expiracion = new Date(tiempoactual.getTime() + ConstantSecurity.JWT_EXPIRATION_TOKEN);

        String Token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(tiempoactual)
                .setExpiration(expiracion)
                .signWith(SignatureAlgorithm.HS512, ConstantSecurity.JWT_FIRMA)
                .compact();

        return Token;
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantSecurity.JWT_FIRMA)
                .build()
                .parseClaimsJws(token)
                .getBody();

      return claims.getSubject();
    }

    public void IsValidToken(String token) {
           Jwts.parser()
           .setSigningKey(ConstantSecurity.JWT_FIRMA)
           .build()
           .parseClaimsJws(token);
    }
}
