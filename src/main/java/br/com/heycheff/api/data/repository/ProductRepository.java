package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.ProductDescriptions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<ProductDescriptions, Integer> {

    Optional<ProductDescriptions> findByValue(String description);
}
