package br.com.heycheff.api.app.dto.response;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReceiptModal {

    private List<TagDTO> tags;
    private List<StepDTO> steps;
}
