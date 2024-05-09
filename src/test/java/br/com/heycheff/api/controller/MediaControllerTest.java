package br.com.heycheff.api.controller;

import br.com.heycheff.api.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MediaControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    FileService fileService;

    @Test
    void retrieveMedia() throws Exception {
        when(fileService.getMedia(anyString())).thenReturn(any());

        mvc.perform(get("/media")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .queryParam("path", "thumb"))
                .andExpect(status().isOk());
    }
}
