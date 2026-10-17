package com.kelvinoliveis.portalrbac.error;

/**
 * Usuário existe, mas está inativo e não pode autenticar.
 */
public class UsuarioInativoException extends RuntimeException {

    public UsuarioInativoException(String mensagem) {
        super(mensagem);
    }
}
