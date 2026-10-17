package com.kelvinoliveira.marketplace.domain.dto;

import java.math.BigDecimal;
import com.kelvinoliveira.marketplace.domain.modelo.StatusProposta;

/**
 * DTO de sa\u00edda que representa uma proposta enviada a um an\u00fancio.
 */
public record PropostaResponse(
        Long id,
        Long anuncioId,
        String clienteId,
        String descricao,
        BigDecimal precoProposto,
        StatusProposta status
) {
}
