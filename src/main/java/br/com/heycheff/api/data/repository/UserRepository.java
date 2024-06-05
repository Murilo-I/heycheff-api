package br.com.heycheff.api.data.repository;

import br.com.heycheff.api.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

}

	   
