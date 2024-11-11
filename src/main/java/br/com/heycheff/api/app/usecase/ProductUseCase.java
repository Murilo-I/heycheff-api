package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.MeasureUnitDTO;
import br.com.heycheff.api.app.dto.ProductDescDTO;

import java.util.List;
import java.util.Set;

public interface ProductUseCase {
    Set<ProductDescDTO> listProducts();

    List<MeasureUnitDTO> listMeasureUnits();
}
