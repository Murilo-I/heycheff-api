package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Tag;
import br.com.heycheff.api.model.TagReceita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagReceitaRepository extends JpaRepository<TagReceita, Integer> {

    @Query("select tr.tag from TagReceita tr where tr.receita.id = ?1")
    List<Tag> findByReceitaId(Integer id);
}
