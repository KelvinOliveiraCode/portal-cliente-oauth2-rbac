package com.kelvinoliveis.portalrbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de leitura de recurso protegível.
 */
public record RecursoDto(Long id,
                         @NotBlank @Size(max = 100) String nome,
                         @NotBlank @Size(max = 200) String caminho,
                         @Size(max = 200) String descricao) {
}
