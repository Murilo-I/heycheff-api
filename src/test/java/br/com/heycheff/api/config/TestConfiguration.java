package br.com.heycheff.api.config;

import com.azure.storage.blob.BlobServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    @Profile("test")
    public BlobServiceClient blobServiceClient() {
        return mock(BlobServiceClient.class);
    }
}
