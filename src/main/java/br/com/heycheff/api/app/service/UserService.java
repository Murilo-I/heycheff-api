package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.request.UserRequest;
import br.com.heycheff.api.data.model.Role;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.UserRepository;
import br.com.heycheff.api.util.exception.UserRegistrationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User save(UserRequest request) {
        try {
            return repository.save(
                    User.builder().email(request.getEmail())
                            .username(request.getUsername())
                            .password(new BCryptPasswordEncoder()
                                    .encode(request.getPassword()))
                            .roles(Collections.singletonList(Role.USER))
                            .build()
            );
        } catch (Exception e) {
            throw new UserRegistrationException(e);
        }
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
