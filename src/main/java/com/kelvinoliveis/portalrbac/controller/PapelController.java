package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.PapelDto;
import com.kelvinoliveis.portalrbac.dto.PapelRequest;
import com.kelvinoliveis.portalrbac.service.PapelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de papéis (RBAC): ADMIN, CLIENTE, etc.
 */
@RestController
@RequestMapping("/api/papeis")
public class PapelController {

    private final PapelService service;

    public PapelController(PapelService service) {
        this.service = service;
    }

    @GetMapping
    public List<PapelDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PapelDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PapelDto> criar(@Valid @RequestBody PapelRequest request) {
        PapelDto dto = service.criar(request);
        return ResponseEntity.created(URI.create("/api/papeis/" + dto.id())).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
