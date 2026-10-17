package com.kelvinoliveis.portalrbac.repository;

import com.kelvinoliveis.portalrbac.domain.Papel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementação em memória de {@link PapelRepository}. Sem banco de dados externo;
 * o estado é volátil e determinístico por instância da aplicação.
 */
@Repository
public class PapelRepositoryImpl implements PapelRepository {

    private final ConcurrentHashMap<Long, Papel> papeis = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    @Override
    public Papel salvar(Papel papel) {
        Long id = papel.id() != null ? papel.id() : sequencia.incrementAndGet();
        Papel salvo = new Papel(id, papel.nome(), papel.descricao());
        papeis.put(id, salvo);
        return salvo;
    }

    @Override
    public Optional<Papel> buscarPorId(Long id) {
        return Optional.ofNullable(papeis.get(id));
    }

    @Override
    public Optional<Papel> buscarPorNome(String nome) {
        return papeis.values().stream()
                .filter(p -> p.nome().equalsIgnoreCase(nome))
                .findFirst();
    }

    @Override
    public List<Papel> listarTodos() {
        return List.copyOf(papeis.values());
    }

    @Override
    public void excluirPorId(Long id) {
        papeis.remove(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return buscarPorNome(nome).isPresent();
    }
}
