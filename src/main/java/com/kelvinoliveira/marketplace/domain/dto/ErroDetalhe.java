package com.kelvinoliveira.marketplace.domain.dto;

/**
 * Corpo padronizado de resposta de erro da API.
 */
public record ErroDetalhe(
        int status,
        String erro,
        String mensagem
) {
}
