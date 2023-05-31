package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.ProdutoDTO;
import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.ReceitaStep;
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

    @Autowired
    private StepService service;

    @PostMapping
    public ResponseEntity<ReceitaStep> incluir(Integer step, String modoPreparo,
                                               String produtos,
                                               MultipartFile video,
                                               @PathVariable Integer id) {
        Type listOfProdutos = new TypeToken<ArrayList<ProdutoDTO>>() {}.getType();
        var dto = new StepDTO(null, step, new Gson().fromJson(produtos, listOfProdutos), modoPreparo);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.incluir(dto, video, id));
    }

    @DeleteMapping("/{stepId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer stepId,
                        @PathVariable Integer id) {
        service.deletar(stepId, id);
    }
}
