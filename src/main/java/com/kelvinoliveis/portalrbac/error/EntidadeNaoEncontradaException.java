package com.kelvinoliveis.portalrbac.error;

/**
 * Entidade requisitada não existe no repositório.
 */
public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
