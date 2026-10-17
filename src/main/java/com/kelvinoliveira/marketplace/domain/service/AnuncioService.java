package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.AnuncioRequest;
import com.kelvinoliveira.marketplace.domain.dto.AnuncioResponse;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.Anuncio;
import com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servi\u00e7o de an\u00fancias de servi\u00e7o.
 * Responsabilidades: publicar um an\u00fancio dispon\u00edvel e listar an\u00fancias
 * abertos \u00e0 contrata\u00e7\u00e3o.
 */
@Service
public class AnuncioService {

    private final AnuncioRepository repository;

    public AnuncioService(AnuncioRepository repository) {
        this.repository = repository;
    }

    /**
     * Publica um novo an\u00fancio com status DISPONIVEL.
     *
     * @param request dados do an\u00fancio a publicar
     * @return an\u00fancio publicado com ID atribu\u00eddo
     * @throws IllegalArgumentException se request for nulo
     */
    public AnuncioResponse publicar(AnuncioRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request de an\u00fancio n\u00e3o pode ser nulo");
        }
        Anuncio anuncio = new Anuncio(null, request.titulo(), request.descricao(),
                request.preco(), request.categoria(), request.prestadorId(),
                StatusAnuncio.DISPONIVEL);
        Anuncio salvo = repository.salvar(anuncio);
        return paraResposta(salvo);
    }

    /**
     * Busca an\u00fancio pelo ID.
     *
     * @param id identificador do an\u00fancio
     * @return resposta do an\u00fancio encontrado
     * @throws ResourceNotFoundException se n\u00e3o existir
     */
    public AnuncioResponse buscarPorId(Long id) {
        Anuncio anuncio = repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "an\u00fancio com id " + id + " n\u00e3o encontrado"));
        return paraResposta(anuncio);
    }

    /**
     * Lista an\u00fancias com status DISPONIVEL.
     *
     * @return lista de respostas de an\u00fancias dispon\u00edveis
     */
    public List<AnuncioResponse> listarDisponiveis() {
        return repository.listarDisponiveis().stream()
                .map(this::paraResposta)
                .toList();
    }

    /**
     * Marca um an\u00fancio como CONTRATADO (usado ao fechar uma contrata\u00e7\u00e3o).
     *
     * @param id identificador do an\u00fancio
     * @throws ResourceNotFoundException se n\u00e3o existir
     */
    public void marcarComoContratado(Long id) {
        Anuncio anuncio = repository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "an\u00fancio com id " + id + " n\u00e3o encontrado"));
        Anuncio atualizado = new Anuncio(anuncio.id(), anuncio.titulo(), anuncio.descricao(),
                anuncio.preco(), anuncio.categoria(), anuncio.prestadorId(),
                StatusAnuncio.CONTRATADO);
        repository.salvar(atualizado);
    }

    private AnuncioResponse paraResposta(Anuncio anuncio) {
        return new AnuncioResponse(anuncio.id(), anuncio.titulo(), anuncio.descricao(),
                anuncio.preco(), anuncio.categoria(), anuncio.prestadorId(), anuncio.status());
    }
}
