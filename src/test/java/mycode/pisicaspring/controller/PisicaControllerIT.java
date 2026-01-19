package mycode.pisicaspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mycode.pisicaspring.dtos.PisicaListResponsePageable;
import mycode.pisicaspring.dtos.PisicaResponse;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PisicaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PisicaRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllPisiciReturnsPagedResults() throws Exception {
        repository.save(pisica("Siamese", 2, "Luna"));
        repository.save(pisica("British", 4, "Maya"));

        MvcResult result = mockMvc.perform(get("/api/pisici")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn();

        PisicaListResponsePageable response = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                PisicaListResponsePageable.class
        );
        assertThat(response.totalElements()).isEqualTo(2);
        assertThat(response.pisicaDtoList()).hasSize(1);
        assertThat(response.pisicaDtoList().get(0).nume()).isEqualTo("Luna");
    }

    @Test
    void createPisicaEndpointPersistsEntity() throws Exception {
        Pisica payload = pisica("Sphynx", 3, "Zoe");

        MvcResult result = mockMvc.perform(post("/api/pisici/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(payload)))
                .andExpect(status().isCreated())
                .andReturn();

        PisicaResponse response = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                PisicaResponse.class
        );
        assertThat(response.nume()).isEqualTo("Zoe");

        assertThat(repository.findByNume("Zoe")).isPresent();
    }

    @Test
    void invalidSortParameterShouldReturnBadRequest() throws Exception {
        repository.save(pisica("Siamese", 2, "Luna"));

        MvcResult result = mockMvc.perform(get("/api/pisici")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "string"))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Object> response = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                Map.class
        );
        assertThat(response.get("error")).isEqualTo("Parametrii furnizati sunt invalizi.");
    }

    private Pisica pisica(String rasa, int varsta, String nume) {
        Pisica entity = new Pisica();
        entity.setRasa(rasa);
        entity.setVarsta(varsta);
        entity.setNume(nume);
        return entity;
    }
}
