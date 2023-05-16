package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Integer> {

}
