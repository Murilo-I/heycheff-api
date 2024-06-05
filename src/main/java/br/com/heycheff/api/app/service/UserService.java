package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.data.model.Role;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService {

    final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User save(UserRequest request) {
        return repository.save(
                User.builder().email(request.getEmail())
                        .password(request.getPassword())
                        .username(request.getUsername())
                        .roles(Collections.singletonList(Role.USER))
                        .build()
        );
    }
}
