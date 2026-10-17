package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.domain.Usuario;
import com.kelvinoliveis.portalrbac.dto.UsuarioDto;
import com.kelvinoliveis.portalrbac.dto.UsuarioRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.PapelRepositoryImpl;
import com.kelvinoliveis.portalrbac.repository.UsuarioRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * UsuarioService: cadastro, consulta, ativação e gestão de papéis (RBAC).
 */
class UsuarioServiceTest {

    private UsuarioRepositoryImpl usuarioRepository;
    private PapelRepositoryImpl papelRepository;
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();
        papelRepository = new PapelRepositoryImpl();
        service = new UsuarioService(usuarioRepository, papelRepository,
                new BCryptPasswordEncoder());
        papelRepository.salvar(new Papel(1L, "ADMIN", "Administrador"));
        papelRepository.salvar(new Papel(2L, "CLIENTE", "Cliente"));
    }

    @Test
    void should_cadastrarUsuario_quandoDadosValidos() {
        UsuarioDto dto = service.cadastrar(new UsuarioRequest(
                "ana", "ana@portal.local", "senha123", List.of(1L)));

        assertThat(dto.id()).isNotNull();
        assertThat(dto.username()).isEqualTo("ana");
        assertThat(dto.ativo()).isTrue();
        assertThat(dto.roles()).containsExactly("ADMIN");
        // senha não exposta no DTO e armazenada codificada no repositório
        Usuario persistido = usuarioRepository.buscarPorId(dto.id()).orElseThrow();
        assertThat(new BCryptPasswordEncoder().matches("senha123", persistido.senha())).isTrue();
    }

    @Test
    void should_rejeitarUsuarioDuplicado_quandoUsernameJaExiste() {
        service.cadastrar(new UsuarioRequest("ana", "ana@portal.local", "senha123", List.of()));

        assertThatThrownBy(() -> service.cadastrar(new UsuarioRequest("ana", "outra@portal.local", "senha123", List.of())))
                .isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void should_atribuirPapel_quandoUsuarioEPapelExistem() {
        UsuarioDto dto = service.cadastrar(new UsuarioRequest("ana", "ana@portal.local", "senha123", List.of()));
        service.atribuirPapel(dto.id(), 2L);

        assertThat(service.buscarPorId(dto.id()).roles())
                .containsExactly("CLIENTE");
    }

    @Test
    void should_removerPapel_quandoUsuarioTemPapel() {
        UsuarioDto dto = service.cadastrar(new UsuarioRequest("ana", "ana@portal.local", "senha123", List.of(1L, 2L)));
        service.removerPapel(dto.id(), 1L);

        assertThat(service.buscarPorId(dto.id()).roles())
                .containsExactly("CLIENTE");
    }

    @Test
    void should_desativarUsuario_quandoIdExistente() {
        UsuarioDto dto = service.cadastrar(new UsuarioRequest("ana", "ana@portal.local", "senha123", List.of()));
        service.desativar(dto.id());

        assertThat(service.buscarPorId(dto.id()).ativo()).isFalse();
    }

    @Test
    void should_lancarEntidadeNaoEncontrada_quandoAtribuirPapelUsuarioInexistente() {
        assertThatThrownBy(() -> service.atribuirPapel(99L, 1L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void should_lancarEntidadeNaoEncontrada_quandoPapelInexistenteNaAtribuicao() {
        UsuarioDto dto = service.cadastrar(new UsuarioRequest("ana", "ana@portal.local", "senha123", List.of()));

        assertThatThrownBy(() -> service.atribuirPapel(dto.id(), 55L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
