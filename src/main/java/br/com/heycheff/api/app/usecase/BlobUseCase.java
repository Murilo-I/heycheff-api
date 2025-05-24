package br.com.heycheff.api.app.usecase;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface BlobUseCase {
    String uploadMedia(MultipartFile file, String fileName);

    ByteArrayOutputStream getFileOutputStream(String blobName);
}
