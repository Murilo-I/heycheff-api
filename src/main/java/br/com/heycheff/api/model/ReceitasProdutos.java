package br.com.heycheff.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter

public class ReceitasProdutos {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id_rec_prod;
		private Integer id_receita;
		private Integer id_produto;
		private String auditado;
		
}
