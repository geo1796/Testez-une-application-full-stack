package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @BeforeEach
    void setUp() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/abc")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testFindByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/user/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/user/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/abc")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testDeleteUnauthorizedJwt() throws Exception {
        mockMvc.perform(delete("/api/user/1")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void testDeleteUnauthorizedUsername() throws Exception {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("user@test.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        String wrongJwt = jwtUtils.generateJwtToken(authentication);
        mockMvc.perform(delete("/api/user/1")
                        .header("Authorization", "Bearer " + wrongJwt))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
