package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.domain.Usuario;
import com.kelvinoliveis.portalrbac.dto.AuthResponse;
import com.kelvinoliveis.portalrbac.dto.LoginRequest;
import com.kelvinoliveis.portalrbac.error.CredenciaisInvalidasException;
import com.kelvinoliveis.portalrbac.error.UsuarioInativoException;
import com.kelvinoliveis.portalrbac.repository.UsuarioRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * AuthService: autenticação OAuth2 com emissão de JWT.
 */
class AuthServiceTest {

    private UsuarioRepositoryImpl usuarioRepository;
    private TokenService tokenService;
    private AuthService service;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();
        tokenService = new TokenService();
        service = new AuthService(usuarioRepository, tokenService, new BCryptPasswordEncoder());
        usuarioRepository.salvar(new Usuario(
                1L, "ana", "ana@portal.local", new BCryptPasswordEncoder().encode("senha123"),
                true, Set.of(new Papel(1L, "ADMIN", "Administrador"))));
        usuarioRepository.salvar(new Usuario(
                2L, "inativo", "inativo@portal.local", new BCryptPasswordEncoder().encode("senha123"),
                false, Set.of(new Papel(2L, "CLIENTE", "Cliente"))));
    }

    @Test
    void should_autenticar_quandoCredenciaisValidas() {
        AuthResponse response = service.autenticar(new LoginRequest("ana", "senha123"));

        assertThat(response.token()).isNotBlank();
        assertThat(response.username()).isEqualTo("ana");
        assertThat(response.roles()).containsExactly("ADMIN");
    }

    @Test
    void should_lancarCredenciaisInvalidas_quandoUsuarioNaoExiste() {
        assertThatThrownBy(() -> service.autenticar(new LoginRequest("nao-existe", "senha123")))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void should_lancarCredenciaisInvalidas_quandoSenhaInvalida() {
        assertThatThrownBy(() -> service.autenticar(new LoginRequest("ana", "senhaErrada")))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void should_lancarUsuarioInativo_quandoUsuarioDesativado() {
        assertThatThrownBy(() -> service.autenticar(new LoginRequest("inativo", "senha123")))
                .isInstanceOf(UsuarioInativoException.class);
    }
}
