package br.com.heycheff.api.app.service;

import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    @Test
    void returnProductDescriptionsSuccessfully() {
        var sal = "sal";
        when(repository.findAll())
                .thenReturn(Collections.singletonList(new ProductDescriptions(sal)));

        var prods = service.listProducts();
        assertFalse(prods.isEmpty());
        assertEquals(sal, prods.stream().findFirst().get().getProdutoDesc());
    }

    @Test
    void returnAllMeasureUnitsSuccessfully() {
        var units = service.listMeasureUnits();

        assertEquals(9, units.size());
        assertEquals(MeasureUnit.UNIDADE.getDescription(), units.get(0).getDescricao());
    }
}
