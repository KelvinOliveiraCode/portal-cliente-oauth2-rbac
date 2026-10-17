package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Recurso;
import com.kelvinoliveis.portalrbac.dto.RecursoDto;
import com.kelvinoliveis.portalrbac.dto.RecursoRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de recursos protegíveis (ex: /api/admin/relatorios).
 */
@Service
public class RecursoService {

    private final RecursoRepository repository;

    public RecursoService(RecursoRepository repository) {
        this.repository = repository;
    }

    public List<RecursoDto> listar() {
        return repository.listarTodos().stream()
                .map(this::paraDto)
                .toList();
    }

    public Optional<RecursoDto> buscarPorCaminho(String caminho) {
        return repository.buscarPorCaminho(caminho).map(this::paraDto);
    }

    public RecursoDto buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .map(this::paraDto)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Recurso não encontrado: " + id));
    }

    public RecursoDto criar(RecursoRequest request) {
        if (repository.existePorCaminho(request.caminho())) {
            throw new RegraNegocioException("Recurso já cadastrado: " + request.caminho());
        }
        Recurso salvo = repository.salvar(new Recurso(null, request.nome(), request.caminho(), request.descricao()));
        return paraDto(salvo);
    }

    public void excluir(Long id) {
        if (!repository.buscarPorId(id).isPresent()) {
            throw new EntidadeNaoEncontradaException("Recurso não encontrado: " + id);
        }
        repository.excluirPorId(id);
    }

    private RecursoDto paraDto(Recurso recurso) {
        return new RecursoDto(recurso.id(), recurso.nome(), recurso.caminho(), recurso.descricao());
    }
}
