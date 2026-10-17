package com.kelvinoliveis.portalrbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação/edição de papel.
 */
public record PapelRequest(@NotBlank @Size(max = 50) String nome,
                           @Size(max = 200) String descricao) {
}
