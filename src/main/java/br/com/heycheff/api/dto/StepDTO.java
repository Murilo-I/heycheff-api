package br.com.heycheff.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StepDTO {

    private String path;
    private Integer step;
    private List<ProdutoDTO> produtos;
    private String modoPreparo;
}
