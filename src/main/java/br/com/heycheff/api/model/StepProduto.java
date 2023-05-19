package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StepProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recStepProd;
    @ManyToOne
    @JoinColumn(name = "stepId")
    private ReceitaStep step;
    @ManyToOne
    @JoinColumn(name = "produtoId")
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "unidMedidaId")
    private UnidadeMedida unidMedida;
    private Float qtMedida;

    public StepProduto(ReceitaStep step, Produto produto, UnidadeMedida unidMedida, Float qtMedida) {
        this.step = step;
        this.produto = produto;
        this.unidMedida = unidMedida;
        this.qtMedida = qtMedida;
    }
}