package com.kelvinoliveis.portalrbac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO de entrada para cadastro/edição de usuário.
 */
public record UsuarioRequest(@NotBlank @Size(max = 100) String username,
                             @NotBlank @Email @Size(max = 200) String email,
                             @NotBlank @Size(min = 6, max = 100) String senha,
                             List<Long> roleIds) {
}
