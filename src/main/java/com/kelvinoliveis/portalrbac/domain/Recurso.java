package com.kelvinoliveis.portalrbac.domain;

import java.util.Objects;

/**
 * Recurso protegível do sistema (ex: /api/admin/relatorios).
 */
public record Recurso(Long id, String nome, String caminho, String descricao) {

    public Recurso {
        Objects.requireNonNull(nome, "nome do recurso é obrigatório");
        Objects.requireNonNull(caminho, "caminho do recurso é obrigatório");
    }
}
