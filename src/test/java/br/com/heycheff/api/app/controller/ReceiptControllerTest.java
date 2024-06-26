package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.response.ReceiptFeed;
import br.com.heycheff.api.app.dto.response.ReceiptModal;
import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.service.ReceiptService;
import br.com.heycheff.api.util.exception.ReceiptNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static br.com.heycheff.api.app.service.ReceiptServiceTest.multipart;
import static br.com.heycheff.api.app.service.ReceiptServiceTest.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReceiptControllerTest {

    static final long ID = 1L;
    static final String URL = "/receitas";
    static final String URL_TEMPLATE = URL + "/" + ID;

    @Autowired
    MockMvc mvc;
    @MockBean
    ReceiptService service;

    @Test
    @WithMockUser("heycheff")
    void loadFeed() throws Exception {
        when(service.loadFeed())
                .thenReturn(List.of(new ReceiptFeed(ID, "thumb", "title")));

        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo", is("title")));
    }

    @Test
    @WithMockUser("heycheff")
    void loadModal() throws Exception {
        when(service.loadModal(anyLong())).thenReturn(modal());

        mvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tags[0].id", is(1)))
                .andExpect(jsonPath("$.steps[0].modoPreparo", is("prepare")));
    }

    private ReceiptModal modal() {
        var modal = new ReceiptModal();
        modal.setTags(List.of(new TagDTO(1, "tag")));
        modal.setSteps(List.of(new StepDTO("path", 1, emptyList(), "prepare")));
        return modal;
    }

    @Test
    @WithMockUser("heycheff")
    void modalNotFound() throws Exception {
        doThrow(ReceiptNotFoundException.class).when(service).loadModal(anyLong());

        mvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("heycheff")
    void includeReceipt() throws Exception {
        when(service.save(any(), any())).thenReturn(receipt());

        var formData = """
                titulo:Camarão do Baiano
                tags:[ { "id": 1, "tag": "Salgado" } ]
                """;

        mvc.perform(post(URL)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(formData)
                        .content(multipart().getBytes()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(SCRAMBLED_EGGS)));
    }

    @Test
    @WithMockUser("heycheff")
    void updateStatus() throws Exception {
        doNothing().when(service).updateStatus(any(), anyLong());

        var json = """
                {
                    "status": true
                }
                """;

        mvc.perform(patch(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }
}
