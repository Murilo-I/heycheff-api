package br.com.heycheff.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter

public class ReceitasStepProd {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer rec_step_prod;
		private Integer id_step;
		private Integer id_produto;
		private Integer id_receita;
		private String descricao;
}