package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {

    Optional<UnidadeMedida> findByDescricao(String descricao);
}