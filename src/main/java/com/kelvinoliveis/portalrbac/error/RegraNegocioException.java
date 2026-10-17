package com.kelvinoliveis.portalrbac.error;

/**
 * Violação de regra de negócio (ex: username ou papel duplicado).
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
