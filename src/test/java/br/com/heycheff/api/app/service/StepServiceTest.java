package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.ProductDTO;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.app.usecase.StepUseCase;
import br.com.heycheff.api.data.model.MeasureUnit;
import br.com.heycheff.api.data.model.Product;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import br.com.heycheff.api.util.exception.StepNotInRecipeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StepServiceTest {

    static final String PATH = "path";
    static final String DESC = "desc";
    static final String UNID_MEDIDA = MeasureUnit.UNIDADE.getDescription();
    static final float MEDIDA = 3f;
    static final String PREPARE_MODE = "prepare mode";
    static final Integer STEP_NUMBER = 1;

    RecipeRepository recipeRepository = mock(RecipeRepository.class);
    ProductRepository productRepository = mock(ProductRepository.class);
    FileUseCase fileUseCase = mock(FileUseCase.class);
    SequenceGeneratorUseCase sequenceUseCase = mock(SequenceGeneratorUseCase.class);
    StepUseCase stepUseCase = new StepService(
            recipeRepository, productRepository, fileUseCase, sequenceUseCase
    );

    @Test
    void saveStepSuccessfully() {
        when(recipeRepository.findBySeqId(anyLong())).thenReturn(Optional.of(RecipeServiceTest.recipe()));
        when(sequenceUseCase.generateSequence(anyString())).thenReturn(RecipeServiceTest.ID);
        when(productRepository.findByValue(anyString()))
                .thenReturn(Optional.of(new ProductDescriptions(DESC)));
        when(productRepository.save(any())).thenReturn(new Product());
        when(fileUseCase.salvar(any(), anyString())).thenReturn(PATH);
        when(recipeRepository.save(any())).thenReturn(RecipeServiceTest.recipe());

        var step = stepUseCase.save(dto(), RecipeServiceTest.multipart(), RecipeServiceTest.ID);

        Assertions.assertEquals(2, step.getProducts().size());
        Assertions.assertEquals(PREPARE_MODE, step.getPreparationMode());
        Assertions.assertEquals(PATH, step.getPath());
    }

    StepDTO dto() {
        return new StepDTO(PATH, STEP_NUMBER, List.of(
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA),
                new ProductDTO(DESC, UNID_MEDIDA, MEDIDA)
        ), PREPARE_MODE, 15);
    }

    @Test
    void throwReceiptNotFoundExceptionWhenSavingStep() {
        when(recipeRepository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> stepUseCase.save(dto(), RecipeServiceTest.multipart(), RecipeServiceTest.ID));
    }

    @Test
    void deleteStepSuccessfully() {
        when(recipeRepository.findBySeqId(anyLong())).thenReturn(Optional.of(RecipeServiceTest.recipe()));
        when(recipeRepository.save(any())).thenReturn(RecipeServiceTest.recipe());
        doNothing().when(fileUseCase).delete(anyString());
        assertDoesNotThrow(() -> stepUseCase.delete(STEP_NUMBER, RecipeServiceTest.ID));
    }

    @Test
    void throwReceiptNotFoundExceptionWhenDeletingStep() {
        when(recipeRepository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> stepUseCase.delete(STEP_NUMBER, RecipeServiceTest.ID));
    }

    @Test
    void throwStepNotInReceiptExceptionWhenDeletingStep() {
        when(recipeRepository.findBySeqId(anyLong())).thenReturn(Optional.of(RecipeServiceTest.recipe()));

        var exception = assertThrows(StepNotInRecipeException.class,
                () -> stepUseCase.delete(2, RecipeServiceTest.ID));

        assertEquals("O Step de ID: 2 não existe para a receita de ID: 1", exception.getMessage());
    }

    public static Step step() {
        var step = new Step(1L, STEP_NUMBER, RecipeServiceTest.SCRAMBLED_EGGS, 15);
        step.setProducts(List.of(
                new Product("ovo", UNID_MEDIDA, MEDIDA),
                new Product("sal", MeasureUnit.GRAMA.getDescription(), .5f)
        ));
        step.setPath(PATH);
        return step;
    }
}
