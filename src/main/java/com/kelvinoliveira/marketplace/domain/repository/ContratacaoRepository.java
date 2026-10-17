package com.kelvinoliveira.marketplace.domain.repository;

import com.kelvinoliveira.marketplace.domain.modelo.Contratacao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reposit\u00f3rio em mem\u00f3ria de contrata\u00e7\u00f5es fechadas no marketplace.
 * Determinista: IDs s\u00e3o sequenciais (AtomicLong).
 */
@Repository
public class ContratacaoRepository {

    private final ConcurrentHashMap<Long, Contratacao> contratacoes = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Contratacao salvar(Contratacao contratacao) {
        Long id = contratacao.id();
        if (id == null) {
            id = sequence.incrementAndGet();
        }
        Contratacao salvo = new Contratacao(id, contratacao.anuncioId(), contratacao.propostaId(),
                contratacao.prestadorId(), contratacao.clienteId(), contratacao.status());
        contratacoes.put(id, salvo);
        return salvo;
    }

    public Optional<Contratacao> buscarPorId(Long id) {
        return Optional.ofNullable(contratacoes.get(id));
    }

    public boolean exists(Long id) {
        return contratacoes.containsKey(id);
    }

    public List<Contratacao> listarPorAnuncio(Long anuncioId) {
        return contratacoes.values().stream()
                .filter(c -> c.anuncioId().equals(anuncioId))
                .toList();
    }

    public List<Contratacao> listarTodas() {
        return contratacoes.values().stream().toList();
    }

    public boolean existeContratacaoParaAnuncio(Long anuncioId) {
        return contratacoes.values().stream()
                .anyMatch(c -> c.anuncioId().equals(anuncioId));
    }

    public void remover(Long id) {
        contratacoes.remove(id);
    }
}
