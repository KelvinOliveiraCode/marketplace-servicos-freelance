package com.kelvinoliveira.marketplace.domain.controller;

import com.kelvinoliveira.marketplace.domain.dto.AnuncioRequest;
import com.kelvinoliveira.marketplace.domain.dto.AnuncioResponse;
import com.kelvinoliveira.marketplace.domain.service.AnuncioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    private final AnuncioService service;

    public AnuncioController(AnuncioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AnuncioResponse> publicar(@Valid @RequestBody AnuncioRequest request) {
        AnuncioResponse resposta = service.publicar(request);
        return ResponseEntity.created(URI.create("/anuncios/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<AnuncioResponse>> listarDisponiveis() {
        return ResponseEntity.ok(service.listarDisponiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
