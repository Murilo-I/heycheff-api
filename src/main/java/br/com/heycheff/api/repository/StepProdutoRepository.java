package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.ReceitaStep;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.heycheff.api.model.StepProduto;

import java.util.List;

public interface StepProdutoRepository extends JpaRepository<StepProduto, Integer> {

    List<StepProduto> findByStep(ReceitaStep step);
}
