package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerTest {
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private SessionService sessionService;

    @Test
    public void testFindByIdOk() {
        Long id = 1L;
        String name = "hello world";
        Session session = Session.builder().id(id).name(name).build();
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById(""+id);
        SessionDto responseBody = (SessionDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, responseBody);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;

        when(this.sessionService.getById(id)).thenReturn(null);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById(""+id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByIdBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById("abc");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll() {
        List<SessionDto> dtos = new ArrayList<>();
        SessionDto dto1 = new SessionDto();
        dto1.setName("dto1");
        SessionDto dto2 = new SessionDto();
        dto2.setName("dto2");
        dtos.add(dto1);
        dtos.add(dto2);

        when(sessionService.findAll()).thenReturn(new ArrayList<>());
        when(sessionMapper.toDto(any(List.class))).thenReturn(dtos);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findAll();
        List<SessionDto> responseBody = (List<SessionDto>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, responseBody);
    }

    @Test
    public void testCreateOk() {
        String name = "hello world";
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);
        Session session = Session.builder().name(name).build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.create(sessionDto);
        SessionDto responseBody = (SessionDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, responseBody);
    }

    @Test
    public void testUpdateOk() {
        Long id = 1L;
        String name = "hello world";
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);
        Session session = Session.builder().name(name).build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.update(""+id, sessionDto);
        SessionDto responseBody = (SessionDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, responseBody);
    }

    @Test
    public void testUpdateBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.update("abc", new SessionDto());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteOk() {
        Long id = 1L;
        when(sessionService.getById(id)).thenReturn(new Session());
        doNothing().when(sessionService).delete(id);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.delete(""+id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNotFound() {
        Long id = 1L;
        when(sessionService.getById(id)).thenReturn(null);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.delete(""+id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.delete("abc");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testParticipateOk() {
        Long id = 1L;
        Long userId = 2L;

        doNothing().when(sessionService).participate(id, userId);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.participate(""+id, ""+userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testParticipateBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.participate("abc", "abc");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipateOk() {
        Long id = 1L;
        Long userId = 2L;

        doNothing().when(sessionService).noLongerParticipate(id, userId);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.noLongerParticipate(""+id, ""+userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipateBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.noLongerParticipate("abc", "abc");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}