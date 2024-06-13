package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.config.auth.AuthenticationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final AuthenticationUseCase authUseCase;

    public AuthenticationController(AuthenticationUseCase authService) {
        this.authUseCase = authService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> authenticate(@RequestBody UserDTO user) {
        return ResponseEntity.ok().body(authUseCase.authenticate(user));
    }
}
