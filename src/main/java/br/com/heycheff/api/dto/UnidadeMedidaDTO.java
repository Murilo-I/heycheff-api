package br.com.heycheff.api.dto;

import br.com.heycheff.api.model.UnidadeMedida;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnidadeMedidaDTO {

    private String descricao;

    public static UnidadeMedidaDTO fromEntity(UnidadeMedida medida) {
        return new UnidadeMedidaDTO(medida.getDescricao());
    }
}
