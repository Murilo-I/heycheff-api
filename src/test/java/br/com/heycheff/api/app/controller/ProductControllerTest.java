package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.ProductDescDTO;
import br.com.heycheff.api.app.usecase.ProductUseCase;
import br.com.heycheff.api.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class ProductControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    ProductUseCase useCase;

    @Test
    @WithMockUser("heycheff")
    void listAllProducts() throws Exception {
        when(useCase.listProducts()).thenReturn(Set.of(new ProductDescDTO("prod")));

        mvc.perform(get("/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].produtoDesc", is("prod")));
    }
}
