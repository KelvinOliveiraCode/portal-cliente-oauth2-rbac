package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.dto.PapelDto;
import com.kelvinoliveis.portalrbac.dto.PapelRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.PapelRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * PapelService: catálogo de papéis do RBAC.
 */
class PapelServiceTest {

    private PapelRepositoryImpl repository;
    private PapelService service;

    @BeforeEach
    void setUp() {
        repository = new PapelRepositoryImpl();
        service = new PapelService(repository);
    }

    @Test
    void should_criarPapel_quandoNomeValido() {
        PapelDto dto = service.criar(new PapelRequest("ADMIN", "Administrador"));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.nome()).isEqualTo("ADMIN");
        assertThat(dto.descricao()).isEqualTo("Administrador");
    }

    @Test
    void should_rejeitarPapelDuplicado_quandoNomeJaExiste() {
        service.criar(new PapelRequest("ADMIN", "Administrador"));

        assertThatThrownBy(() -> service.criar(new PapelRequest("ADMIN", "Outro")))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Papel já cadastrado");
    }

    @Test
    void should_listarPapeis_retornaTodosCadastrados() {
        service.criar(new PapelRequest("ADMIN", "Administrador"));
        service.criar(new PapelRequest("CLIENTE", "Cliente"));

        assertThat(service.listar())
                .extracting(PapelDto::nome)
                .containsExactlyInAnyOrder("ADMIN", "CLIENTE");
    }

    @Test
    void should_lancarEntidadeNaoEncontrada_quandoBuscarPapelInexistente() {
        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void should_retornarVazio_quandoBuscarPapelPorNomeNaoEncontrado() {
        Optional<PapelDto> encontrado = service.buscarPorNome("NAO_EXISTE");

        assertThat(encontrado).isEmpty();
    }

    @Test
    void should_buscarPapelPorNome_ignorandoMaiusculas() {
        service.criar(new PapelRequest("ADMIN", "Administrador"));

        Optional<PapelDto> encontrado = service.buscarPorNome("admin");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().nome()).isEqualTo("ADMIN");
    }
}
