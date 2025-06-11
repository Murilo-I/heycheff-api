package br.com.heycheff.api.config;

import com.azure.storage.blob.BlobServiceClient;
import com.clerk.backend_api.Clerk;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Profile("test")
@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    public BlobServiceClient blobServiceClient() {
        return mock(BlobServiceClient.class);
    }

    @Bean
    public GoogleIdTokenVerifier googleVerifier() {
        return mock(GoogleIdTokenVerifier.class);
    }

    @Bean
    public Clerk clerk() {
        return mock(Clerk.class);
    }
}
