package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.TagDTO;
import br.com.heycheff.api.app.usecase.TagUseCase;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TagControllerTest {

    static final String URL = "/tags";

    @Autowired
    MockMvc mvc;
    @MockBean
    TagUseCase useCase;

    @Test
    @WithMockUser("heycheff")
    void listAllTags() throws Exception {
        when(useCase.listAll()).thenReturn(tags());

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
    @WithMockUser("heycheff")
    void listTagsByRecipe() throws Exception {
        when(useCase.findByRecipeId(anyLong())).thenReturn(tags());

        mvc.perform(get(URL)
                        .queryParam("recipeId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[1].tag", is("salgado")));
    }
}
