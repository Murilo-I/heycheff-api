package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUseCase {

    final AuthenticationManager authManager;
    final TokenService tokenService;

    public AuthenticationUseCase(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    public TokenDTO authenticate(UserDTO user) {
        var usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        return tokenService.generateToken(authManager.authenticate(usernamePasswordToken));
    }
}
