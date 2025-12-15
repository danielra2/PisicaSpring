package mycode.pisicaspring.controller;

import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PisicaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PisicaRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllPisiciReturnsPagedResults() throws Exception {
        repository.save(new Pisica(null, "Siamese", 2, "Luna"));
        repository.save(new Pisica(null, "British", 4, "Maya"));

        mockMvc.perform(get("/api/pisici")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pisicaDtoList[0].nume").value("Luna"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void createPisicaEndpointPersistsEntity() throws Exception {
        String payload = "{" +
                "\"rasa\":\"Sphynx\"," +
                "\"varsta\":3," +
                "\"nume\":\"Zoe\"" +
                "}";

        mockMvc.perform(post("/api/pisici/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nume").value("Zoe"));

        assertThat(repository.findByNume("Zoe")).isPresent();
    }

    @Test
    void invalidSortParameterShouldReturnBadRequest() throws Exception {
        repository.save(new Pisica(null, "Siamese", 2, "Luna"));

        mockMvc.perform(get("/api/pisici")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "string"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parametrii furnizati sunt invalizi."));
    }
}
