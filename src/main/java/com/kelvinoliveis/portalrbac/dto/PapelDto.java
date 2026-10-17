package com.kelvinoliveis.portalrbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de leitura de papel (sem expor dados internos do repositório).
 */
public record PapelDto(Long id,
                       @NotBlank @Size(max = 50) String nome,
                       @Size(max = 200) String descricao) {
}
