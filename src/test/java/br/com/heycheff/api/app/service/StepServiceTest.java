package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.RecipeDataUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.app.usecase.StepUseCase;
import br.com.heycheff.api.data.model.ProductDescriptions;
import br.com.heycheff.api.data.repository.ProductRepository;
import br.com.heycheff.api.util.exception.StepNotInRecipeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static br.com.heycheff.api.data.helper.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StepServiceTest {

    RecipeDataUseCase recipeData = mock(RecipeDataUseCase.class);
    ProductRepository productRepository = mock(ProductRepository.class);
    FileUseCase fileUseCase = mock(FileUseCase.class);
    SequenceGeneratorUseCase sequenceUseCase = mock(SequenceGeneratorUseCase.class);
    StepUseCase stepUseCase = new StepService(
            recipeData, productRepository, fileUseCase, sequenceUseCase
    );

    @BeforeEach
    void validateRecipe() {
        when(recipeData.validateRecipe(anyLong())).thenReturn(recipe());
        doNothing().when(recipeData).persist(any());
    }

    @Test
    void saveStepSuccessfully() {
        when(sequenceUseCase.generateSequence(anyString())).thenReturn(ID);
        when(productRepository.findByValue(anyString()))
                .thenReturn(Optional.of(new ProductDescriptions(DESC)));
        when(fileUseCase.salvar(any(), anyString())).thenReturn(PATH);

        var step = stepUseCase.save(dto(), multipart("step"), ID);

        Assertions.assertEquals(2, step.getProducts().size());
        Assertions.assertEquals(PREPARE_MODE, step.getPreparationMode());
        Assertions.assertEquals(PATH, step.getPath());
    }

    @Test
    void deleteStepSuccessfully() {
        doNothing().when(fileUseCase).delete(anyString());
        assertDoesNotThrow(() -> stepUseCase.delete(STEP_NUMBER, ID));
    }

    @Test
    void throwStepNotInRecipeExceptionWhenDeletingStep() {
        var exception = assertThrows(StepNotInRecipeException.class,
                () -> stepUseCase.delete(3, ID));

        assertEquals("O Step de ID: 3 não existe para a receita de ID: 1", exception.getMessage());
    }

    @Test
    void getStep() {
        when(fileUseCase.resolve(anyString())).thenReturn(PATH);

        var step = stepUseCase.getStep(1, 1L);

        assertNotNull(step);
    }

    @Test
    void updateStepSuccessfully() {
        when(productRepository.save(any()))
                .thenReturn(new ProductDescriptions(DESC));
        when(fileUseCase.salvar(any(), anyString())).thenReturn(PATH);

        var updated = stepUseCase.update(dto(), multipart("step"), 2, ID);

        assertEquals(DESC, updated.getProducts().get(0).getDescription());
    }
}
