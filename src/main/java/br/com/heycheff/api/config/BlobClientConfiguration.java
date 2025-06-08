package br.com.heycheff.api.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class BlobClientConfiguration {

    @Value("${heycheff.azure.blob-storage}")
    String blobStorageConnectionURI;
    @Value("${heycheff.azure.account-name}")
    String accountName;
    @Value("${heycheff.azure.account-key}")
    String accountKey;

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(blobStorageConnectionURI)
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }
}
