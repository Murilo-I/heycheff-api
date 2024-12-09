package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.ProductDescDTO;
import br.com.heycheff.api.app.usecase.ProductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    final ProductUseCase useCase;

    public ProductController(ProductUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<Set<ProductDescDTO>> listAll() {
        return ResponseEntity.ok(useCase.listProducts());
    }
}
