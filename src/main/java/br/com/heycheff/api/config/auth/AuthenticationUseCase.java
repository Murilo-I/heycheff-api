package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.app.dto.request.ClerkRequest;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.util.exception.AccountNotRegisteredException;
import br.com.heycheff.api.util.exception.GoogleOauthException;
import com.clerk.backend_api.Clerk;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticationUseCase {

    final AuthenticationManager authManager;
    final TokenService tokenService;
    final UserUseCase userService;
    final GoogleIdTokenVerifier verifier;
    final Clerk clerk;

    public AuthenticationUseCase(AuthenticationManager authManager,
                                 TokenService tokenService,
                                 UserUseCase userService, GoogleIdTokenVerifier verifier, Clerk clerk) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.verifier = verifier;
        this.clerk = clerk;
    }

    public TokenDTO authenticate(UserDTO user) {
        var usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        var principal = (User) authManager.authenticate(usernamePasswordToken).getPrincipal();
        userService.updateLastLogin(principal.getId());
        return tokenService.generateToken(principal);
    }

    public TokenDTO authenticate(User user) {
        userService.updateLastLogin(user.getId());
        return tokenService.generateToken(user);
    }

    public User verifyGoogleOauthToken(String token) {
        try {
            var tokenId = verifier.verify(token);

            if (Objects.nonNull(tokenId)) {
                var payload = tokenId.getPayload();
                var email = payload.getEmail();
                var user = userService.findByEmail(email);
                if (user.isPresent()) return user.get();
            }

            throw new AccountNotRegisteredException();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Token verification failed", e);
            throw new GoogleOauthException();
        } catch (Exception e) {
            var errorMsg = "Google Auth Error!";
            log.error(errorMsg, e);
            throw new GoogleOauthException(errorMsg);
        }
    }

    public User verifyClerkSessionId(ClerkRequest request) {
        try {
            var response = clerk.sessions().get().sessionId(request.sessionId()).call();

            if (response.session().isPresent()) {
                var user = userService.findByEmail(request.userEmail());
                if (user.isPresent()) return user.get();
            }

            throw new AccountNotRegisteredException();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GoogleOauthException("Invalid Clerk Session ID!");
        }
    }
}
