package com.kelvinoliveis.portalrbac.service;

import com.kelvinoliveis.portalrbac.domain.Papel;
import com.kelvinoliveis.portalrbac.domain.Usuario;
import com.kelvinoliveis.portalrbac.dto.AuthResponse;
import com.kelvinoliveis.portalrbac.dto.LoginRequest;
import com.kelvinoliveis.portalrbac.error.CredenciaisInvalidasException;
import com.kelvinoliveis.portalrbac.error.UsuarioInativoException;
import com.kelvinoliveis.portalrbac.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de autenticação OAuth2: valida credenciais contra o repositório de
 * usuários e emite o token JWT com os papeis (RBAC) do usuário.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       TokenService tokenService,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse autenticar(LoginRequest request) {
        Usuario usuario = usuarioRepository.buscarPorUsername(request.username())
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha inválidos"));
        if (!usuario.ativo()) {
            throw new UsuarioInativoException("Usuário inativo: " + usuario.username());
        }
        if (!passwordEncoder.matches(request.senha(), usuario.senha())) {
            throw new CredenciaisInvalidasException("Usuário ou senha inválidos");
        }
        List<SimpleGrantedAuthority> autoridades = usuario.roles().stream()
                .map(papel -> new SimpleGrantedAuthority("ROLE_" + papel.nome()))
                .toList();
        String token = tokenService.gerarToken(usuario.username(), autoridades);
        var autenticacao = new UsernamePasswordAuthenticationToken(usuario.username(), null, autoridades);
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
        List<String> nomes = usuario.roles().stream()
                .map(Papel::nome)
                .toList();
        return new AuthResponse(token, usuario.username(), nomes);
    }
}
