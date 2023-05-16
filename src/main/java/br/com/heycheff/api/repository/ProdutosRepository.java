package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produto, Integer> {

}
