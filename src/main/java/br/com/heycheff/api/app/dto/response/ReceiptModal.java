package br.com.heycheff.api.app.dto.response;

import br.com.heycheff.api.app.dto.StepDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReceiptModal {

    private List<StepDTO> steps;
}
