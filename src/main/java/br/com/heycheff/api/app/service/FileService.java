package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileService implements FileUseCase {

    private static final String OS_NAME = "os.name";
    private static final String WINDOWS = "Windows";
    private static final String PROD = "prod";

    @Value("${media-server-paths}")
    List<String> serverPaths;

    final ServletContext context;
    final Environment environment;
    final BlobUseCase blobUseCase;
    private String basePath;
    private boolean isProfileProd;

    public FileService(ServletContext context, Environment environment, BlobUseCase blobUseCase) {
        this.context = context;
        this.environment = environment;
        this.blobUseCase = blobUseCase;
    }

    @PostConstruct
    public void init() {
        var activeProfiles = Arrays.stream(environment.getActiveProfiles()).toList();
        isProfileProd = activeProfiles.contains(PROD);
    }

    @Override
    public String salvar(MultipartFile file, String fileName) {
        try {
            String pathToSave = fileName + "." +
                    Objects.requireNonNull(file.getContentType()).split("/")[1];
            if (isProfileProd) {
                return blobUseCase.uploadMedia(file, pathToSave);
            } else {
                Path baseDir = Paths.get(getBasePath());
                Path filePath = baseDir.resolve(pathToSave);

                Files.createDirectories(baseDir);
                file.transferTo(filePath.toFile());
                return pathToSave;
            }
        } catch (NullPointerException e) {
            throw new MediaException("File must not be null", e);
        } catch (IOException e) {
            throw new MediaException(e);
        }
    }

    @Override
    public Resource getMedia(String fileName) {
        if (isProfileProd)
            return new ByteArrayResource(
                    blobUseCase.getFileOutputStream(fileName).toByteArray()
            );

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

    @Override
    public void delete(String fileName) {
        if (isProfileProd) return;

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

    @Override
    public String resolve(String path) {
        if (isProfileProd)
            return path;
        else
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

    @Override
    public InputStream getJksFile(List<String> jksPath) {
        String path;

        if (System.getProperty(OS_NAME).startsWith(WINDOWS))
            path = jksPath.get(0);
        else
            path = jksPath.get(1);

        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            log.error("{} not found", path);
            throw new MediaException(e);
        }
    }
}