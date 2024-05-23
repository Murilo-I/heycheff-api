package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.*;
import br.com.heycheff.api.model.Receipt;
import br.com.heycheff.api.service.ReceiptService;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceiptController {
    final ReceiptService service;

    @Autowired
    public ReceiptController(ReceiptService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReceiptFeed>> loadFeed() {
        return ResponseEntity.ok(service.loadFeed());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaModal> loadModal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.loadModal(id));
        } catch (ReceiptNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Receipt> include(String titulo, String tags, MultipartFile thumb) {
        Type listOfTags = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();
        var receita = new ReceitaRequest(titulo, new Gson().fromJson(tags, listOfTags));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(receita, thumb));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody ReceitaStatusDTO status,
                             @PathVariable Long id) {
        service.updateStatus(status, id);
    }
}
