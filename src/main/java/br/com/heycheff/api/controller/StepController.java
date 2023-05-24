package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.StepDTO;
import br.com.heycheff.api.model.ReceitaStep;
import br.com.heycheff.api.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/receitas/{id}/steps")
public class StepController {

    @Autowired
    private StepService service;

    @PostMapping
    public ResponseEntity<ReceitaStep> incluir(StepDTO step, MultipartFile video,
                                               @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.incluir(step, video, id));
    }

    @DeleteMapping("/{stepId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer stepId,
                        @PathVariable Integer id) {
        service.deletar(stepId, id);
    }
}
