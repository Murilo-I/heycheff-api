package br.com.heycheff.api.repository;

import br.com.heycheff.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UsuarioRepository extends MongoRepository<Usuario, String> {

}

	   
