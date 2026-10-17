package com.kelvinoliveira.marketplace.domain.controller;

import com.kelvinoliveira.marketplace.domain.dto.PropostaRequest;
import com.kelvinoliveira.marketplace.domain.dto.PropostaResponse;
import com.kelvinoliveira.marketplace.domain.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaService service;

    public PropostaController(PropostaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> enviar(@Valid @RequestBody PropostaRequest request) {
        PropostaResponse resposta = service.enviarProposta(request);
        return ResponseEntity.created(URI.create("/propostas/" + resposta.id())).body(resposta);
    }

    @PostMapping("/{id}/aceitar")
    public ResponseEntity<PropostaResponse> aceitar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aceitarProposta(id));
    }

    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<PropostaResponse> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeitarProposta(id));
    }

    @GetMapping("/anuncios/{anuncioId}")
    public ResponseEntity<List<PropostaResponse>> listarPorAnuncio(@PathVariable Long anuncioId) {
        return ResponseEntity.ok(service.listarPorAnuncio(anuncioId));
    }
}
