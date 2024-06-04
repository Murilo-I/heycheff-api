package br.com.heycheff.api.service;

import br.com.heycheff.api.dto.ProductDescDTO;
import br.com.heycheff.api.dto.UnidadeMedidaDTO;
import br.com.heycheff.api.model.MeasureUnit;
import br.com.heycheff.api.repository.ProductRepository;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Set<ProductDescDTO> listProducts() {
        return repository.findAll().stream().map(TypeMapper::fromProductDescription)
                .collect(Collectors.toSet());
    }

    public List<UnidadeMedidaDTO> listMeasureUnits() {
        return Arrays.stream(MeasureUnit.values()).map(TypeMapper::fromMeasureUnit).toList();
    }
}
