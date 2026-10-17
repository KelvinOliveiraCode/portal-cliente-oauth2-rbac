package com.kelvinoliveis.portalrbac.dto;

import java.util.List;

/**
 * DTO de leitura de usuário. A senha nunca é exposta.
 */
public record UsuarioDto(Long id,
                         String username,
                         String email,
                         boolean ativo,
                         List<String> roles) {
}
