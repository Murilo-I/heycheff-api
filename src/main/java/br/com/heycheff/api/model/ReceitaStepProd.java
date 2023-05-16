package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReceitaStepProd {
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
    @JoinColumn(name = "receitaId")
    private Receita receita;
    private String descricao;
}