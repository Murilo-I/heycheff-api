package br.com.heycheff.api.app.dto.response;

import br.com.heycheff.api.app.dto.StepDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptNextStep {

    private List<StepDTO> steps;
    private Integer nextStep;
}
