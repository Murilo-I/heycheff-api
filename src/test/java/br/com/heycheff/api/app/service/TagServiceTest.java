package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.TagUseCase;
import br.com.heycheff.api.data.model.Tags;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static br.com.heycheff.api.data.helper.DataHelper.ID;
import static br.com.heycheff.api.data.helper.DataHelper.recipe;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagServiceTest {

    RecipeRepository repository = mock(RecipeRepository.class);
    TagUseCase useCase = new TagService(repository);

    @Test
    void listAllTags() {
        var tags = useCase.listAll();
        assertEquals(17, tags.size());
        assertEquals(Tags.GREGA.getTag(), tags.get(14).getTag());
    }

    @Test
    void listTagsByRecipe() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.of(recipe()));

        var tags = useCase.findByRecipeId(ID);

        assertEquals(3, tags.size());
        assertEquals(Tags.VEGANO.getTag(), tags.get(2).getTag());
    }

    @Test
    void throwRecipeNotFoundWhenListingTagsByRecipe() {
        when(repository.findBySeqId(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> useCase.findByRecipeId(ID));
    }
}
