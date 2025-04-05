package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.usecase.StepUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.heycheff.api.app.service.RecipeServiceTest.SCRAMBLED_EGGS;
import static br.com.heycheff.api.app.service.RecipeServiceTest.multipart;
import static br.com.heycheff.api.app.service.StepServiceTest.step;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StepControllerTest {

    static final String URL = "/receitas/1/steps";

    @Autowired
    MockMvc mvc;
    @MockBean
    StepUseCase useCase;

    @Test
    @WithMockUser("heycheff")
    void saveStep() throws Exception {
        when(useCase.save(any(), any(), anyLong())).thenReturn(step());

        var formData = """
                stepNumber:1
                timeMinutes:60
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
    @WithMockUser("heycheff")
    void deleteStep() throws Exception {
        when(useCase.delete(anyInt(), anyLong())).thenReturn(step());
        mvc.perform(delete(URL + "/1")).andExpect(status().isNoContent());
    }
}
