package br.com.heycheff.api.util.mapper;

import br.com.heycheff.api.dto.ProductDTO;
import br.com.heycheff.api.dto.ProductDescDTO;
import br.com.heycheff.api.dto.TagDTO;
import br.com.heycheff.api.dto.UnidadeMedidaDTO;
import br.com.heycheff.api.model.MeasureUnit;
import br.com.heycheff.api.model.Product;
import br.com.heycheff.api.model.ProductDescriptions;
import br.com.heycheff.api.model.Tags;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TypeMapper {

    public TagDTO fromEntity(Integer tagId) {
        Tags tags = Arrays.stream(Tags.values()).filter(tag -> tag.getId().equals(tagId))
                .findFirst().orElseThrow(TagNotFoundException::new);
        return new TagDTO(tags.getId(), tags.getTag());
    }

    public ProductDTO fromEntity(Product product) {
        return new ProductDTO(product.getDescription(),
                product.getMeasureUnit(),
                product.getQuantity());
    }

    public ProductDescDTO fromEntity(ProductDescriptions product) {
        return new ProductDescDTO(product.getValue());
    }

    public UnidadeMedidaDTO fromEntity(MeasureUnit measureUnit) {
        return new UnidadeMedidaDTO(measureUnit.getDescription());
    }
}
