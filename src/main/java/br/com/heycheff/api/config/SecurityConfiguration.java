package br.com.heycheff.api.config;

import br.com.heycheff.api.config.auth.AuthenticationFilter;
import br.com.heycheff.api.data.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebMvc
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "PATCH",
            "DELETE", "OPTIONS");
    private static final List<String> ALLOWED_HEADERS = List.of("Content-Type", "X-Requested-With",
            "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers",
            "Authorization");
    private static final List<String> EXPOSED_HEADERS = List.of("Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials");
    private static final String[] IGNORING_MATCHERS = {"/**.html", "/v2/api-docs", "/webjars/**",
            "/configuration/**", "/swagger-resources/**"};

    @Value("${cors.allowed-origins}")
    List<String> allowedOrigins;

    final AuthenticationFilter authenticationFilter;

    public SecurityConfiguration(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setExposedHeaders(EXPOSED_HEADERS);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(httpCustomizer -> httpCustomizer
                        .requestMatchers(POST, "/user").permitAll()
                        .requestMatchers(POST, "/auth").permitAll()
                        .requestMatchers(POST, "/auth/clerk").permitAll()
                        .requestMatchers(GET, "/receitas").permitAll()
                        .requestMatchers(GET, "/media**").permitAll()
                        .requestMatchers(GET, "/receitas/all").hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers(GET, "/user/all").hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(IGNORING_MATCHERS);
    }
}
