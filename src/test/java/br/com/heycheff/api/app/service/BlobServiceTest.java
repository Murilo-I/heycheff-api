package br.com.heycheff.api.app.service;

import br.com.heycheff.api.app.usecase.BlobUseCase;
import br.com.heycheff.api.util.exception.MediaException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static br.com.heycheff.api.data.helper.DataHelper.multipart;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BlobServiceTest {

    BlobClient mockClient = mock(BlobClient.class);
    BlobServiceClient blobClient = mock(BlobServiceClient.class);
    BlobUseCase useCase = new BlobService(blobClient);

    @BeforeEach
    void setup() {
        var mockContainer = mock(BlobContainerClient.class);
        when(blobClient.getBlobContainerClient(null))
                .thenReturn(mockContainer);
        when(mockContainer.getBlobClient(anyString()))
                .thenReturn(mockClient);
    }

    @Test
    void shouldUploadMedia() {
        doNothing().when(mockClient).upload(any(), anyLong(), anyBoolean());
        when(mockClient.getBlobName()).thenReturn("newBlob");

        assertDoesNotThrow(() -> useCase.uploadMedia(multipart("blob"), "fileN"));
    }

    @Test
    void shouldThrowNullPointerWhenUploadingMedia() {
        doThrow(NullPointerException.class).when(mockClient)
                .upload(any(), anyLong(), anyBoolean());

        assertThrows(MediaException.class,
                () -> useCase.uploadMedia(multipart("blob"), "fileN"));
    }

    @Test
    void shouldThrowIOExceptionWhenUploadingMedia() throws IOException {
        doNothing().when(mockClient).upload(any(), anyLong(), anyBoolean());

        var corruptedFile = spy(MultipartFile.class);
        doThrow(IOException.class).when(corruptedFile).getInputStream();

        assertThrows(MediaException.class,
                () -> useCase.uploadMedia(corruptedFile, "fileN"));
    }

    @Test
    void shouldGetFileOutputStream() {
        doNothing().when(mockClient).downloadStream(any());

        var stream = useCase.getFileOutputStream("blobN");

        assertNotNull(stream);
    }
}
