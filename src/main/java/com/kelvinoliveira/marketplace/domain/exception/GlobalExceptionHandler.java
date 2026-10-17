package com.kelvinoliveira.marketplace.domain.exception;

import com.kelvinoliveira.marketplace.domain.dto.ErroDetalhe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tratador global de exce\u00e7\u00f5es da API.
 * Converte exce\u00e7\u00f5es de domínio e de valida\u00e7\u00e3o em respostas HTTP
 * com corpo padronizado (ErroDetalhe), sem vazar stack traces ao cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroDetalhe> naoEncontrado(ResourceNotFoundException ex) {
        return erro(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErroDetalhe> regraDeNegocio(BusinessRuleException ex) {
        return erro(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroDetalhe> argumentoInvalido(IllegalArgumentException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDetalhe> validacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        String mensagem = detalhes.isEmpty() ? "requisi\u00e7\u00e3o inv\u00e1lida"
                : String.join("; ", detalhes);
        return erro(HttpStatus.BAD_REQUEST, "Validation Failed", mensagem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDetalhe> inesperado(Exception ex) {
        return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "erro inesperado ao processar a requisi\u00e7\u00e3o");
    }

    private ResponseEntity<ErroDetalhe> erro(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity.status(status)
                .body(new ErroDetalhe(status.value(), erro, mensagem));
    }
}
