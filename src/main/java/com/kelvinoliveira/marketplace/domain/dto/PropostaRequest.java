package com.kelvinoliveira.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de entrada para envio de uma proposta a um an\u00fancio.
 */
public record PropostaRequest(
        @NotNull(message = "anuncioId \u00e9 obrigat\u00f3rio") Long anuncioId,
        @NotBlank(message = "clienteId \u00e9 obrigat\u00f3rio") String clienteId,
        @NotBlank(message = "descri\u00e7\u00e3o \u00e9 obrigat\u00f3ria") String descricao,
        @Positive(message = "pre\u00e7o proposto deve ser positivo") BigDecimal precoProposto
) {
}
