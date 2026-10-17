package com.kelvinoliveira.marketplace.domain.repository;

import com.kelvinoliveira.marketplace.domain.modelo.Proposta;
import com.kelvinoliveira.marketplace.domain.modelo.StatusProposta;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reposit\u00f3rio em mem\u00f3ria de propostas enviadas a an\u00fancias.
 * Determinista: IDs s\u00e3o sequenciais (AtomicLong).
 */
@Repository
public class PropostaRepository {

    private final ConcurrentHashMap<Long, Proposta> propostas = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Proposta salvar(Proposta proposta) {
        Long id = proposta.id();
        if (id == null) {
            id = sequence.incrementAndGet();
        }
        Proposta salvo = new Proposta(id, proposta.anuncioId(), proposta.clienteId(),
                proposta.descricao(), proposta.precoProposto(), proposta.status());
        propostas.put(id, salvo);
        return salvo;
    }

    public Optional<Proposta> buscarPorId(Long id) {
        return Optional.ofNullable(propostas.get(id));
    }

    public boolean exists(Long id) {
        return propostas.containsKey(id);
    }

    public List<Proposta> listarPorAnuncio(Long anuncioId) {
        return propostas.values().stream()
                .filter(p -> p.anuncioId().equals(anuncioId))
                .toList();
    }

    public List<Proposta> listarTodas() {
        return propostas.values().stream().toList();
    }

    public long contarPorAnuncioESituacao(Long anuncioId, StatusProposta status) {
        return propostas.values().stream()
                .filter(p -> p.anuncioId().equals(anuncioId) && p.status() == status)
                .count();
    }

    public void remover(Long id) {
        propostas.remove(id);
    }
}
