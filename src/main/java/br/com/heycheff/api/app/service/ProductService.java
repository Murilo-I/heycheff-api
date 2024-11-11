package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.MeasureUnitDTO;
import br.com.heycheff.api.app.dto.ProductDescDTO;
import br.com.heycheff.api.app.usecase.ProductUseCase;
import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.util.constants.CacheNames;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductUseCase {

    final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = CacheNames.PRODUCTS)
    public Set<ProductDescDTO> listProducts() {
        return repository.findAll().stream().map(TypeMapper::fromProductDescription)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheNames.MEASURE_UNIT)
    public List<MeasureUnitDTO> listMeasureUnits() {
        return Arrays.stream(MeasureUnit.values()).map(TypeMapper::fromMeasureUnit).toList();
    }
}
