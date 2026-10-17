package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.ContratacaoRequest;
import com.kelvinoliveira.marketplace.domain.dto.ContratacaoResponse;
import com.kelvinoliveira.marketplace.domain.exception.BusinessRuleException;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.Anuncio;
import com.kelvinoliveira.marketplace.domain.modelo.Contratacao;
import com.kelvinoliveira.marketplace.domain.modelo.Proposta;
import com.kelvinoliveira.marketplace.domain.modelo.StatusContratacao;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import com.kelvinoliveira.marketplace.domain.repository.ContratacaoRepository;
import com.kelvinoliveira.marketplace.domain.repository.PropostaRepository;
import org.springframework.stereotype.Service;

/**
 * Servi\u00e7o de contrata\u00e7\u00e3o.
 * Fecha um servi\u00e7o a partir de uma proposta aceita e marca o an\u00fancio
 * como CONTRATADO, impedindo nova contrata\u00e7\u00e3o sobre o mesmo an\u00fancio.
 */
@Service
public class ContratacaoService {

    private final PropostaRepository propostaRepository;
    private final AnuncioRepository anuncioRepository;
    private final ContratacaoRepository contratacaoRepository;

    public ContratacaoService(PropostaRepository propostaRepository,
                              AnuncioRepository anuncioRepository,
                              ContratacaoRepository contratacaoRepository) {
        this.propostaRepository = propostaRepository;
        this.anuncioRepository = anuncioRepository;
        this.contratacaoRepository = contratacaoRepository;
    }

    /**
     * Fecha a contrata\u00e7\u00e3o a partir de uma proposta aceita.
     *
     * @param request dados da contrata\u00e7\u00e3o (propostaId)
     * @return contrata\u00e7\u00e3o criada com status ATIVA
     * @throws IllegalArgumentException   se request for nulo
     * @throws ResourceNotFoundException  se proposta ou an\u00fancio n\u00e3o existirem
     * @throws BusinessRuleException      se proposta n\u00e3o estiver aceita,
     *                                    an\u00fancio n\u00e3o estiver dispon\u00edvel
     *                                    ou j\u00e1 houver contrata\u00e7\u00e3o para o an\u00fancio
     */
    public ContratacaoResponse contratar(ContratacaoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request de contrata\u00e7\u00e3o n\u00e3o pode ser nulo");
        }
        Proposta proposta = propostaRepository.buscarPorId(request.propostaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "proposta com id " + request.propostaId() + " n\u00e3o encontrada"));
        if (proposta.status() != com.kelvinoliveira.marketplace.domain.modelo.StatusProposta.ACEITA) {
            throw new BusinessRuleException(
                    "s\u00f3 \u00e9 poss\u00edvel contratar proposta com status ACEITA; proposta "
                            + request.propostaId() + " est\u00e1 em " + proposta.status());
        }
        var anuncioOpt = anuncioRepository.buscarPorId(proposta.anuncioId());
        if (anuncioOpt.isEmpty()) {
            throw new ResourceNotFoundException(
                    "an\u00fancio com id " + proposta.anuncioId() + " n\u00e3o encontrado");
        }
        var anuncio = anuncioOpt.get();
        if (anuncio.status() != com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.DISPONIVEL) {
            throw new BusinessRuleException(
                    "an\u00fancio com id " + proposta.anuncioId() + " j\u00e1 est\u00e1 em "
                            + anuncio.status());
        }
        if (contratacaoRepository.existeContratacaoParaAnuncio(proposta.anuncioId())) {
            throw new BusinessRuleException(
                    "j\u00e1 existe contrata\u00e7\u00e3o para o an\u00fancio com id "
                            + proposta.anuncioId());
        }

        // Marca o an\u00fancio como contratado e cria a contrata\u00e7\u00e3o ativa.
        Anuncio contratado = new Anuncio(anuncio.id(), anuncio.titulo(), anuncio.descricao(),
                anuncio.preco(), anuncio.categoria(), anuncio.prestadorId(),
                com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio.CONTRATADO);
        anuncioRepository.salvar(contratado);

        Contratacao nova = new Contratacao(null, proposta.anuncioId(), proposta.id(),
                anuncio.prestadorId(), proposta.clienteId(), StatusContratacao.ATIVA);
        Contratacao salvo = contratacaoRepository.salvar(nova);
        return paraResposta(salvo);
    }

    /**
     * Busca contrata\u00e7\u00e3o pelo ID.
     *
     * @param id identificador da contrata\u00e7\u00e3o
     * @return resposta da contrata\u00e7\u00e3o encontrada
     * @throws ResourceNotFoundException se n\u00e3o existir
     */
    public ContratacaoResponse buscarPorId(Long id) {
        Contratacao contratacao = contratacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "contrata\u00e7\u00e3o com id " + id + " n\u00e3o encontrada"));
        return paraResposta(contratacao);
    }

    private ContratacaoResponse paraResposta(Contratacao contratacao) {
        return new ContratacaoResponse(contratacao.id(), contratacao.anuncioId(),
                contratacao.propostaId(), contratacao.prestadorId(), contratacao.clienteId(),
                contratacao.status());
    }
}
