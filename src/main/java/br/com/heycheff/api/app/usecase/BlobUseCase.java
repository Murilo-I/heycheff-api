package br.com.heycheff.api.app.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface BlobUseCase {
    String uploadMedia(MultipartFile file, String fileName);
}
