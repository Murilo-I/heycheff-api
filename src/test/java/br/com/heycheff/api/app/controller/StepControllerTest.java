package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.usecase.StepUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.heycheff.api.app.service.RecipeServiceTest.SCRAMBLED_EGGS;
import static br.com.heycheff.api.app.service.StepServiceTest.step;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

        var mockFile = new MockMultipartFile(
                "video",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mvc.perform(multipart(URL)
                        .file(mockFile)
                        .param("stepNumber", "1")
                        .param("timeMinutes", "60")
                        .param("modoPreparo", "quebre 2 ovos e misture ao frango desfiado")
                        .param("produtos", "[ { \"desc\": \"ovo\", \"unidMedida\": \"unidade\", " +
                                "\"medida\": 3 }, { \"desc\": \"frango desfiado\", \"unidMedida\": \"grama\", " +
                                "\"medida\": 300 } ]"))
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
