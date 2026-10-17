package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.MensagemDto;
import com.kelvinoliveis.portalrbac.error.CredenciaisInvalidasException;
import com.kelvinoliveis.portalrbac.error.EntidadeNaoEncontradaException;
import com.kelvinoliveis.portalrbac.error.RegraNegocioException;
import com.kelvinoliveis.portalrbac.error.UsuarioInativoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tradutor centralizado de exceções para respostas HTTP com corpo padronizado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<MensagemDto> naoEncontrada(EntidadeNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemDto(e.getMessage()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<MensagemDto> regraNegocio(RegraNegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemDto(e.getMessage()));
    }

    @ExceptionHandler({CredenciaisInvalidasException.class, UsuarioInativoException.class})
    public ResponseEntity<MensagemDto> credenciais(RuntimeException e) {
        HttpStatus status = e instanceof UsuarioInativoException ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new MensagemDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemDto> validacao(MethodArgumentNotValidException e) {
        List<String> erros = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensagemDto("Erro de validação: " + String.join("; ", erros)));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MensagemDto> acessoNegado(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensagemDto("Acesso negado: " + e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MensagemDto> naoAutenticado(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensagemDto("Não autenticado: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemDto> inesperado(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensagemDto("Erro inesperado: " + e.getMessage()));
    }
}
