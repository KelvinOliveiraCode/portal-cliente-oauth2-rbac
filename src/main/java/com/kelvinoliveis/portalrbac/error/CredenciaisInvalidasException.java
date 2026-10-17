package com.kelvinoliveis.portalrbac.error;

/**
 * Credenciais de login inválidas (usuário não encontrado ou senha incorreta).
 */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
