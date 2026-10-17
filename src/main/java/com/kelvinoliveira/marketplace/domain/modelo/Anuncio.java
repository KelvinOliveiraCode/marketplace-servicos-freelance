package com.kelvinoliveira.marketplace.domain.modelo;

import java.math.BigDecimal;

/**
 * An\u00fancio de servi\u00e7o publicado por um prestador no marketplace.
 * Imut\u00e1vel: qualquer altera\u00e7\u00e3o de status produz uma nova inst\u00e2ncia
 * atrav\u00e9s do reposit\u00f3rio.
 */
public record Anuncio(
        Long id,
        String titulo,
        String descricao,
        BigDecimal preco,
        String categoria,
        String prestadorId,
        StatusAnuncio status
) {
}
