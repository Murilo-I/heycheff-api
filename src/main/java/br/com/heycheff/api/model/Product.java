package br.com.heycheff.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    private String descricao;
    private Boolean auditado;
    private UnidadeMedida unidMedida;
    private Float qtMedida;
}
