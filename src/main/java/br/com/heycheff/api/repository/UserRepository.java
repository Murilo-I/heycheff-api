package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

}

	   
