package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ReceitaModal {

    private List<Tag> tags;
    private List<StepDTO> steps;
}
