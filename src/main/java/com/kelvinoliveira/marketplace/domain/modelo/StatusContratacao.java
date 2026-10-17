package com.kelvinoliveira.marketplace.domain.modelo;

/**
 * Ciclo de vida de uma contrata\u00e7\u00e3o fechada no marketplace.
 * ATIVA: servi\u00e7o em andamento.
 * FINALIZADA: servi\u00e7o entregue e encerrado.
 * CANCELADA: contrata\u00e7\u00e3o desfeita antes da entrega.
 */
public enum StatusContratacao {
    ATIVA,
    FINALIZADA,
    CANCELADA
}
