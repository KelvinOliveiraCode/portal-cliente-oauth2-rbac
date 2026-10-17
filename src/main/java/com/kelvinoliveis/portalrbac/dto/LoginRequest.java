package com.kelvinoliveis.portalrbac.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de login OAuth2 (username + senha).
 */
public record LoginRequest(@NotBlank String username,
                           @NotBlank String senha) {
}
