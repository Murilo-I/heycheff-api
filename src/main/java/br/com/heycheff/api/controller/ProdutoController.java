package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.ProdutoDescDTO;
import br.com.heycheff.api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<ProdutoDescDTO>> listAll() {
        return ResponseEntity.ok(service.listaProdutos());
    }
}
