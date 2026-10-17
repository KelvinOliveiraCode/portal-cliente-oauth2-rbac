package com.kelvinoliveis.portalrbac.domain;

import java.util.Objects;

/**
 * Papel de acesso do sistema (ex: ADMIN, CLIENTE).
 */
public record Papel(Long id, String nome, String descricao) {

    public Papel {
        Objects.requireNonNull(nome, "nome do papel é obrigatório");
    }
}
