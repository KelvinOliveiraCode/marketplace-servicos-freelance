package com.kelvinoliveira.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de entrada para publica\u00e7\u00e3o de um novo an\u00fancio de servi\u00e7o.
 */
public record AnuncioRequest(
        @NotBlank(message = "t\u00edtulo \u00e9 obrigat\u00f3rio") String titulo,
        @NotBlank(message = "descri\u00e7\u00e3o \u00e9 obrigat\u00f3ria") String descricao,
        @Positive(message = "pre\u00e7o deve ser positivo") BigDecimal preco,
        @NotBlank(message = "categoria \u00e9 obrigat\u00f3ria") String categoria,
        @NotBlank(message = "prestadorId \u00e9 obrigat\u00f3rio") String prestadorId
) {
}
