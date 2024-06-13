package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.MeasureUnitDTO;
import br.com.heycheff.api.app.dto.ProductDescDTO;
import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Set<ProductDescDTO> listProducts() {
        return repository.findAll().stream().map(TypeMapper::fromProductDescription)
                .collect(Collectors.toSet());
    }

    public List<MeasureUnitDTO> listMeasureUnits() {
        return Arrays.stream(MeasureUnit.values()).map(TypeMapper::fromMeasureUnit).toList();
    }
}
