package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReceitaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepId;
    @ManyToOne
    @JoinColumn(name = "receitaId")
    private Receita receita;
    private String path;
    private Integer step;
    private Integer qtd;
    private String modoPreparo;

    public ReceitaStep(Receita receita, String path, Integer step, Integer qtd, String modoPreparo) {
        this.receita = receita;
        this.path = path;
        this.step = step;
        this.qtd = qtd;
        this.modoPreparo = modoPreparo;
    }
}