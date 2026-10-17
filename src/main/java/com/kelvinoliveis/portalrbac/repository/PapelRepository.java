package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Papel;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de papéis (RBAC). Implementação em memória, determinística.
 */
public interface PapelRepository {

    Papel salvar(Papel papel);

    Optional<Papel> buscarPorId(Long id);

    Optional<Papel> buscarPorNome(String nome);

    List<Papel> listarTodos();

    void excluirPorId(Long id);

    boolean existePorNome(String nome);
}
