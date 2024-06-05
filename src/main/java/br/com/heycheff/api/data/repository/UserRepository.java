package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}

	   
