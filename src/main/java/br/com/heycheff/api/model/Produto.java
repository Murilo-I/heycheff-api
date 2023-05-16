package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer produtoId;
    private String descricao;
    @OneToMany
    @JoinColumn(name = "unidMedida")
    private UnidadeMedida unidMedida;
}