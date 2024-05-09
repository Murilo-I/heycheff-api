package br.com.heycheff.api.controller;

import br.com.heycheff.api.service.StepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.heycheff.api.service.ReceiptServiceTest.SCRAMBLED_EGGS;
import static br.com.heycheff.api.service.ReceiptServiceTest.multipart;
import static br.com.heycheff.api.service.StepServiceTest.step;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StepControllerTest {

    static final String URL = "/receitas/1/steps";

    @Autowired
    MockMvc mvc;
    @MockBean
    StepService service;

    @Test
    void saveStep() throws Exception {
        when(service.save(any(), any(), anyLong())).thenReturn(step());

        var formData = """
                step:1
                modoPreparo:quebre 2 ovos e misture ao frango desfiado
                produtos:[ { "desc": "ovo", "unidMedida": "unidade", "medida": 3 }, { "desc": "frango desfiado", "unidMedida": "grama", "medida": 300 } ]
                """;

        mvc.perform(post(URL)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(formData)
                        .content(multipart().getBytes()))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.preparationMode", is(SCRAMBLED_EGGS)));
    }

    @Test
    void deleteStep() throws Exception {
        doNothing().when(service).delete(anyLong(), anyLong());
        mvc.perform(delete(URL + "/1")).andExpect(status().isNoContent());
    }
}
