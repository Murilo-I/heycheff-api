package br.com.heycheff.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.heycheff.api.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Integer>{

}
