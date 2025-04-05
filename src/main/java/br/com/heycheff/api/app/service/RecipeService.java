package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.RecipeRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.RecipeUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.data.model.Recipe;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.constants.CacheNames;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromRecipeEntity;
import static br.com.heycheff.api.util.mapper.TypeMapper.fromStepEntity;

@Service
public class RecipeService implements RecipeUseCase {

    final RecipeRepository recipeRepository;
    final FileUseCase fileUseCase;
    final SequenceGeneratorUseCase sequenceUseCase;

    public RecipeService(RecipeRepository recipeRepository, FileUseCase fileUseCase,
                         SequenceGeneratorUseCase sequenceUseCase) {
        this.recipeRepository = recipeRepository;
        this.fileUseCase = fileUseCase;
        this.sequenceUseCase = sequenceUseCase;
    }

    @Override
    @Cacheable(value = CacheNames.FEED)
    public PageResponse<RecipeFeed> loadFeed(PageRequest pageRequest) {
        Page<Recipe> recipes = recipeRepository.findByStatus(true, pageRequest);
        var recipeFeed = mapRecipes(recipes);
        return new PageResponse<>(recipeFeed, recipes.getTotalElements());
    }

    @Override
    public PageResponse<RecipeFeed> loadUserContent(PageRequest pageRequest, String userId) {
        Page<Recipe> recipes = recipeRepository.findByOwnerId(userId, pageRequest);
        var userRecipes = mapRecipes(recipes);
        return new PageResponse<>(userRecipes, recipes.getTotalElements());
    }

    private List<RecipeFeed> mapRecipes(Page<Recipe> recipes) {
        return recipes.map(recipe -> fromRecipeEntity(
                        recipe, fileUseCase.resolve(recipe.getThumb())
                )
        ).getContent();
    }

    @Override
    public RecipeModal loadModal(Long id) {
        var recipe = validateRecipe(id);
        List<StepDTO> steps = new ArrayList<>();

        recipe.getSteps().forEach(step -> steps.add(
                fromStepEntity(step, fileUseCase.resolve(step.getPath()))
        ));

        RecipeModal modal = new RecipeModal();
        modal.setUserId(recipe.getOwnerId());
        modal.setSteps(steps);

        return modal;
    }

    @Override
    public List<FullRecipeResponse> findAll() {
        var fullResponse = new ArrayList<FullRecipeResponse>();
        recipeRepository.findAll().forEach(recipe -> {
            var fullRecipe = FullRecipeResponse.builder()
                    .recipeId(recipe.getId())
                    .title(recipe.getTitle())
                    .tags(recipe.getTags().stream()
                            .map(TypeMapper::fromTagId)
                            .toList())
                    .steps(recipe.getSteps().stream()
                            .map(step -> fromStepEntity(step, null))
                            .toList())
                    .build();
            fullResponse.add(fullRecipe);
        });
        return fullResponse;
    }

    @Override
    @Transactional
    public RecipeId save(RecipeRequest request, MultipartFile thumb) {
        Recipe recipe = new Recipe(request.getTitulo(), request.getUserId());
        recipe.setSeqId(sequenceUseCase.generateSequence(Recipe.RECIPE_SEQUENCE));
        recipe.setTags(request.getTags().stream().map(TagDTO::toEntity).toList());
        recipe.setThumb(fileUseCase.salvar(thumb, "thumbReceita" + recipe.getSeqId()));
        recipeRepository.save(recipe);
        return new RecipeId(recipe.getSeqId());
    }

    @Override
    @Transactional
    public void updateStatus(RecipeStatus dto, Long id) {
        var recipe = validateRecipe(id);
        recipe.setStatus(dto.getStatus());
        recipeRepository.save(recipe);
    }

    @Override
    public RecipeNextStep nextStep(Long id) {
        var recipe = validateRecipe(id);
        var steps = recipe.getSteps();
        var nextStep = steps.stream().map(Step::getStepNumber)
                .max(Integer::compareTo).orElse(0);

        return new RecipeNextStep(steps.stream().map(step -> fromStepEntity(
                        step, fileUseCase.resolve(step.getPath())
                ))
                .toList(), nextStep + 1);
    }

    private Recipe validateRecipe(Long recipeId) {
        return recipeRepository.findBySeqId(recipeId).orElseThrow(RecipeNotFoundException::new);
    }
}
