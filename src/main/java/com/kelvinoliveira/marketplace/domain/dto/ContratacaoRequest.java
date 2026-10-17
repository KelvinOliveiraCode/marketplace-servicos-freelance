package com.kelvinoliveira.marketplace.domain.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para fechamento de uma contrata\u00e7\u00e3o a partir de uma proposta.
 */
public record ContratacaoRequest(
        @NotNull(message = "propostaId \u00e9 obrigat\u00f3rio") Long propostaId
) {
}
