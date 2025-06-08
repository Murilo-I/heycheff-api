package br.com.heycheff.api.data.service;

import br.com.heycheff.api.app.usecase.RecipeDataUseCase;
import br.com.heycheff.api.data.model.Recipe;
import br.com.heycheff.api.data.repository.RecipeRepository;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeDataService implements RecipeDataUseCase {

    final RecipeRepository repository;

    public RecipeDataService(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void persist(Recipe recipe) {
        repository.save(recipe);
    }

    @Override
    public List<Recipe> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Recipe> findByStatus(boolean status, PageRequest pageRequest) {
        return repository.findByStatus(status, pageRequest);
    }

    @Override
    public Page<Recipe> findByUserId(String userId, PageRequest pageRequest) {
        return repository.findByOwnerId(userId, pageRequest);
    }

    @Override
    public Recipe validateRecipe(Long recipeId) {
        return repository.findBySeqId(recipeId).orElseThrow(RecipeNotFoundException::new);
    }
}
