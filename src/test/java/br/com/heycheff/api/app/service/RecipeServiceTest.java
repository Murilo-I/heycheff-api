package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.response.RecipeStatus;
import br.com.heycheff.api.app.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static br.com.heycheff.api.data.helper.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    RecipeDataUseCase repository = mock(RecipeDataUseCase.class);
    FileUseCase fileUseCase = mock(FileUseCase.class);
    SequenceGeneratorUseCase seqGenUseCase = mock(SequenceGeneratorUseCase.class);
    UserUseCase userUseCase = mock(UserUseCase.class);
    RecipeUseCase recipeUseCase = new RecipeService(repository, fileUseCase, seqGenUseCase, userUseCase);

    @BeforeEach
    void validateRecipe() {
        when(repository.validateRecipe(anyLong())).thenReturn(recipe());
    }

    @Test
    void loadFeedSuccessfully() {
        when(repository.findByStatus(anyBoolean(), any()))
                .thenReturn(new PageImpl<>(
                                Collections.singletonList(recipe())
                        )
                );

        var feed = recipeUseCase.loadFeed(pageRequest());

        assertFalse(feed.items().isEmpty());
        assertEquals(SCRAMBLED_EGGS, feed.items().get(0).getTitulo());
    }

    @Test
    void loadModalSuccessfully() {
        var modal = recipeUseCase.loadModal(ID);

        assertNotNull(modal);
        assertEquals(step().getPreparationMode(), modal.getSteps().get(0).getModoPreparo());
    }

    @Test
    void saveRecipeSuccessfully() {
        var expected = recipe();
        doNothing().when(repository).persist(any());
        when(seqGenUseCase.generateSequence(anyString())).thenReturn(ID);
        when(fileUseCase.salvar(any(), anyString())).thenReturn(THUMB);

        var recipe = recipeUseCase.save(request(), multipart("recipe"));

        assertEquals(expected.getSeqId(), recipe.getSeqId());
    }

    @Test
    void updateRecipeStatusSuccessfully() {
        doNothing().when(repository).persist(any());

        assertDoesNotThrow(() -> recipeUseCase.updateStatus(status(), ID));
        verify(repository, times(1)).persist(any());
    }

    RecipeStatus status() {
        return new RecipeStatus(true);
    }

    @Test
    void loadUserContentSuccessfully() {
        when(repository.findByUserId(anyString(), any()))
                .thenReturn(new PageImpl<>(
                                Collections.singletonList(recipe())
                        )
                );

        var content = recipeUseCase.loadUserContent(pageRequest(), USER_ID);

        assertFalse(content.items().isEmpty());
        assertEquals(3, content.items().get(0).getTags().size());
    }

    @Test
    void shouldLoadAllRecipes() {
        when(repository.findAll()).thenReturn(List.of(recipe(), recipe(), recipe()));

        var fullResponse = recipeUseCase.findAll();

        assertFalse(fullResponse.isEmpty());
        assertEquals(3, fullResponse.size());
    }

    @Test
    void shouldGetNextRecipeStep() {
        when(fileUseCase.resolve(anyString())).thenReturn(THUMB);

        var next = recipeUseCase.nextStep(1L);

        assertEquals(3, next.getNextStep());
    }

    @Test
    void shouldCallUserAppendWatchedVideo() {
        doNothing().when(userUseCase).appendWatchedVideo(any());

        assertDoesNotThrow(() -> recipeUseCase.markReceiptAsWatched(1L));
    }
}
