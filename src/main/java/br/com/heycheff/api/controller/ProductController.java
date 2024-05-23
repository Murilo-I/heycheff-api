package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.ProductDescDTO;
import br.com.heycheff.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Set<ProductDescDTO>> listAll() {
        return ResponseEntity.ok(service.listProducts());
    }
}
