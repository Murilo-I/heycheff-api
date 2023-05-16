package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ReceitaProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recProdId;
    @ManyToOne
    @JoinColumn(name = "receitaId")
    private Receita receita;
    @ManyToOne
    @JoinColumn(name = "produtoId")
    private Produto produto;
    private Boolean auditado;
}