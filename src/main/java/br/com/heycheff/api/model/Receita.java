package br.com.heycheff.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Receita {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String thumb;
	private String titulo;
	private LocalDateTime dateTime;
	@ManyToOne
	@JoinColumn(name = "userId")
	private Usuario usuario;

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
}