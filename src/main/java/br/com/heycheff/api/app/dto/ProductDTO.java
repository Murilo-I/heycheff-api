package br.com.heycheff.api.app.dto;

import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.model.Product;
import br.com.heycheff.api.util.exception.MeasureUnitNotFoundException;
import br.com.heycheff.api.util.mapper.EntityMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements EntityMapper<Product> {

    private String desc;
    private String unidMedida;
    private Float medida;

    @Override
    public Product toEntity() {
        MeasureUnit measureUnit = Arrays.stream(MeasureUnit.values())
                .filter(measure -> measure.getDescription().equals(this.unidMedida))
                .findFirst().orElseThrow(MeasureUnitNotFoundException::new);
        return new Product(this.desc, measureUnit.getDescription(), this.medida);
    }
}
