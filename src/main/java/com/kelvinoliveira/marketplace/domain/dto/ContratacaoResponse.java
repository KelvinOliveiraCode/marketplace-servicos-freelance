package com.kelvinoliveira.marketplace.domain.dto;

import com.kelvinoliveira.marketplace.domain.modelo.StatusContratacao;

/**
 * DTO de sa\u00edda que representa uma contrata\u00e7\u00e3o fechada no marketplace.
 */
public record ContratacaoResponse(
        Long id,
        Long anuncioId,
        Long propostaId,
        String prestadorId,
        String clienteId,
        StatusContratacao status
) {
}
