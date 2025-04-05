package br.com.heycheff.api.util.mapper;

import br.com.heycheff.api.app.dto.*;
import br.com.heycheff.api.app.dto.request.StepRequest;
import br.com.heycheff.api.app.dto.response.RecipeFeed;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.data.model.*;
import br.com.heycheff.api.util.exception.RecipeEstimatedTimeException;
import br.com.heycheff.api.util.exception.TagNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public interface TypeMapper {

    static UserResponse fromUser(User user, Long recipesCount) {
        int followers = 0;
        int following = 0;
        try {
            followers = user.getFollowersIds().size();
            following = user.getFollowingIds().size();
        } catch (NullPointerException ignored) {
        }
        return UserResponse.builder()
                .username(user.getUsername())
                .followers(followers)
                .following(following)
                .followersIds(user.getFollowersIds())
                .recipesCount(recipesCount)
                .build();
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

    static RecipeFeed fromRecipeEntity(Recipe recipe, String thumb) {
        var tags = recipe.getTags().stream().map(TypeMapper::fromTagId).toList();
        int estimatedTime = 0;
        try {
            estimatedTime = recipe.getSteps().stream().map(Step::getTimeMinutes)
                    .reduce(Integer::sum).orElseThrow(RecipeEstimatedTimeException::new);
        } catch (NullPointerException ignored) {
        }
        return new RecipeFeed(
                recipe.getSeqId(), thumb, recipe.getTitle(), tags, estimatedTime
        );
    }
}
