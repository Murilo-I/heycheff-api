package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.StepProduto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDTO {

    private String desc;
    private String unidMedida;
    private Float medida;

    public static ProdutoDTO fromEntity(StepProduto stepProduto) {
        return new ProdutoDTO(stepProduto.getProduto().getDescricao(),
                stepProduto.getUnidMedida().getDescricao(), stepProduto.getQtMedida());
    }
}
