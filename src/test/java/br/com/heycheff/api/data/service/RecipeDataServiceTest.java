package br.com.heycheff.api.data.service;

import br.com.heycheff.api.app.usecase.RecipeDataUseCase;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.data.helper.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeDataServiceTest {

    RecipeRepository repository = mock(RecipeRepository.class);
    RecipeDataUseCase useCase = new RecipeDataService(repository);

    @Test
    void validateMethods() {
        when(repository.save(any())).thenReturn(recipe());
        when(repository.findAll()).thenReturn(List.of(recipe()));
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(recipe()));
        when(repository.findByOwnerId(anyString(), any())).thenReturn(Page.empty());
        when(repository.findByStatus(anyBoolean(), any())).thenReturn(Page.empty());

        assertAll(
                () -> useCase.findAll(),
                () -> useCase.persist(recipe()),
                () -> useCase.validateRecipe(ID),
                () -> useCase.findByUserId(USER_ID, pageRequest()),
                () -> useCase.findByStatus(Boolean.TRUE, pageRequest())
        );
    }

    @Test
    void shouldThrowRecipeNotFoundExceptionWhenValidating() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> useCase.validateRecipe(ID));
    }
}
