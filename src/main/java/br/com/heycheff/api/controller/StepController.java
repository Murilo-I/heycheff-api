package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.ProductDTO;
import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.Step;
import br.com.heycheff.api.service.StepService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;

@RestController
@RequestMapping("/receitas/{id}/steps")
public class StepController {

    final StepService service;

    @Autowired
    public StepController(StepService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Step> save(Integer step, String modoPreparo,
                                     String produtos,
                                     MultipartFile video,
                                     @PathVariable Long id) {
        Type listOfProducts = new TypeToken<ArrayList<ProductDTO>>() {
        }.getType();
        var dto = new StepDTO(null, step, new Gson().fromJson(produtos, listOfProducts), modoPreparo);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto, video, id));
    }

    @DeleteMapping("/{stepId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long stepId,
                       @PathVariable Long id) {
        service.delete(stepId, id);
    }
}
