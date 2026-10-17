package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.ContratacaoRequest;
import com.kelvinoliveira.marketplace.domain.dto.ContratacaoResponse;
import com.kelvinoliveira.marketplace.domain.exception.BusinessRuleException;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.StatusContratacao;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import com.kelvinoliveira.marketplace.domain.repository.ContratacaoRepository;
import com.kelvinoliveira.marketplace.domain.repository.PropostaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContratacaoServiceTest {

    private final AnuncioRepository anuncioRepository = new AnuncioRepository();
    private final PropostaRepository propostaRepository = new PropostaRepository();
    private final ContratacaoRepository contratacaoRepository = new ContratacaoRepository();
    private final ContratacaoService service = new ContratacaoService(
            propostaRepository, anuncioRepository, contratacaoRepository);

    @Test
    void shouldCriarContratacaoAtivaEMarcarAnuncioContratadoQuandoPropostaEstaAceita() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "Consultoria", "desc", new BigDecimal("2500"), "TI", "prestador-1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = propostaRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Proposta(
                null, anuncioId, "cliente-1", "descricao", new BigDecimal("2400"),
                com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.ACEITA)).id();

        ContratacaoResponse resposta = service.contratar(new ContratacaoRequest(propostaId));

        assertThat(resposta.id()).isNotNull();
        assertThat(resposta.status()).isEqualTo(StatusContratacao.ATIVA);
        assertThat(resposta.prestadorId()).isEqualTo("prestador-1");
        assertThat(resposta.clienteId()).isEqualTo("cliente-1");
        // an\u00fancio agora est\u00e1 contratado
        var anuncio = anuncioRepository.buscarPorId(anuncioId).get();
        assertThat(anuncio.status()).isEqualTo(
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.CONTRATADO);
    }

    @Test
    void shouldLancarBusinessRuleExceptionQuandoContratarPropostaNaoAceita() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = propostaRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Proposta(
                null, anuncioId, "cliente-1", "descricao", new BigDecimal("700"),
                com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.PENDENTE)).id();

        assertThrows(BusinessRuleException.class,
                () -> service.contratar(new ContratacaoRequest(propostaId)));
    }

    @Test
    void shouldLancarBusinessRuleExceptionQuandoAnuncioNaoEstaDisponivel() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.CONTRATADO)).id();
        Long propostaId = propostaRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Proposta(
                null, anuncioId, "cliente-1", "descricao", new BigDecimal("700"),
                com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.ACEITA)).id();

        assertThrows(BusinessRuleException.class,
                () -> service.contratar(new ContratacaoRequest(propostaId)));
    }

    @Test
    void shouldLancarBusinessRuleExceptionQuandoJaExisteContratacaoParaAnuncio() {
        Long anuncioId = anuncioRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Anuncio(
                null, "A", "desc", new BigDecimal("100"), "cat", "p1",
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL)).id();
        Long propostaId = propostaRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Proposta(
                null, anuncioId, "cliente-1", "descricao", new BigDecimal("700"),
                com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.ACEITA)).id();
        service.contratar(new ContratacaoRequest(propostaId));
        Long propostaId2 = propostaRepository.salvar(new com.kelvinoliveira.marketplace.domain.modelo.Proposta(
                null, anuncioId, "cliente-2", "descricao 2", new BigDecimal("900"),
                com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.ACEITA)).id();

        assertThrows(BusinessRuleException.class,
                () -> service.contratar(new ContratacaoRequest(propostaId2)));
    }

    @Test
    void shouldLancarResourceNotFoundExceptionQuandoContratarPropostaInexistente() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.contratar(new ContratacaoRequest(99L)));
    }
}
