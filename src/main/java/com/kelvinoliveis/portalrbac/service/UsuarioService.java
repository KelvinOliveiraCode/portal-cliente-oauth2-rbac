package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.domain.Usuario;
import com.kelvinoliveis.portalrbac.dto.UsuarioDto;
import com.kelvinoliveis.portalrbac.dto.UsuarioRequest;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.repository.PapelRepository;
import com.kelvinoliveis.portalrbac.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Serviço de usuários: cadastro, consulta, ativação/desativação e gestão de papéis
 * (RBAC). A senha é sempre codificada antes de persistir.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PapelRepository papelRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDto> listar() {
        return usuarioRepository.listarTodos().stream()
                .map(this::paraDto)
                .toList();
    }

    public UsuarioDto buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .map(this::paraDto)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
    }

    public UsuarioDto cadastrar(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new RegraNegocioException("Usuário já cadastrado: " + request.username());
        }
        Set<Papel> papeis = validarPapeis(request.roleIds());
        Usuario usuario = new Usuario(null, request.username(), request.email(),
                passwordEncoder.encode(request.senha()), true, papeis);
        return paraDto(usuarioRepository.salvar(usuario));
    }

    public UsuarioDto atualizar(Long id, UsuarioRequest request) {
        Usuario existente = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
        if (!existente.username().equalsIgnoreCase(request.username())
                && usuarioRepository.existsByUsername(request.username())) {
            throw new RegraNegocioException("Usuário já cadastrado: " + request.username());
        }
        Set<Papel> papeis = validarPapeis(request.roleIds());
        Usuario atualizado = new Usuario(id, request.username(), request.email(),
                existente.senha(), existente.ativo(), papeis);
        return paraDto(usuarioRepository.salvar(atualizado));
    }

    public void desativar(Long id) {
        Usuario existente = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + id));
        Usuario desativado = new Usuario(existente.id(), existente.username(), existente.email(),
                existente.senha(), false, existente.roles());
        usuarioRepository.salvar(desativado);
    }

    public void atribuirPapel(Long usuarioId, Long papelId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
        Papel papel = papelRepository.buscarPorId(papelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Papel não encontrado: " + papelId));
        Set<Papel> papeis = new LinkedHashSet<>(usuario.roles());
        papeis.add(papel);
        Usuario atualizado = new Usuario(usuario.id(), usuario.username(), usuario.email(),
                usuario.senha(), usuario.ativo(), papeis);
        usuarioRepository.salvar(atualizado);
    }

    public void removerPapel(Long usuarioId, Long papelId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado: " + usuarioId));
        papelRepository.buscarPorId(papelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Papel não encontrado: " + papelId));
        Set<Papel> papeis = new LinkedHashSet<>(usuario.roles());
        papeis.removeIf(p -> p.id().equals(papelId));
        Usuario atualizado = new Usuario(usuario.id(), usuario.username(), usuario.email(),
                usuario.senha(), usuario.ativo(), papeis);
        usuarioRepository.salvar(atualizado);
    }

    private Set<Papel> validarPapeis(List<Long> roleIds) {
        if (roleIds == null) {
            return Set.of();
        }
        Set<Papel> papeis = new LinkedHashSet<>();
        for (Long roleId : roleIds) {
            Papel papel = papelRepository.buscarPorId(roleId)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Papel não encontrado: " + roleId));
            papeis.add(papel);
        }
        return papeis;
    }

    private UsuarioDto paraDto(Usuario usuario) {
        List<String> nomes = usuario.roles().stream()
                .map(Papel::nome)
                .toList();
        return new UsuarioDto(usuario.id(), usuario.username(), usuario.email(), usuario.ativo(), nomes);
    }
}
