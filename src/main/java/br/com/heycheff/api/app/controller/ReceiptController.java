package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.app.usecase.ReceiptUseCase;
import br.com.heycheff.api.util.constants.ValidationMessages;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
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
import java.util.Objects;

@RestController
@RequestMapping("/receitas")
public class ReceiptController {
    final ReceiptUseCase useCase;

    public ReceiptController(ReceiptUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReceiptFeed>> loadFeed(@RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam(required = false)
                                                              String userId) {
        var pageRequest = PageRequest.of(pageNum, pageSize);
        if (Objects.isNull(userId))
            return ResponseEntity.ok(useCase.loadFeed(pageRequest));

        return ResponseEntity.ok(useCase.loadUserContent(pageRequest, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptModal> loadModal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(useCase.loadModal(id));
        } catch (ReceiptNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/next-step")
    public ResponseEntity<ReceiptNextStep> nextStep(@PathVariable Long id) {
        return ResponseEntity.ok(useCase.nextStep(id));
    }

    @PostMapping
    public ResponseEntity<ReceiptId> include(@NotBlank(message = ValidationMessages.RECEIPT_TITLE)
                                             String titulo,
                                             @NotNull(message = ValidationMessages.RECEIPT_TAGS)
                                             String tags,
                                             @NotNull(message = ValidationMessages.USER_ID)
                                             String userId,
                                             @NotNull(message = ValidationMessages.RECEIPT_THUMB)
                                             MultipartFile thumb) {
        Type listOfTags = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();
        var receita = new ReceiptRequest(titulo, userId, new Gson().fromJson(tags, listOfTags));
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.save(receita, thumb));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody ReceiptStatus status,
                             @PathVariable Long id) {
        useCase.updateStatus(status, id);
    }
}
