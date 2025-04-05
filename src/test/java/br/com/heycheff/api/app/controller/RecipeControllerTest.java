package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.StepDTO;
import br.com.heycheff.api.app.dto.response.PageResponse;
import br.com.heycheff.api.app.dto.response.RecipeFeed;
import br.com.heycheff.api.app.dto.response.RecipeId;
import br.com.heycheff.api.app.dto.response.RecipeModal;
import br.com.heycheff.api.app.usecase.RecipeUseCase;
import br.com.heycheff.api.util.exception.RecipeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static br.com.heycheff.api.app.service.RecipeServiceTest.multipart;
import static br.com.heycheff.api.app.service.RecipeServiceTest.recipe;
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
class RecipeControllerTest {

    static final long ID = 1L;
    static final String URL = "/receitas";
    static final String URL_TEMPLATE = URL + "/" + ID;

    @Autowired
    MockMvc mvc;
    @MockBean
    RecipeUseCase useCase;

    @Test
    @WithMockUser("heycheff")
    void loadFeed() throws Exception {
        when(useCase.loadFeed(PageRequest.of(1, 1)))
                .thenReturn(new PageResponse<>(
                        List.of(new RecipeFeed(
                                ID, "thumb", "title", Collections.emptyList(), 15)
                        ), 1L)
                );

        mvc.perform(get(URL)
                        .queryParam("pageNum", String.valueOf(1))
                        .queryParam("pageSize", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items[0].titulo", is("title")))
                .andExpect(jsonPath("$.count", is(1)));
    }

    @Test
    @WithMockUser("heycheff")
    void loadModal() throws Exception {
        when(useCase.loadModal(anyLong())).thenReturn(modal());

        mvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.steps[0].modoPreparo", is("prepare")));
    }

    private RecipeModal modal() {
        var modal = new RecipeModal();
        modal.setSteps(List.of(new StepDTO(
                "path", 1, emptyList(), "prepare", 15
        )));
        return modal;
    }

    @Test
    @WithMockUser("heycheff")
    void modalNotFound() throws Exception {
        doThrow(RecipeNotFoundException.class).when(useCase).loadModal(anyLong());

        mvc.perform(get(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("heycheff")
    void includeReceipt() throws Exception {
        when(useCase.save(any(), any())).thenReturn(new RecipeId(recipe().getSeqId()));

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
                .andExpect(jsonPath("$.seqId", is(1)));
    }

    @Test
    @WithMockUser("heycheff")
    void updateStatus() throws Exception {
        doNothing().when(useCase).updateStatus(any(), anyLong());

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
