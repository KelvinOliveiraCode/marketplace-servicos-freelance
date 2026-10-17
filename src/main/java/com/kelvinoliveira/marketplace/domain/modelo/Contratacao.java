package com.kelvinoliveira.marketplace.domain.modelo;

/**
 * Contrata\u00e7\u00e3o fechada a partir de uma proposta aceita.
 * Vincula um an\u00fancio, a proposta aceita, o prestador e o cliente.
 * Imut\u00e1vel: mudan\u00e7a de status produz nova inst\u00e2ncia via reposit\u00f3rio.
 */
public record Contratacao(
        Long id,
        Long anuncioId,
        Long propostaId,
        String prestadorId,
        String clienteId,
        StatusContratacao status
) {
}
