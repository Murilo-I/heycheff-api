package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.Receita;
import br.com.heycheff.api.model.ReceitaStep;
import br.com.heycheff.api.service.ReceitaService;
import br.com.heycheff.api.util.exception.ReceitaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {
    @Autowired
    private ReceitaService service;

    @GetMapping
    public ResponseEntity<List<ReceitaFeed>> loadFeed() {
        return ResponseEntity.ok(service.loadFeed());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaModal> loadModal(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.loadModal(id));
        } catch (ReceitaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Receita> incluir(ReceitaRequest receita, MultipartFile thumb) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.incluir(receita, thumb));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Receita> atualizaStatus(@RequestBody ReceitaStatusDTO status,
                                                  @PathVariable Integer id){
        return ResponseEntity.ok(service.atualizaStatus(status, id));
    }
}
