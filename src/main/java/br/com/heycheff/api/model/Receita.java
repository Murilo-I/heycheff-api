package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Receita {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer receitaId;
	private String thumb;
	private String titulo;
	private LocalDateTime dateTime;
	@ManyToOne
	@JoinColumn(name = "userId")
	private Usuario usuario;
	@OneToMany
	@JoinColumn(name = "tagId")
	private Tag tags;
}