package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementação em memória de {@link UsuarioRepository}.
 */
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final ConcurrentHashMap<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Usuario salvar(Usuario usuario) {
        Long id = usuario.id() != null ? usuario.id() : sequencia.incrementAndGet();
        Usuario salvo = new Usuario(id, usuario.username(), usuario.email(), usuario.senha(), usuario.ativo(), usuario.roles());
        usuarios.put(id, salvo);
        return salvo;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarios.values().stream()
                .filter(u -> u.username().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios.values());
    }

    @Override
    public void excluirPorId(Long id) {
        usuarios.remove(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return buscarPorUsername(username).isPresent();
    }
}
