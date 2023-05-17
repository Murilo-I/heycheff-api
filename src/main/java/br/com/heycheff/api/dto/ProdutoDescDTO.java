package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDescDTO {

    private String produtoDesc;

    public static ProdutoDescDTO fromEntity(Produto produto) {
        return new ProdutoDescDTO(produto.getDescricao());
    }
}
