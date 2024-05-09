package br.com.heycheff.api.controller;

import br.com.heycheff.api.dto.TagDTO;
import br.com.heycheff.api.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    static final String URL = "/tags";

    @Autowired
    MockMvc mvc;
    @MockBean
    TagService service;

    @Test
    void listAllTags() throws Exception {
        when(service.listAll()).thenReturn(tags());

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tag", is("doce")));
    }

    private static List<TagDTO> tags() {
        return List.of(new TagDTO(1, "doce"), new TagDTO(2, "salgado"));
    }

    @Test
    void listTagsByReceipt() throws Exception {
        when(service.findByReceiptId(anyLong())).thenReturn(tags());

        mvc.perform(get(URL)
                        .queryParam("receiptId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[1].tag", is("salgado")));
    }
}
