package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.UnidadeMedidaDTO;
import br.com.heycheff.api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos/{id}/medidas")
public class UnidadeMedidaController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<UnidadeMedidaDTO>> list(@PathVariable(name = "id") Integer produtoId) {
        if (produtoId == 0)
            return ResponseEntity.ok(service.listaUnidMedidas());
        else
            return ResponseEntity.notFound().build();
    }
}
