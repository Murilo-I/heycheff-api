package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.data.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RecipeDataUseCase {

    void persist(Recipe recipe);

    List<Recipe> findAll();

    Page<Recipe> findByStatus(boolean status, PageRequest pageRequest);

    Page<Recipe> findByUserId(String userId, PageRequest pageRequest);

    Recipe validateRecipe(Long recipeId);
}
