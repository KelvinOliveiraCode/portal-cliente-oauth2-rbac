package com.kelvinoliveis.portalrbac.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Emissão e validação de tokens JWT (OAuth2 bearer). Chave simétrica HMAC-SHA;
 * emissor e expiração fixos para tornar o comportamento determinístico em testes.
 */
@Service
public class TokenService {

    private static final String SEGREDO = "portal-cliente-oauth2-rbac-chave-secreta-minima-com-256-bits-!!!";
    private static final long EXPIRACAO_MS = 8L * 60 * 60 * 1000;
    private final SecretKey chave = Keys.hmacShaKeyFor(SEGREDO.getBytes(StandardCharsets.UTF_8));

    public String gerarToken(String username,
                             Collection<? extends org.springframework.security.core.GrantedAuthority> autoridades) {
        List<String> roles = autoridades.stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRACAO_MS))
                .signWith(chave)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            extrairClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extrairUsuario(String token) {
        return extrairClaims(token).getSubject();
    }

    public List<String> extrairRoles(String token) {
        return extrairClaims(token).get("roles", List.class);
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
