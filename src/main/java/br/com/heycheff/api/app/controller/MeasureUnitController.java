package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.MeasureUnitDTO;
import br.com.heycheff.api.app.usecase.ProductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos/{id}/medidas")
public class MeasureUnitController {

    final ProductUseCase useCase;

    public MeasureUnitController(ProductUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<List<MeasureUnitDTO>> list(@PathVariable(name = "id") Integer produtoId) {
        if (produtoId == 0)
            return ResponseEntity.ok(useCase.listMeasureUnits());
        else
            return ResponseEntity.notFound().build();
    }
}
