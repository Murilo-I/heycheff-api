package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReceitaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepId;
    @ManyToOne
    @JoinColumn(name = "receitaId")
    private Receita receita;
    private String path;
    private Integer step;
    @OneToMany
    @JoinColumn(name = "produtoId")
    private Produto produto;
    private Integer qtd;
}