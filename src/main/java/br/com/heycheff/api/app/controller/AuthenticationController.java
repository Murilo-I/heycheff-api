package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.app.dto.request.ClerkRequest;
import br.com.heycheff.api.config.auth.AuthenticationUseCase;
import br.com.heycheff.api.util.exception.AccountNotRegisteredException;
import com.google.api.client.http.HttpStatusCodes;
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
        return ResponseEntity.ok(authUseCase.authenticate(user));
    }

    @PostMapping("/clerk")
    public ResponseEntity<TokenDTO> authenticateWithClerk(@RequestBody ClerkRequest request) {
        try {
            var user = authUseCase.verifyClerkSessionId(request);
            return ResponseEntity.ok(authUseCase.authenticate(user));
        } catch (AccountNotRegisteredException e) {
            return ResponseEntity.status(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED).build();
        }
    }
}
