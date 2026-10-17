package com.kelvinoliveira.marketplace.domain.modelo;

/**
 * Ciclo de vida de um an\u00fancio de servi\u00e7o.
 * DISPONIVEL: aceita propostas.
 * CONTRATADO: j\u00e1 fechado em uma contrata\u00e7\u00e3o.
 * INATIVO: removido pelo prestador, n\u00e3o aceita mais propostas.
 */
public enum StatusAnuncio {
    DISPONIVEL,
    CONTRATADO,
    INATIVO
}
