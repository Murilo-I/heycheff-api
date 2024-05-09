package br.com.heycheff.api.service;

import br.com.heycheff.api.model.MeasureUnit;
import br.com.heycheff.api.model.ProductDescriptions;
import br.com.heycheff.api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService service;
    @MockBean
    ProductRepository repository;

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
