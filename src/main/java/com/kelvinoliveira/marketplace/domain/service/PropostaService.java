package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.PropostaRequest;
import com.kelvinoliveira.marketplace.domain.dto.PropostaResponse;
import com.kelvinoliveira.marketplace.domain.exception.BusinessRuleException;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.Proposta;
import com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio;
import com.kelvinoliveira.marketplace.domain.modelo.StatusProposta;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import com.kelvinoliveira.marketplace.domain.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servi\u00e7o de propostas enviadas a an\u00fancias de servi\u00e7o.
 * Responsabilidades: enviar proposta para an\u00fancio dispon\u00edvel e aceitar
 * ou rejeitar uma proposta pendente.
 */
@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final AnuncioRepository anuncioRepository;

    public PropostaService(PropostaRepository propostaRepository,
                           AnuncioRepository anuncioRepository) {
        this.propostaRepository = propostaRepository;
        this.anuncioRepository = anuncioRepository;
    }

    /**
     * Envia uma proposta para um an\u00fancio dispon\u00edvel.
     *
     * @param request dados da proposta
     * @return proposta criada com status PENDENTE
     * @throws IllegalArgumentException se request for nulo
     * @throws ResourceNotFoundException se o an\u00fancio n\u00e3o existir
     * @throws BusinessRuleException se o an\u00fancio n\u00e3o estiver dispon\u00edvel
     */
    public PropostaResponse enviarProposta(PropostaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request de proposta n\u00e3o pode ser nulo");
        }
        var anuncio = anuncioRepository.buscarPorId(request.anuncioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "an\u00fancio com id " + request.anuncioId() + " n\u00e3o encontrado"));
        if (anuncio.status() != StatusAnuncio.DISPONIVEL) {
            throw new BusinessRuleException(
                    "an\u00fancio com id " + request.anuncioId() + " n\u00e3o est\u00e1 dispon\u00edvel para propostas");
        }
        Proposta proposta = new Proposta(null, request.anuncioId(), request.clienteId(),
                request.descricao(), request.precoProposto(), StatusProposta.PENDENTE);
        Proposta salvo = propostaRepository.salvar(proposta);
        return paraResposta(salvo);
    }

    /**
     * Aceita uma proposta pendente, tornando-a apta a ser contratada.
     *
     * @param propostaId identificador da proposta
     * @return proposta com status ACEITA
     * @throws ResourceNotFoundException se a proposta n\u00e3o existir
     * @throws BusinessRuleException se a proposta n\u00e3o estiver pendente
     */
    public PropostaResponse aceitarProposta(Long propostaId) {
        Proposta proposta = propostaRepository.buscarPorId(propostaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "proposta com id " + propostaId + " n\u00e3o encontrada"));
        if (proposta.status() != StatusProposta.PENDENTE) {
            throw new BusinessRuleException(
                    "proposta com id " + propostaId + " j\u00e1 est\u00e1 em " + proposta.status());
        }
        Proposta atualizada = new Proposta(proposta.id(), proposta.anuncioId(), proposta.clienteId(),
                proposta.descricao(), proposta.precoProposto(), StatusProposta.ACEITA);
        Proposta salvo = propostaRepository.salvar(atualizada);
        return paraResposta(salvo);
    }

    /**
     * Rejeita uma proposta pendente.
     *
     * @param propostaId identificador da proposta
     * @return proposta com status REJEITADA
     * @throws ResourceNotFoundException se a proposta n\u00e3o existir
     * @throws BusinessRuleException se a proposta n\u00e3o estiver pendente
     */
    public PropostaResponse rejeitarProposta(Long propostaId) {
        Proposta proposta = propostaRepository.buscarPorId(propostaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "proposta com id " + propostaId + " n\u00e3o encontrada"));
        if (proposta.status() != StatusProposta.PENDENTE) {
            throw new BusinessRuleException(
                    "proposta com id " + propostaId + " j\u00e1 est\u00e1 em " + proposta.status());
        }
        Proposta atualizada = new Proposta(proposta.id(), proposta.anuncioId(), proposta.clienteId(),
                proposta.descricao(), proposta.precoProposto(), StatusProposta.REJEITADA);
        Proposta salvo = propostaRepository.salvar(atualizada);
        return paraResposta(salvo);
    }

    /**
     * Lista propostas de um an\u00fancio.
     *
     * @param anuncioId identificador do an\u00fancio
     * @return lista de respostas de propostas do an\u00fancio
     */
    public List<PropostaResponse> listarPorAnuncio(Long anuncioId) {
        return propostaRepository.listarPorAnuncio(anuncioId).stream()
                .map(this::paraResposta)
                .toList();
    }

    private PropostaResponse paraResposta(Proposta proposta) {
        return new PropostaResponse(proposta.id(), proposta.anuncioId(), proposta.clienteId(),
                proposta.descricao(), proposta.precoProposto(), proposta.status());
    }
}
