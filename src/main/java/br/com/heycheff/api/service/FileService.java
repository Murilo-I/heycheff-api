package br.com.heycheff.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileService {

    @Value("${media-base-path}")
    private String basePath;

    public String salvar(MultipartFile file, String fileName) {
        Path dirPath = Paths.get(basePath);
        Path filePath = dirPath.resolve(fileName + "." +
                Objects.requireNonNull(file.getContentType()).split("/")[1]);
        try {
            Files.createDirectories(dirPath);
            file.transferTo(filePath.toFile());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
