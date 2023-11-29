package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {

    private String desc;
    private String unidMedida;
    private Float medida;

    public static ProdutoDTO fromEntity(Product product) {
        return new ProdutoDTO(product.getDescricao(),
                product.getUnidMedida().getDescricao(),
                product.getQtMedida());
    }
}
