package edu.comillas.icai.gitt.pat.spring.p5.controller;

import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import edu.comillas.icai.gitt.pat.spring.p5.service.UserServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    private static final String NAME = "Name";
    private static final String EMAIL = "name@email.com";

    @Autowired private MockMvc mockMvc;

    @MockBean private UserServiceInterface userService;

    @Test void registerOk() throws Exception {
        // Given ...
        Mockito.when(userService.profile(Mockito.any(RegisterRequest.class)))
                .thenReturn(new ProfileResponse(NAME, EMAIL, Role.USER));
        String request = "{" +
                "\"name\":\"" + NAME + "\"," +
                "\"email\":\"" + EMAIL + "\"," +
                "\"role\":\"" + Role.USER + "\"," +
                "\"password\":\"aaaaaaA1\"}";
        // When ...
        this.mockMvc
            .perform(MockMvcRequestBuilders.post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
            // Then ...
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string("{" +
                    "\"name\":\"" + NAME + "\"," +
                    "\"email\":\"" + EMAIL + "\"," +
                    "\"role\":\"" + Role.USER + "\"}"));
    }

    /**
     * TODO#8
     * Completa este test de integración para que verifique la respuesta
     * que se debe devolver cuando se intenta registrar con un password inseguro
     * (no cumple condiciones)
     */
    @Test
    void registerInvalidPassword() throws Exception {
        // Given
        String invalidPassword = "debil"; // Password que no cumple con los requisitos mínimos
        RegisterRequest request = new RegisterRequest(NAME, EMAIL, Role.USER, invalidPassword);

        // When
        Mockito.when(userService.profile(Mockito.any(RegisterRequest.class)))
                .thenThrow(new DataIntegrityViolationException("Password inseguro"));

        // Then
        String requestJson = "{" +
                "\"name\":\"" + NAME + "\"," +
                "\"email\":\"" + EMAIL + "\"," +
                "\"role\":\"" + Role.USER + "\"," +
                "\"password\":\"" + invalidPassword + "\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
