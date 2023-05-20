package br.com.heycheff.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
        String pathToSave = fileName + "." +
                Objects.requireNonNull(file.getContentType()).split("/")[1];
        Path filePath = dirPath.resolve(pathToSave);

        try {
            Files.createDirectories(dirPath);
            file.transferTo(filePath.toFile());
            return pathToSave;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getMedia(String path) {
        Path dirPath = Paths.get(basePath);
        Path file = dirPath.resolve(path);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new FileNotFoundException();
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
