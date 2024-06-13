package br.com.heycheff.api.app.service;

import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    final UserRepository repository;

    public AuthenticationService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);

        if (user.isPresent()) return user.get();

        throw new UsernameNotFoundException("Usuário ou senha inválidos");
    }
}
