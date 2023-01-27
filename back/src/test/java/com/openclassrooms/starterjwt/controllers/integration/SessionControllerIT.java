package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SessionControllerIT {
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
    public void testFindByIdOk() throws Exception {
        mockMvc.perform(get("/api/session/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/session/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/abc")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testFindByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session/abc")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testFindAllUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCreate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("new session");
        sessionDto.setDescription("this is a new session");
        sessionDto.setDate(Date.from(Instant.now()));
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(sessionDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testCreateUnauthorized() throws Exception {
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(new SessionDto()))
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("new session");
        sessionDto.setDescription("this is a new session");
        sessionDto.setDate(Date.from(Instant.now()));
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(sessionDto))
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateUnauthorized() throws Exception {
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(new SessionDto()))
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/session/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/session/1")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void testParticipate() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testParticipateUnauthorized() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void testNoLongerParticipate() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/2")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testNoLongerParticipateUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .header("Authorization", "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
