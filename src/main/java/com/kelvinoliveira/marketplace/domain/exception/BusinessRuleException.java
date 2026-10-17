package com.kelvinoliveira.marketplace.domain.exception;

/**
 * Lan\u00e7ado quando uma regra de neg\u00f3cio impede a opera\u00e7\u00e3o
 * (ex.: contratar proposta j\u00e1 usada ou an\u00fancio j\u00e1 contratado).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String mensagem) {
        super(mensagem);
    }

}
