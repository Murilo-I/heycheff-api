package br.com.heycheff.api.util.mapper;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.*;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public interface TypeMapper {

    static TagDTO fromTagId(Integer tagId) {
        Tags tags = Arrays.stream(Tags.values()).filter(tag -> tag.getId().equals(tagId))
                .findFirst().orElseThrow(TagNotFoundException::new);
        return new TagDTO(tags.getId(), tags.getTag());
    }

    static ProductDTO fromProduct(Product product) {
        return new ProductDTO(product.getDescription(),
                product.getMeasureUnit(),
                product.getQuantity());
    }

    static ProductDescDTO fromProductDescription(ProductDescriptions product) {
        return new ProductDescDTO(product.getValue());
    }

    static UnidadeMedidaDTO fromMeasureUnit(MeasureUnit measureUnit) {
        return new UnidadeMedidaDTO(measureUnit.getDescription());
    }

    static StepDTO fromStepRequest(StepRequest request) {
        Type listOfProducts = new TypeToken<ArrayList<ProductDTO>>() {
        }.getType();

        return new StepDTO(null, request.getStepNumber(), new Gson()
                .fromJson(request.getProdutos(), listOfProducts), request.getModoPreparo());
    }

    static StepDTO fromStepEntity(Step step, String path) {
        return new StepDTO(path, step.getStepNumber(), step.getProducts().stream()
                .map(TypeMapper::fromProduct).toList(), step.getPreparationMode());
    }
}
