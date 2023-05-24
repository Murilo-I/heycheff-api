package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String thumb;
    private String titulo;
    private LocalDateTime dateTime;
    private Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Usuario usuario;

    public Receita(String titulo, LocalDateTime dateTime) {
        this.titulo = titulo;
        this.dateTime = dateTime;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setStatus(Boolean status){
        this.status = status;
    }
}


