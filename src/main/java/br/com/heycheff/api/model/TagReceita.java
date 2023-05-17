package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TagReceita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "receitaId")
    private Receita receita;
    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    public TagReceita(Receita receita, Tag tag) {
        this.receita = receita;
        this.tag = tag;
    }
}
