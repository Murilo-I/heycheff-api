package br.com.heycheff.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class StepDTO {

    private String path;
    private Integer step;
    private List<ProdutoDTO> produtos;
    private String modoPreparo;
}
