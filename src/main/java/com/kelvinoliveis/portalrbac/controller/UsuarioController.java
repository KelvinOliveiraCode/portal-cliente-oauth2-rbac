package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.UsuarioDto;
import com.kelvinoliveis.portalrbac.dto.UsuarioRequest;
import com.kelvinoliveis.portalrbac.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de usuários (RBAC). Apenas ADMIN gerencia usuários.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioDto dto = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + dto.id())).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> atualizar(@PathVariable Long id,
                                                @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/papeis")
    public ResponseEntity<Void> atribuirPapel(@PathVariable Long usuarioId,
                                              @RequestParam Long papelId) {
        service.atribuirPapel(usuarioId, papelId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}/papeis/{papelId}")
    public ResponseEntity<Void> removerPapel(@PathVariable Long usuarioId,
                                             @PathVariable Long papelId) {
        service.removerPapel(usuarioId, papelId);
        return ResponseEntity.noContent().build();
    }
}
