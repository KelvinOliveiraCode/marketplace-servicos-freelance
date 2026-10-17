package com.kelvinoliveira.marketplace.domain.repository;

import com.kelvinoliveira.marketplace.domain.modelo.Anuncio;
import com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reposit\u00f3rio em mem\u00f3ria de an\u00fancias de servi\u00e7o.
 * Determinista: IDs s\u00e3o sequenciais (AtomicLong) e n\u00e3o h\u00e1 depend\u00eancia
 * de banco de dados externo.
 */
@Repository
public class AnuncioRepository {

    private final ConcurrentHashMap<Long, Anuncio> anuncios = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    /**
     * Persiste (ou substitui) um an\u00fancio. IDs nulos recebem um novo ID
     * sequencial; IDs j\u00e1 existentes sobrescrevem a entrada.
     *
     * @param anuncio an\u00fancio a salvar
     * @return an\u00fancio salvo com ID atribu\u00eddo
     */
    public Anuncio salvar(Anuncio anuncio) {
        Long id = anuncio.id();
        if (id == null) {
            id = sequence.incrementAndGet();
        }
        Anuncio salvo = new Anuncio(id, anuncio.titulo(), anuncio.descricao(),
                anuncio.preco(), anuncio.categoria(), anuncio.prestadorId(), anuncio.status());
        anuncios.put(id, salvo);
        return salvo;
    }

    /**
     * Busca an\u00fancio pelo ID.
     *
     * @param id identificador do an\u00fancio
     * @return Optional contendo o an\u00fancio, ou vazio se n\u00e3o existir
     */
    public Optional<Anuncio> buscarPorId(Long id) {
        return Optional.ofNullable(anuncios.get(id));
    }

    /**
     * Verifica se existe an\u00fancio com o ID informado.
     *
     * @param id identificador do an\u00fancio
     * @return true se existir, false caso contr\u00e1rio
     */
    public boolean exists(Long id) {
        return anuncios.containsKey(id);
    }

    /**
     * Lista an\u00fancias com status DISPONIVEL, na ordem de cria\u00e7\u00e3o.
     *
     * @return lista imut\u00e1vel de an\u00fancias dispon\u00edveis
     */
    public List<Anuncio> listarDisponiveis() {
        return anuncios.values().stream()
                .filter(a -> a.status() == StatusAnuncio.DISPONIVEL)
                .toList();
    }

    /**
     * Lista todos os an\u00fancias, independentemente do status.
     *
     * @return lista imut\u00e1vel de todos os an\u00fancias
     */
    public List<Anuncio> listarTodos() {
        return anuncios.values().stream().toList();
    }

    /**
     * Remove um an\u00fancio pelo ID.
     *
     * @param id identificador do an\u00fancio
     */
    public void remover(Long id) {
        anuncios.remove(id);
    }
}
