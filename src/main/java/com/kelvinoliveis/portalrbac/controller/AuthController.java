package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.AuthResponse;
import com.kelvinoliveis.portalrbac.dto.LoginRequest;
import com.kelvinoliveis.portalrbac.dto.MensagemDto;
import com.kelvinoliveis.portalrbac.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoints de autenticação OAuth2: login (emissão de JWT) e logout.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<MensagemDto> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MensagemDto("Sessão encerrada"));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities()
        ));
    }
}
