package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, String> {

    Page<Recipe> findByStatus(boolean status, Pageable pageable);

    Optional<Recipe> findBySeqId(Long id);

    Page<Recipe> findByOwnerId(String userId, Pageable pageable);
}
