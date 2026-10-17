package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Recurso;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de recursos protegíveis. Implementação em memória, determinística.
 */
public interface RecursoRepository {

    Recurso salvar(Recurso recurso);

    Optional<Recurso> buscarPorId(Long id);

    Optional<Recurso> buscarPorCaminho(String caminho);

    List<Recurso> listarTodos();

    void excluirPorId(Long id);

    boolean existePorCaminho(String caminho);
}
