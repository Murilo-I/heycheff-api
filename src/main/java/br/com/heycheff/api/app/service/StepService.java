package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.ProductDTO;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.app.usecase.StepUseCase;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.model.Recipe;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInRecipeException;
import br.com.heycheff.api.util.mapper.TypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;

@Service
@Transactional
public class StepService implements StepUseCase {
    private static final String STEP_NOT_IN_RECIPE_MESSAGE =
            "O Step de ID: %d não existe para a receita de ID: %d";

    final RecipeRepository recipeRepository;
    final ProductRepository productRepository;
    final FileUseCase fileUseCase;
    final SequenceGeneratorUseCase sequenceUseCase;

    public StepService(RecipeRepository recipeRepository, ProductRepository productRepository,
                       FileUseCase fileUseCase, SequenceGeneratorUseCase sequenceUseCase) {
        this.recipeRepository = recipeRepository;
        this.productRepository = productRepository;
        this.fileUseCase = fileUseCase;
        this.sequenceUseCase = sequenceUseCase;
    }

    @Override
    public StepDTO getStep(Integer stepNumber, Long recipeId) {
        var recipe = validateRecipe(recipeId);
        var step = validateStep(stepNumber, recipe);
        return TypeMapper.fromStepEntity(step, fileUseCase.resolve(step.getPath()));
    }

    @Override
    public Step save(StepDTO step, MultipartFile video, Long recipeId) {
        var recipe = validateRecipe(recipeId);
        var savedStep = new Step(sequenceUseCase.generateSequence(Step.STEP_SEQUENCE),
                step.getStepNumber(), step.getModoPreparo(), step.getTimeMinutes());

        setProducts(step, savedStep);
        savedStep.setPath(fileUseCase.salvar(video,
                "receitaStep_" + recipeId + "_" + savedStep.getStepNumber()));

        recipe.getSteps().add(savedStep);
        recipeRepository.save(recipe);

        return savedStep;
    }

    @Override
    public Step delete(Integer stepNumber, Long recipeId) {
        var recipe = validateRecipe(recipeId);
        var delStep = validateStep(stepNumber, recipe);

        fileUseCase.delete(delStep.getPath());
        recipe.getSteps().remove(delStep);
        recipeRepository.save(recipe);

        return delStep;
    }

    @Override
    public Step update(StepDTO step, MultipartFile video, Integer stepNumber, Long recipeId) {
        var updStep = delete(stepNumber, recipeId);
        var recipe = validateRecipe(recipeId);
        var steps = recipe.getSteps();

        setProducts(step, updStep);
        updStep.setStepNumber(step.getStepNumber());
        updStep.setPreparationMode(step.getModoPreparo());
        updStep.setPath(fileUseCase.salvar(video,
                "receitaStep_" + recipeId + "_" + updStep.getStepNumber()));

        steps.add(updStep);
        recipe.setSteps(steps.stream().sorted(Comparator
                .comparing(Step::getStepNumber)).toList());
        recipeRepository.save(recipe);
        return updStep;
    }

    private Recipe validateRecipe(Long recipeId) {
        return recipeRepository.findBySeqId(recipeId).orElseThrow(RecipeNotFoundException::new);
    }

    private Step validateStep(Integer stepNumber, Recipe recipe) {
        return recipe.getSteps().stream().filter(step -> step.getStepNumber().equals(stepNumber))
                .findFirst().orElseThrow(() -> new StepNotInRecipeException(
                        String.format(STEP_NOT_IN_RECIPE_MESSAGE, stepNumber, recipe.getSeqId())
                ));
    }

    private void setProducts(StepDTO stepDto, Step stepEntity) {
        stepEntity.setProducts(stepDto.getProdutos().stream().map(ProductDTO::toEntity).toList());
        stepDto.getProdutos().forEach(product -> {
            if (productRepository.findByValue(product.getDesc()).isEmpty())
                productRepository.save(new ProductDescriptions(product.getDesc()));
        });
    }
}
