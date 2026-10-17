package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de usuários. Implementação em memória, determinística.
 */
public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorUsername(String username);

    List<Usuario> listarTodos();

    void excluirPorId(Long id);

    boolean existsByUsername(String username);
}
