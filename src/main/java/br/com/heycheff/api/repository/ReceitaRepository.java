package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, Integer> {
    List<Receita> findByStatus(Boolean status);

}
