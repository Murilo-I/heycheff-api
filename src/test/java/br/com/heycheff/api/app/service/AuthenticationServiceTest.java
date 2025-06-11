package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.dto.TokenDTO;
import br.com.heycheff.api.app.dto.UserDTO;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.config.auth.AuthenticationUseCase;
import br.com.heycheff.api.config.auth.TokenService;
import br.com.heycheff.api.util.exception.GoogleOauthException;
import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.Sessions;
import com.clerk.backend_api.models.components.Session;
import com.clerk.backend_api.models.operations.GetSessionRequestBuilder;
import com.clerk.backend_api.models.operations.GetSessionResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static br.com.heycheff.api.data.helper.DataHelper.clerkRequest;
import static br.com.heycheff.api.data.helper.DataHelper.user;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    final AuthenticationManager authManager = mock(AuthenticationManager.class);
    final TokenService tokenService = mock(TokenService.class);
    final UserUseCase userService = mock(UserUseCase.class);
    final GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
    final Clerk clerk = mock(Clerk.class);
    AuthenticationUseCase useCase = new AuthenticationUseCase(authManager, tokenService,
            userService, verifier, clerk);

    @Test
    void shouldAuthenticate() {
        Authentication authMock = mock(Authentication.class);
        when(authManager.authenticate(any())).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(user());
        doNothing().when(userService).updateLastLogin(anyString());
        when(tokenService.generateToken(any())).thenReturn(new TokenDTO());

        var token1 = useCase.authenticate(user());
        var token2 = useCase.authenticate(new UserDTO("uName", "uPass"));

        assertNotNull(token1);
        assertNotNull(token2);
    }

    private GoogleIdToken googleIdToken() {
        return new GoogleIdToken(
                new JsonWebSignature.Header(),
                new GoogleIdToken.Payload().setEmail("g@mail.com"),
                new byte[]{}, new byte[]{}
        );
    }

    @Test
    void shouldVerifyGoogleOAuthToken() throws GeneralSecurityException, IOException {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user()));
        when(verifier.verify(anyString())).thenReturn(googleIdToken());

        var user = useCase.verifyGoogleOauthToken("token");

        assertNotNull(user);
    }

    @Test
    void shouldThrowExceptionWhenVerifyingGoogleOAuthToken() throws GeneralSecurityException, IOException {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(verifier.verify(anyString())).thenReturn(googleIdToken());

        assertThrows(GoogleOauthException.class,
                () -> useCase.verifyGoogleOauthToken("token"));
    }

    @Test
    void shouldThrowSecurityExceptionWhenVerifyingGoogleOAuthToken() throws GeneralSecurityException, IOException {
        doThrow(GeneralSecurityException.class).when(verifier).verify(anyString());

        assertThrows(GoogleOauthException.class,
                () -> useCase.verifyGoogleOauthToken("token"));
    }

    private void mockClerk() throws Exception {
        var sessions = mock(Sessions.class);
        var sessionBuilder = mock(GetSessionRequestBuilder.class);
        var response = mock(GetSessionResponse.class);
        var sessionMock = mock(Session.class);
        when(clerk.sessions()).thenReturn(sessions);
        when(sessions.get()).thenReturn(sessionBuilder);
        when(sessionBuilder.sessionId(anyString())).thenReturn(sessionBuilder);
        when(sessionBuilder.call()).thenReturn(response);
        when(response.session()).thenReturn(Optional.of(sessionMock));
    }

    @Test
    void shouldVerifyClerkSessionId() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user()));
        mockClerk();

        var user = useCase.verifyClerkSessionId(clerkRequest());

        assertNotNull(user);
    }

    @Test
    void shouldThrowExceptionWhenVerifyingClerkSessionId() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        mockClerk();

        assertThrows(GoogleOauthException.class,
                () -> useCase.verifyClerkSessionId(clerkRequest()));
    }
}
