package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.app.dto.request.ClerkRequest;
import br.com.heycheff.api.app.service.UserService;
import br.com.heycheff.api.data.model.User;
import br.com.heycheff.api.util.exception.AccountNotRegisteredException;
import br.com.heycheff.api.util.exception.GoogleOauthException;
import com.clerk.backend_api.Clerk;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticationUseCase {

    @Value("${heycheff.oauth.google-client-id}")
    String googleClientId;
    @Value("${heycheff.clerk.secret}")
    String clerkSecret;

    final AuthenticationManager authManager;
    final TokenService tokenService;
    final UserService userService;

    public AuthenticationUseCase(AuthenticationManager authManager,
                                 TokenService tokenService,
                                 UserService userService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    public TokenDTO authenticate(UserDTO user) {
        var usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        return tokenService.generateToken(authManager.authenticate(usernamePasswordToken));
    }

    public TokenDTO authenticate(User user) {
        return tokenService.generateToken(user);
    }

    public User verifyGoogleOauthToken(String token) {
        try {
            var verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
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
            var clerk = Clerk.builder().bearerAuth(clerkSecret).build();
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
