package br.com.heycheff.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDTO {

    private String path;
    private Integer stepNumber;
    private List<ProductDTO> produtos;
    private String modoPreparo;
}
