package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.dto.request.FollowRequest;
import br.com.heycheff.api.app.dto.request.RecommendationRequest;
import br.com.heycheff.api.app.dto.response.FollowResponse;
import br.com.heycheff.api.app.dto.response.UserResponse;
import br.com.heycheff.api.app.usecase.UserUseCase;
import br.com.heycheff.api.config.TestConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static br.com.heycheff.api.data.helper.DataHelper.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class UserControllerTest {

    static final String URL = "/user";
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    UserUseCase useCase;

    @Test
    @WithMockUser(value = "heycheff", authorities = "ADMIN")
    void getAll() throws Exception {
        when(useCase.findAll()).thenReturn(List.of());

        mvc.perform(get(URL + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("heycheff")
    void getOne() throws Exception {
        when(useCase.findById(anyString())).thenReturn(UserResponse.builder().build());

        mvc.perform(get(URL + "/123"))
                .andExpect(status().isOk());
    }

    @Test
    void save() throws Exception {
        when(useCase.save(any())).thenReturn(user());

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest())))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(USER_ID)));
    }

    @Test
    @WithMockUser("heycheff")
    void follow() throws Exception {
        when(useCase.follow(any())).thenReturn(new FollowResponse(List.of(USER_ID)));

        mvc.perform(post(URL + "/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new FollowRequest())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.following[0]", is(USER_ID)));
    }

    @Test
    @WithMockUser(value = "heycheff", authorities = "ADMIN")
    void updateRecommendationList() throws Exception {
        doNothing().when(useCase).updateRecommendationList(anyString(), any());

        mvc.perform(patch(URL + "/123/recommended-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new RecommendationRequest(List.of())
                        )))
                .andExpect(status().isOk());
    }
}
