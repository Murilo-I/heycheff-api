package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    final AuthenticationManager authManager;
    final TokenService tokenService;

    public AuthenticationService(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    public TokenDTO authenticate(UserDTO user) {
        var usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        return tokenService.generateToken(authManager.authenticate(usernamePasswordToken));
    }
}
