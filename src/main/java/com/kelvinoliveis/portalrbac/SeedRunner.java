package com.kelvinoliveis.portalrbac;

import com.kelvinoliveis.portalrbac.dto.PapelDto;
import com.kelvinoliveis.portalrbac.dto.PapelRequest;
import com.kelvinoliveis.portalrbac.dto.RecursoRequest;
import com.kelvinoliveis.portalrbac.dto.UsuarioRequest;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.service.PapelService;
import com.kelvinoliveis.portalrbac.service.RecursoService;
import com.kelvinoliveis.portalrbac.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Carrega dados iniciais (seed) determinísticos: papéis ADMIN/CLIENTE, recursos
 * protegíveis e usuários administrador e cliente de demonstração.
 */
@Configuration
public class SeedRunner {

    @Bean
    CommandLineRunner seed(PapelService papelService, UsuarioService usuarioService,
                           RecursoService recursoService) {
        return args -> {
            PapelDto adminPapel = seedPapel(papelService,
                    new PapelRequest("ADMIN", "Administrador do sistema"));
            PapelDto clientePapel = seedPapel(papelService,
                    new PapelRequest("CLIENTE", "Cliente do portal"));

            // Recursos protegíveis
            seedRecurso(recursoService, new RecursoRequest(
                    "Relatórios administrativos", "/api/admin/relatorios", "Apenas ADMIN"));
            seedRecurso(recursoService, new RecursoRequest(
                    "Painel do cliente", "/api/cliente/painel", "CLIENTE ou ADMIN"));

            // Usuários de demonstração (senhas codificadas pelo serviço)
            seedUsuario(usuarioService, new UsuarioRequest(
                    "admin", "admin@portal.local", "admin123", List.of(adminPapel.id())));
            seedUsuario(usuarioService, new UsuarioRequest(
                    "cliente", "cliente@portal.local", "cliente123", List.of(clientePapel.id())));
        };
    }

    private PapelDto seedPapel(PapelService papelService, PapelRequest request) {
        try {
            return papelService.criar(request);
        } catch (RegraNegocioException e) {
            // idempotente: papel já existe, retorna o cadastrado
            return papelService.buscarPorNome(request.nome())
                    .orElseThrow(() -> new IllegalStateException(
                            "Papel " + request.nome() + " não encontrado após conflito"));
        }
    }

    private void seedRecurso(RecursoService recursoService, RecursoRequest request) {
        try {
            recursoService.criar(request);
        } catch (RegraNegocioException ignored) {
            // já existe: idempotente
        }
    }

    private void seedUsuario(UsuarioService usuarioService, UsuarioRequest request) {
        try {
            usuarioService.cadastrar(request);
        } catch (RegraNegocioException ignored) {
            // já existe: idempotente
        }
    }
}
