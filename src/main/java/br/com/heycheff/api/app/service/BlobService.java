package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Profile({"dev", "prod"})
public class BlobService implements BlobUseCase {

    @Value("${heycheff.azure.container-name}")
    String containerName;
    @Value("${heycheff.azure.blob-storage}")
    String blobStorageConnectionURI;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init() {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(blobStorageConnectionURI).buildClient();
    }

    @Override
    public String uploadMedia(MultipartFile file, String fileName) {
        try {
            var blobClient = blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new MediaException("Blob Storage Upload Failed!", e);
        }
    }
}
