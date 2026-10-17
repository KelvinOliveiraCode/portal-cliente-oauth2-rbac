package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Recurso;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementação em memória de {@link RecursoRepository}.
 */
@Repository
public class RecursoRepositoryImpl implements RecursoRepository {

    private final ConcurrentHashMap<Long, Recurso> recursos = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Recurso salvar(Recurso recurso) {
        Long id = recurso.id() != null ? recurso.id() : sequencia.incrementAndGet();
        Recurso salvo = new Recurso(id, recurso.nome(), recurso.caminho(), recurso.descricao());
        recursos.put(id, salvo);
        return salvo;
    }

    @Override
    public Optional<Recurso> buscarPorId(Long id) {
        return Optional.ofNullable(recursos.get(id));
    }

    @Override
    public Optional<Recurso> buscarPorCaminho(String caminho) {
        return recursos.values().stream()
                .filter(r -> r.caminho().equalsIgnoreCase(caminho))
                .findFirst();
    }

    @Override
    public List<Recurso> listarTodos() {
        return List.copyOf(recursos.values());
    }

    @Override
    public void excluirPorId(Long id) {
        recursos.remove(id);
    }

    @Override
    public boolean existePorCaminho(String caminho) {
        return buscarPorCaminho(caminho).isPresent();
    }
}
