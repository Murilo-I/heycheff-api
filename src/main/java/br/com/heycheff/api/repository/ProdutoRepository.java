package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    Optional<Produto> findByDescricao(String descricao);
}
