package mycode.pisicaspring.controller;

import mycode.pisicaspring.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PisicaExceptionHandlerTest {

    private PisicaExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new PisicaExceptionHandler();
    }

    @Test
    void handleAlreadyExistsReturnsConflict() {
        ResponseEntity<Map<String, Object>> response = handler.handleAlreadyExists(new PisicaAlreadyExistsException());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleNotFoundAggregatesExceptions() {
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(new RasaNotFoundException());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleBadRequestCoversValidationErrors() {
        PropertyReferenceException propertyEx = mock(PropertyReferenceException.class);
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(propertyEx);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Parametrii furnizati sunt invalizi.");
    }

    @Test
    void handleGenericCatchesOtherExceptions() {
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(new RuntimeException("boom"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("error", "A aparut o eroare neasteptata.");
    }
}
