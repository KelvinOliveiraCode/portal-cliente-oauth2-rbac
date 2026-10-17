package com.kelvinoliveis.portalrbac.dto;

import java.util.List;

/**
 * Resposta de autenticação com JWT e papeis do usuário.
 */
public record AuthResponse(String token,
                           String username,
                           List<String> roles) {
}
