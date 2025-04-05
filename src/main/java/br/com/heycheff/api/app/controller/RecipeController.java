package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.RecipeRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.app.usecase.RecipeUseCase;
import br.com.heycheff.api.util.constants.ValidationMessages;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/receitas")
public class RecipeController {
    final RecipeUseCase useCase;

    public RecipeController(RecipeUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<PageResponse<RecipeFeed>> loadFeed(@RequestParam Integer pageNum,
                                                             @RequestParam Integer pageSize,
                                                             @RequestParam(required = false)
                                                             String userId) {
        var pageRequest = PageRequest.of(pageNum, pageSize);
        if (Objects.isNull(userId))
            return ResponseEntity.ok(useCase.loadFeed(pageRequest));

        return ResponseEntity.ok(useCase.loadUserContent(pageRequest, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FullRecipeResponse>> getAll() {
        return ResponseEntity.ok(useCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeModal> loadModal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(useCase.loadModal(id));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/next-step")
    public ResponseEntity<RecipeNextStep> nextStep(@PathVariable Long id) {
        return ResponseEntity.ok(useCase.nextStep(id));
    }

    @PostMapping
    public ResponseEntity<RecipeId> include(@NotBlank(message = ValidationMessages.RECIPE_TITLE)
                                            String titulo,
                                            @NotNull(message = ValidationMessages.RECIPE_TAGS)
                                            String tags,
                                            @NotNull(message = ValidationMessages.USER_ID)
                                            String userId,
                                            @NotNull(message = ValidationMessages.RECIPE_THUMB)
                                            MultipartFile thumb) {
        Type listOfTags = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();
        var receita = new RecipeRequest(titulo, userId, new Gson().fromJson(tags, listOfTags));
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.save(receita, thumb));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody RecipeStatus status,
                             @PathVariable Long id) {
        useCase.updateStatus(status, id);
    }
}
