package com.kelvinoliveira.marketplace.domain.modelo;

import java.math.BigDecimal;

/**
 * Proposta enviada por um cliente a um an\u00fancio de servi\u00e7o.
 * Imut\u00e1vel: mudan\u00e7a de status produz nova inst\u00e2ncia via reposit\u00f3rio.
 */
public record Proposta(
        Long id,
        Long anuncioId,
        String clienteId,
        String descricao,
        BigDecimal precoProposto,
        StatusProposta status
) {
}
