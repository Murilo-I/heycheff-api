package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.usecase.TagUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tags")
public class TagController {

    final TagUseCase useCase;

    public TagController(TagUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> list(@RequestParam(required = false) Long recipeId) {
        if (Objects.isNull(recipeId)) return ResponseEntity.ok(useCase.listAll());
        else return ResponseEntity.ok(useCase.findByRecipeId(recipeId));
    }
}
