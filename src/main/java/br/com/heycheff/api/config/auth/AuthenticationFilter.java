package br.com.heycheff.api.config.auth;

import br.com.heycheff.api.data.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static br.com.heycheff.api.config.auth.TokenService.BEARER;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    final TokenService tokenService;
    final UserRepository repository;

    public AuthenticationFilter(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        if (tokenService.isValidToken(token)) authenticate(token);

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        var userIdOnToken = tokenService.getSubject(token);

        repository.findById(userIdOnToken).ifPresent(user -> {
            var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        });
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);

        if (token == null || !token.startsWith(BEARER))
            return null;

        return token.substring(7);
    }
}
