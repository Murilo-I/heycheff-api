package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.heycheff.api.model.ReceitaStep;

import java.util.List;

public interface ReceitaStepRepository extends JpaRepository<ReceitaStep, Integer> {

    List<ReceitaStep> findByReceita(Receita receita);
}