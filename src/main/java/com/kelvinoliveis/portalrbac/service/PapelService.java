package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.dto.PapelDto;
import com.kelvinoliveis.portalrbac.dto.PapelRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.PapelRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de papéis (RBAC). Regras: nome único, não exclui papel atribuído a usuários
 * nesta versão MVP (exclusão apenas remove o catálogo).
 */
@Service
public class PapelService {

    private final PapelRepository repository;

    public PapelService(PapelRepository repository) {
        this.repository = repository;
    }

    public List<PapelDto> listar() {
        return repository.listarTodos().stream()
                .map(this::paraDto)
                .toList();
    }

    public Optional<PapelDto> buscarPorNome(String nome) {
        return repository.buscarPorNome(nome).map(this::paraDto);
    }

    public PapelDto buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .map(this::paraDto)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Papel não encontrado: " + id));
    }

    public PapelDto criar(PapelRequest request) {
        if (repository.existePorNome(request.nome())) {
            throw new RegraNegocioException("Papel já cadastrado: " + request.nome());
        }
        Papel salvo = repository.salvar(new Papel(null, request.nome(), request.descricao()));
        return paraDto(salvo);
    }

    public void excluir(Long id) {
        if (!repository.buscarPorId(id).isPresent()) {
            throw new EntidadeNaoEncontradaException("Papel não encontrado: " + id);
        }
        repository.excluirPorId(id);
    }

    private PapelDto paraDto(Papel papel) {
        return new PapelDto(papel.id(), papel.nome(), papel.descricao());
    }
}
