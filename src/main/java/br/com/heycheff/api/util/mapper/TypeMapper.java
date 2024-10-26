package br.com.heycheff.api.util.mapper;

import br.com.heycheff.api.app.dto.*;
import br.com.heycheff.api.app.dto.request.StepRequest;
import br.com.heycheff.api.app.dto.response.ReceiptFeed;
import br.com.heycheff.api.data.model.*;
import br.com.heycheff.api.util.exception.ReceiptEstimatedTimeException;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public interface TypeMapper {

    static UserDTO fromUser(User user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

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

    static MeasureUnitDTO fromMeasureUnit(MeasureUnit measureUnit) {
        return new MeasureUnitDTO(measureUnit.getDescription());
    }

    static StepDTO fromStepRequest(StepRequest request) {
        Type listOfProducts = new TypeToken<ArrayList<ProductDTO>>() {
        }.getType();

        return new StepDTO(
                null, request.getStepNumber(), new Gson()
                .fromJson(request.getProdutos(), listOfProducts),
                request.getModoPreparo(), request.getTimeMinutes()
        );
    }

    static StepDTO fromStepEntity(Step step, String path) {
        return new StepDTO(
                path, step.getStepNumber(), step.getProducts().stream()
                .map(TypeMapper::fromProduct).toList(), step.getPreparationMode(),
                step.getTimeMinutes()
        );
    }

    static ReceiptFeed fromReceiptEntity(Receipt receipt, String thumb) {
        var tags = receipt.getTags().stream().map(TypeMapper::fromTagId).toList();
        int estimatedTime = 0;
        try {
            estimatedTime = receipt.getSteps().stream().map(Step::getTimeMinutes)
                    .reduce(Integer::sum).orElseThrow(ReceiptEstimatedTimeException::new);
        } catch (NullPointerException ignored) {
        }
        return new ReceiptFeed(
                receipt.getSeqId(), thumb, receipt.getTitle(), tags, estimatedTime
        );
    }
}
