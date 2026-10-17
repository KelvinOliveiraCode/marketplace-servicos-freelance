package com.kelvinoliveira.marketplace.domain.modelo;

/**
 * Ciclo de vida de uma proposta enviada a um an\u00fancio.
 * PENDENTE: aguarda decis\u00e3o do prestador.
 * ACEITA: pronta para ser convertida em contrata\u00e7\u00e3o.
 * REJEITADA: recusada pelo prestador.
 */
public enum StatusProposta {
    PENDENTE,
    ACEITA,
    REJEITADA
}
