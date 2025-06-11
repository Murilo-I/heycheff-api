package br.com.heycheff.api.config.auth;

import com.clerk.backend_api.Clerk;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
@Profile("prod")
public class OAuthBeans {

    @Value("${heycheff.oauth.google-client-id}")
    String googleClientId;
    @Value("${heycheff.clerk.secret}")
    String clerkSecret;

    @Bean
    public GoogleIdTokenVerifier googleVerifier() {
        return new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Bean
    public Clerk clerk() {
        return Clerk.builder().bearerAuth(clerkSecret).build();
    }
}
