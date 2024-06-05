package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.config.auth.AuthenticationService;
import br.com.heycheff.api.app.dto.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> authenticate(@RequestBody UserDTO user) {
        return ResponseEntity.ok().body(authService.authenticate(user));
    }
}
