package br.com.heycheff.api.app.usecase;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileUseCase {
    String salvar(MultipartFile file, String fileName);

    Resource getMedia(String fileName);

    void delete(String fileName);

    String resolve(String path);

    InputStream getJksFile(List<String> jksPath);
}
