package com.kelvinoliveira.marketplace.domain.dto;

import java.math.BigDecimal;
import com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio;

/**
 * DTO de sa\u00edda que representa um an\u00fancio de servi\u00e7o.
 */
public record AnuncioResponse(
        Long id,
        String titulo,
        String descricao,
        BigDecimal preco,
        String categoria,
        String prestadorId,
        StatusAnuncio status
) {
}
