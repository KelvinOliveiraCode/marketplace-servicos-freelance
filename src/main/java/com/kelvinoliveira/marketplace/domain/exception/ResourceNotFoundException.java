package com.kelvinoliveira.marketplace.domain.exception;

/**
 * Lan\u00e7ado quando um recurso (an\u00fancio, proposta ou contrata\u00e7\u00e3o)
 * n\u00e3o \u00e9 encontrado pelo identificador informado.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }

}
