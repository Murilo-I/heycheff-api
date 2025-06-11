package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.app.usecase.FileUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static br.com.heycheff.api.data.helper.DataHelper.multipart;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileServiceTest {

    static final String TEST_PATH = "src/test/resources";
    static final String TEST_PLAIN = "test.plain";

    ServletContext context = mock(ServletContext.class);
    Environment environment = mock(Environment.class);
    BlobUseCase blobUseCase = mock(BlobUseCase.class);
    FileUseCase useCase = new FileService(context, environment, blobUseCase);

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(useCase, "serverPaths", List.of(TEST_PATH, TEST_PATH));
        when(context.getContextPath()).thenReturn("heycheff-test");
    }

    @Test
    void shouldTestFileServiceSuccessfully() {
        var savePath = useCase.salvar(multipart("file"), "test");
        var resource = useCase.getMedia(TEST_PLAIN);
        var path = useCase.resolve(TEST_PLAIN);

        assertAll(() -> useCase.delete(TEST_PLAIN), () -> useCase.delete(TEST_PLAIN));
        assertEquals(TEST_PLAIN, savePath);
        assertNotNull(resource);
        assertEquals("heycheff-test/media?path=" + TEST_PLAIN, path);
    }

    @Test
    void shouldTestFileServiceProdSuccessfully() {
        ReflectionTestUtils.setField(useCase, "isProfileProd", true);

        when(blobUseCase.uploadMedia(any(), anyString())).thenReturn("blobN");
        when(blobUseCase.getFileOutputStream(anyString()))
                .thenReturn(new ByteArrayOutputStream());

        var savePath = useCase.salvar(multipart("file"), "test");
        var resource = useCase.getMedia(TEST_PLAIN);
        var path = useCase.resolve("same-path");

        assertDoesNotThrow(() -> useCase.delete("not-delete"));
        assertEquals("blobN", savePath);
        assertNotNull(resource);
        assertEquals("same-path", path);
    }

    @Test
    void validateFileServiceErrorScenarios() throws IOException {
        var corruptedFile = spy(MultipartFile.class);
        when(corruptedFile.getContentType()).thenReturn("text/plain");
        doThrow(IOException.class).when(corruptedFile).transferTo(any(File.class));

        useCase.delete(TEST_PLAIN);

        assertThrows(MediaException.class, () -> useCase.getMedia("test"));
        assertThrows(MediaException.class, () -> useCase.salvar(null, "test"));
        assertThrows(MediaException.class, () -> useCase.salvar(corruptedFile, "test"));
    }
}
