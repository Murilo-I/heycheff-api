package br.com.heycheff.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter

public class ReceitasStep {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id_step;
		private Integer id_receita;
		private Integer min_ini;
		private Integer min_fim;
		private Integer step;
		private Integer id_produto;
		private Integer qtd;
		
}