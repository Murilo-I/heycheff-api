package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.MeasureUnitDTO;
import br.com.heycheff.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos/{id}/medidas")
public class MeasureUnitController {

    final ProductService service;

    public MeasureUnitController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MeasureUnitDTO>> list(@PathVariable(name = "id") Integer produtoId) {
        if (produtoId == 0)
            return ResponseEntity.ok(service.listMeasureUnits());
        else
            return ResponseEntity.notFound().build();
    }
}
