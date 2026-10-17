package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.PropostaRequest;
import com.kelvinoliveira.marketplace.domain.dto.PropostaResponse;
import com.kelvinoliveira.marketplace.domain.exception.BusinessRuleException;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.StatusProposta;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import com.kelvinoliveira.marketplace.domain.repository.PropostaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropostaServiceTest {

    private final AnuncioRepository anuncioRepository = new AnuncioRepository();
    private final PropostaRepository propostaRepository = new PropostaRepository();
    private final PropostaService service = new PropostaService(propostaRepository, anuncioRepository);

    @Test
    void shouldCriarPropostaComStatusPendenteQuandoAnuncioEstaDisponivel() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "Design de logo", "Cria\u00e7\u00e3o de marca", new BigDecimal("800"),
                "Design", "prestador-1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();

        PropostaRequest request = new PropostaRequest(anuncioId, "cliente-1",
                "Quero uma marca moderna", new BigDecimal("700"));
        PropostaResponse resposta = service.enviarProposta(request);

        assertThat(resposta.id()).isNotNull();
        assertThat(resposta.anuncioId()).isEqualTo(anuncioId);
        assertThat(resposta.status()).isEqualTo(StatusProposta.PENDENTE);
    }

    @Test
    void shouldLancarResourceNotFoundExceptionQuandoAnuncioNaoExiste() {
        PropostaRequest request = new PropostaRequest(99L, "cliente-1",
                "descricao", new BigDecimal("700"));
        assertThrows(ResourceNotFoundException.class, () -> service.enviarProposta(request));
    }

    @Test
    void shouldLancarBusinessRuleExceptionQuandoAnuncioNaoEstaDisponivel() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.CONTRATADO)).id();

        PropostaRequest request = new PropostaRequest(anuncioId, "cliente-1",
                "descricao", new BigDecimal("700"));
        assertThrows(BusinessRuleException.class, () -> service.enviarProposta(request));
    }

    @Test
    void shouldAceitarPropostaPendenteERetornarStatusAceita() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = service.enviarProposta(new PropostaRequest(anuncioId, "cliente-1",
                "descricao", new BigDecimal("700"))).id();

        PropostaResponse resposta = service.aceitarProposta(propostaId);

        assertThat(resposta.status()).isEqualTo(StatusProposta.ACEITA);
    }

    @Test
    void shouldRejeitarPropostaPendenteERetornarStatusRejeitada() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = service.enviarProposta(new PropostaRequest(anuncioId, "cliente-1",
                "descricao", new BigDecimal("700"))).id();

        PropostaResponse resposta = service.rejeitarProposta(propostaId);

        assertThat(resposta.status()).isEqualTo(StatusProposta.REJEITADA);
    }

    @Test
    void shouldLancarBusinessRuleExceptionQuandoAceitarPropostaQueNaoEstaPendente() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = service.enviarProposta(new PropostaRequest(anuncioId, "cliente-1",
                "descricao", new BigDecimal("700"))).id();
        service.aceitarProposta(propostaId);

        assertThrows(BusinessRuleException.class, () -> service.aceitarProposta(propostaId));
    }

    @Test
    void shouldLancarResourceNotFoundExceptionQuandoAceitarPropostaInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> service.aceitarProposta(99L));
    }
}
