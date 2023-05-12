package br.com.heycheff.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter

public class UnidadeMedida {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer unid_medida;
		private String descricao;
		
		
}