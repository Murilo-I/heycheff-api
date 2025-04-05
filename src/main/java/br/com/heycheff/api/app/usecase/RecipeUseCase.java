package br.com.heycheff.api.app.usecase;

import br.com.heycheff.api.app.dto.request.RecipeRequest;
import br.com.heycheff.api.app.dto.response.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeUseCase {
    PageResponse<RecipeFeed> loadFeed(PageRequest pageRequest);

    PageResponse<RecipeFeed> loadUserContent(PageRequest pageRequest, String userId);

    RecipeModal loadModal(Long id);

    List<FullRecipeResponse> findAll();

    RecipeId save(RecipeRequest request, MultipartFile thumb);

    void updateStatus(RecipeStatus dto, Long id);

    RecipeNextStep nextStep(Long id);
}
