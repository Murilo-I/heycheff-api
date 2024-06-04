package br.com.heycheff.api.service;

import br.com.heycheff.api.util.exception.MediaException;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileService {

    private static final String OS_NAME = "os.name";
    private static final String WINDOWS = "Windows";

    @Value("${media-server-paths}")
    List<String> serverPaths;

    final ServletContext context;
    private String basePath;

    public FileService(ServletContext context) {
        this.context = context;
    }

    public String salvar(MultipartFile file, String fileName) {
        try {
            Path baseDir = Paths.get(getBasePath());
            String pathToSave = fileName + "." +
                    Objects.requireNonNull(file.getContentType()).split("/")[1];
            Path filePath = baseDir.resolve(pathToSave);

            Files.createDirectories(baseDir);
            file.transferTo(filePath.toFile());
            return pathToSave;
        } catch (NullPointerException e) {
            throw new MediaException("File must not be null", e);
        } catch (IOException e) {
            throw new MediaException(e);
        }
    }

    public Resource getMedia(String fileName) {
        Path baseDir = Paths.get(getBasePath());
        Path filePath = baseDir.resolve(fileName);

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new FileNotFoundException();
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new MediaException(e);
        }
    }

    public void delete(String fileName) {
        Path baseDir = Paths.get(getBasePath());
        Path filePath = baseDir.resolve(fileName);

        try {
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            log.warn("File {} already deleted", fileName);
        } catch (IOException e) {
            throw new MediaException(e);
        }
    }

    public String resolve(String path) {
        return context.getContextPath() + "/media?path=" + path;
    }

    private String getBasePath() {
        if (Objects.isNull(basePath)) {
            if (System.getProperty(OS_NAME).startsWith(WINDOWS))
                this.basePath = serverPaths.get(0);
            else
                this.basePath = serverPaths.get(1);
        }
        return basePath;
    }
}
