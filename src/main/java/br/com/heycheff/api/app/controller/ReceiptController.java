package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.dto.request.ReceiptRequest;
import br.com.heycheff.api.app.dto.response.*;
import br.com.heycheff.api.app.service.ReceiptService;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    final ReceiptService service;

    public ReceiptController(ReceiptService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReceiptFeed>> loadFeed(@RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam(required = false)
                                                              String userId) {
        var pageRequest = PageRequest.of(pageNum, pageSize);
        if (Objects.isNull(userId))
            return ResponseEntity.ok(service.loadFeed(pageRequest));

        return ResponseEntity.ok(service.loadUserContent(pageRequest, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptModal> loadModal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.loadModal(id));
        } catch (ReceiptNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/next-step")
    public ResponseEntity<ReceiptNextStep> nextStep(@PathVariable Long id) {
        return ResponseEntity.ok(service.nextStep(id));
    }

    @PostMapping
    public ResponseEntity<ReceiptId> include(String titulo, String tags, MultipartFile thumb) {
        Type listOfTags = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();
        var receita = new ReceiptRequest(titulo, new Gson().fromJson(tags, listOfTags));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(receita, thumb));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody ReceiptStatus status,
                             @PathVariable Long id) {
        service.updateStatus(status, id);
    }
}
