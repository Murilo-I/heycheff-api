package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.request.StepRequest;
import br.com.heycheff.api.app.usecase.StepUseCase;
import br.com.heycheff.api.data.model.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.heycheff.api.util.mapper.TypeMapper.fromStepRequest;

@RestController
@RequestMapping("/receitas/{id}/steps")
public class StepController {

    final StepUseCase useCase;

    public StepController(StepUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{stepNumber}")
    public ResponseEntity<StepDTO> getStep(@PathVariable Integer stepNumber, @PathVariable Long id) {
        return ResponseEntity.ok(useCase.getStep(stepNumber, id));
    }

    @PostMapping
    public ResponseEntity<Step> save(StepRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(useCase.save(fromStepRequest(request), request.getVideo(), id));
    }

    @DeleteMapping("/{stepNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer stepNumber, @PathVariable Long id) {
        useCase.delete(stepNumber, id);
    }

    @PatchMapping("/{stepNumber}")
    public ResponseEntity<Step> update(StepRequest request, @PathVariable Integer stepNumber,
                                       @PathVariable Long id) {
        return ResponseEntity.ok(useCase.update(fromStepRequest(request),
                request.getVideo(), stepNumber, id));
    }
}
