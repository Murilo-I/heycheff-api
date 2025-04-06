package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.RecipeRequest;
import br.com.heycheff.api.app.dto.response.RecipeStatus;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.app.usecase.RecipeUseCase;
import br.com.heycheff.api.app.usecase.SequenceGeneratorUseCase;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.Recipe;
import br.com.heycheff.api.data.model.Step;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.heycheff.api.app.service.StepServiceTest.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    public static final String SCRAMBLED_EGGS = "scrambled eggs";
    public static final long ID = 1L;
    static final String THUMB = "thumb";
    static final String USER_ID = "6744ef2d210d581f27826e05";

    RecipeRepository repository = mock(RecipeRepository.class);
    FileUseCase fileUseCase = mock(FileUseCase.class);
    SequenceGeneratorUseCase seqGenUseCase = mock(SequenceGeneratorUseCase.class);
    UserUseCase userUseCase = mock(UserUseCase.class);
    RecipeUseCase recipeUseCase = new RecipeService(repository, fileUseCase, seqGenUseCase, userUseCase);

    @Test
    void loadFeedSuccessfully() {
        when(repository.findByStatus(anyBoolean(), any()))
                .thenReturn(new PageImpl<>(
                                Collections.singletonList(recipe())
                        )
                );

        var feed = recipeUseCase.loadFeed(PageRequest.of(1, 1));

        assertFalse(feed.items().isEmpty());
        assertEquals(SCRAMBLED_EGGS, feed.items().get(0).getTitulo());
    }

    public static Recipe recipe() {
        var recipe = new Recipe(SCRAMBLED_EGGS, USER_ID);
        var steps = new ArrayList<Step>();
        steps.add(step());
        recipe.setSteps(steps);
        recipe.setTags(List.of(1, 2, 3));
        recipe.setThumb(THUMB);
        recipe.setSeqId(ID);
        return recipe;
    }

    @Test
    void loadModalSuccessfully() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(recipe()));

        var modal = recipeUseCase.loadModal(ID);

        assertNotNull(modal);
        assertEquals(step().getPreparationMode(), modal.getSteps().get(0).getModoPreparo());
    }

    @Test
    void throwRecipeNotFoundWhenLoadingModal() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(RecipeNotFoundException.class,
                () -> recipeUseCase.loadModal(ID));

        assertEquals("Receita Not Found!", exception.getMessage());
    }

    @Test
    void saveRecipeSuccessfully() {
        var expected = recipe();
        when(repository.save(any())).thenReturn(expected);
        when(seqGenUseCase.generateSequence(anyString())).thenReturn(ID);
        when(fileUseCase.salvar(any(), anyString())).thenReturn(THUMB);

        var recipe = recipeUseCase.save(request(), multipart());

        assertEquals(expected.getSeqId(), recipe.getSeqId());
    }

    RecipeRequest request() {
        return new RecipeRequest(SCRAMBLED_EGGS, USER_ID,
                Collections.singletonList(new TagDTO(1, "salgado")));
    }

    public static MockMultipartFile multipart() {
        return new MockMultipartFile("multipart", new byte[]{});
    }

    @Test
    void updateRecipeStatusSuccessfully() {
        when(repository.save(any())).thenReturn(recipe());
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(recipe()));

        assertDoesNotThrow(() -> recipeUseCase.updateStatus(status(), ID));
        verify(repository, times(1)).save(any());
    }

    @Test
    void throwRecipeNotFoundWhenUpdatingStatus() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class,
                () -> recipeUseCase.updateStatus(status(), ID));
        verify(repository, times(0)).save(any());
    }

    RecipeStatus status() {
        return new RecipeStatus(true);
    }
}
