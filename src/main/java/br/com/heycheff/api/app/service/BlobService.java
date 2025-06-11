package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class BlobService implements BlobUseCase {

    @Value("${heycheff.azure.container-name}")
    String containerName;

    private final BlobServiceClient blobServiceClient;

    public BlobService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
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