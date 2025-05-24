package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
public class BlobService implements BlobUseCase {

    private static final String TEST = "test";

    @Value("${heycheff.azure.container-name}")
    String containerName;
    @Value("${heycheff.azure.blob-storage}")
    String blobStorageConnectionURI;
    @Value("${heycheff.azure.account-name}")
    String accountName;
    @Value("${heycheff.azure.account-key}")
    String accountKey;

    private BlobServiceClient blobServiceClient;
    final Environment environment;

    public BlobService(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        var activeProfiles = Arrays.stream(environment.getActiveProfiles()).toList();

        if (activeProfiles.contains(TEST)) return;

        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(blobStorageConnectionURI)
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }

    @Override
    public String uploadMedia(MultipartFile file, String fileName) {
        try {
            var blobClient = blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return blobClient.getBlobName();
        } catch (NullPointerException e) {
            throw new MediaException(e);
        } catch (IOException e) {
            throw new MediaException("Blob Storage Upload Failed!", e);
        }
    }

    @Override
    public ByteArrayOutputStream getFileOutputStream(String blobName) {
        BlobClient client = blobServiceClient.getBlobContainerClient(containerName)
                .getBlobClient(blobName);
        var stream = new ByteArrayOutputStream();
        client.downloadStream(stream);
        return stream;
    }
}
