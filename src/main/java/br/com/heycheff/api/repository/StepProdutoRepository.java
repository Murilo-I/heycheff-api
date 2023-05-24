package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.ReceitaStep;
import br.com.heycheff.api.model.StepProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepProdutoRepository extends JpaRepository<StepProduto, Integer> {

    List<StepProduto> findByStep(ReceitaStep step);
}
