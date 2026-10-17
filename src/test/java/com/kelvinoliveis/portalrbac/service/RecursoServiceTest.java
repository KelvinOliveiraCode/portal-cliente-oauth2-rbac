package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Recurso;
import com.kelvinoliveis.portalrbac.dto.RecursoDto;
import com.kelvinoliveis.portalrbac.dto.RecursoRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.RecursoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * RecursoService: recursos protegíveis do portal cliente.
 */
class RecursoServiceTest {

    private RecursoRepositoryImpl repository;
    private RecursoService service;

    @BeforeEach
    void setUp() {
        repository = new RecursoRepositoryImpl();
        service = new RecursoService(repository);
    }

    @Test
    void should_criarRecurso_quandoDadosValidos() {
        RecursoDto dto = service.criar(new RecursoRequest(
                "Relatórios administrativos", "/api/admin/relatorios", "Apenas ADMIN"));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.caminho()).isEqualTo("/api/admin/relatorios");
    }

    @Test
    void should_rejeitarRecursoDuplicado_quandoCaminhoJaExiste() {
        service.criar(new RecursoRequest("X", "/api/y", "z"));

        assertThatThrownBy(() -> service.criar(new RecursoRequest("W", "/api/y", "z")))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void should_listarRecursos_retornaTodosCadastrados() {
        service.criar(new RecursoRequest("A", "/api/a", "a"));
        service.criar(new RecursoRequest("B", "/api/b", "b"));

        assertThat(service.listar())
                .extracting(RecursoDto::caminho)
                .containsExactlyInAnyOrder("/api/a", "/api/b");
    }

    @Test
    void should_lancarEntidadeNaoEncontrada_quandoRecursoInexistente() {
        assertThatThrownBy(() -> service.buscarPorId(7L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void should_retornarVazio_quandoBuscarRecursoPorCaminhoNaoEncontrado() {
        Optional<RecursoDto> encontrado = service.buscarPorCaminho("/api/nao-existe");

        assertThat(encontrado).isEmpty();
    }
}
