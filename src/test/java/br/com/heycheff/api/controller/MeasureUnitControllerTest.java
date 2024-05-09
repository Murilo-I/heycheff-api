package br.com.heycheff.api.controller;

import br.com.heycheff.api.model.MeasureUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MeasureUnitControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listAllMeasureUnits() throws Exception {
        mvc.perform(get("/produtos/0/medidas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].descricao",
                        is(MeasureUnit.UNIDADE.getDescription())));
    }

    @Test
    void measureUnitNotFound() throws Exception {
        mvc.perform(get("/produtos/1/medidas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
