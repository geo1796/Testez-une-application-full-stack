package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void testLoginOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest("yoga@studio.com", "test!1234");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testLoginBadCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("yoga@studio.com", "wrong password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegisterUserOk() throws Exception {
        SignupRequest signupRequest = new SignupRequest("test@email.com", "hello", "world", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testRegisterUserEmailAlreadyTaken() throws Exception {
        SignupRequest signupRequest = new SignupRequest("yoga@studio.com", "hello", "world", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
