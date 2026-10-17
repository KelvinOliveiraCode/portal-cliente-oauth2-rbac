package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.RecursoDto;
import com.kelvinoliveis.portalrbac.dto.RecursoRequest;
import com.kelvinoliveis.portalrbac.service.RecursoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de recursos protegíveis do portal cliente.
 */
@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService service;

    public RecursoController(RecursoService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecursoDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RecursoDto> criar(@Valid @RequestBody RecursoRequest request) {
        RecursoDto dto = service.criar(request);
        return ResponseEntity.created(URI.create("/api/recursos/" + dto.id())).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
