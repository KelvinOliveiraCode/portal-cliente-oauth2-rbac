package com.kelvinoliveis.portalrbac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TokenService: emissão e validação de JWT (determinístico, sem clock externo).
 */
class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
    }

    @Test
    void should_gerarToken_naoVazio_quandoAutoridadesInformadas() {
        String token = tokenService.gerarToken("ana", List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_CLIENTE")
        ));

        assertThat(token).isNotBlank();
    }

    @Test
    void should_extrairUsuarioERoles_quandoTokenValido() {
        String token = tokenService.gerarToken("ana", List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        ));

        assertThat(tokenService.extrairUsuario(token)).isEqualTo("ana");
        assertThat(tokenService.extrairRoles(token)).containsExactly("ADMIN");
    }

    @Test
    void should_retornarFalso_quandoTokenInvalido() {
        assertThat(tokenService.validarToken("qualquer.string.invalida")).isFalse();
        assertThat(tokenService.validarToken("")).isFalse();
    }
}
