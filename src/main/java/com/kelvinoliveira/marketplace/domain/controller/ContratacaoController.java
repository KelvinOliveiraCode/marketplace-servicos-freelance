package com.kelvinoliveira.marketplace.domain.controller;

import com.kelvinoliveira.marketplace.domain.dto.ContratacaoRequest;
import com.kelvinoliveira.marketplace.domain.dto.ContratacaoResponse;
import com.kelvinoliveira.marketplace.domain.service.ContratacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/contratacoes")
public class ContratacaoController {

    private final ContratacaoService service;

    public ContratacaoController(ContratacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ContratacaoResponse> contratar(@Valid @RequestBody ContratacaoRequest request) {
        ContratacaoResponse resposta = service.contratar(request);
        return ResponseEntity.created(URI.create("/contratacoes/" + resposta.id())).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
