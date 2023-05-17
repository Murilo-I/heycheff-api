package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
