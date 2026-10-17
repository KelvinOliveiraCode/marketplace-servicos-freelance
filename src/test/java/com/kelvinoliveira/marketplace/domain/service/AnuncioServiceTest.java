package com.kelvinoliveira.marketplace.domain.service;

import com.kelvinoliveira.marketplace.domain.dto.AnuncioRequest;
import com.kelvinoliveira.marketplace.domain.dto.AnuncioResponse;
import com.kelvinoliveira.marketplace.domain.exception.ResourceNotFoundException;
import com.kelvinoliveira.marketplace.domain.modelo.StatusAnuncio;
import com.kelvinoliveira.marketplace.domain.repository.AnuncioRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnuncioServiceTest {

    private final AnuncioRepository repository = new AnuncioRepository();
    private final AnuncioService service = new AnuncioService(repository);

    @Test
    void shouldCriarAnuncioComStatusDisponivelQuandoPublicarRequestValido() {
        AnuncioRequest request = new AnuncioRequest("Consultoria de TI",
                "Auditoria de sistemas legados", new BigDecimal("2500.00"),
                "TI", "prestador-1");
        AnuncioResponse resposta = service.publicar(request);

        assertThat(resposta.id()).isNotNull();
        assertThat(resposta.titulo()).isEqualTo("Consultoria de TI");
        assertThat(resposta.preco()).isEqualByComparingTo(new BigDecimal("2500.00"));
        assertThat(resposta.status()).isEqualTo(StatusAnuncio.DISPONIVEL);
    }

    @Test
    void shouldAtribuirIdsSequenciaisQuandoPublicarVariosAnuncios() {
        AnuncioResponse primeiro = service.publicar(new AnuncioRequest("A", "desc A",
                new BigDecimal("100"), "cat", "p1"));
        AnuncioResponse segundo = service.publicar(new AnuncioRequest("B", "desc B",
                new BigDecimal("200"), "cat", "p2"));

        assertThat(primeiro.id()).isEqualTo(1L);
        assertThat(segundo.id()).isEqualTo(2L);
    }

    @Test
    void shouldLancarResourceNotFoundExceptionQuandoBuscarAnuncioInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void shouldListarApenasAnunciosDisponiveisQuandoAlgumEstaContratado() {
        AnuncioResponse a = service.publicar(new AnuncioRequest("A", "desc A",
                new BigDecimal("100"), "cat", "p1"));
        AnuncioResponse b = service.publicar(new AnuncioRequest("B", "desc B",
                new BigDecimal("200"), "cat", "p2"));
        service.marcarComoContratado(b.id());

        var disponiveis = service.listarDisponiveis();

        assertThat(disponiveis).extracting(AnuncioResponse::titulo)
                .containsExactly("A");
    }

    @Test
    void shouldRetornarListaVaziaQuandoNenhumAnuncioEstaDisponivel() {
        service.publicar(new AnuncioRequest("A", "desc A", new BigDecimal("100"), "cat", "p1"));
        service.marcarComoContratado(1L);

        var disponiveis = service.listarDisponiveis();

        assertThat(disponiveis).isEmpty();
    }

    @Test
    void shouldLancarIllegalArgumentExceptionQuandoPublicarRequestNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.publicar(null));
    }
}
