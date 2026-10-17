package com.kelvinoliveis.portalrbac.domain;

import java.util.Objects;
import java.util.Set;

/**
 * Usuário do portal cliente. A senha é sempre armazenada codificada.
 */
public record Usuario(Long id, String username, String email, String senha, boolean ativo, Set<Papel> roles) {

    public Usuario {
        Objects.requireNonNull(username, "username é obrigatório");
        Objects.requireNonNull(email, "e-mail é obrigatório");
        Objects.requireNonNull(senha, "senha é obrigatória");
        Objects.requireNonNull(roles, "roles é obrigatório");
    }
}
