package br.com.heycheff.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.heycheff.api.model.ReceitaStep;

public interface ReceitaStepRepository extends JpaRepository<ReceitaStep, Integer> {

}