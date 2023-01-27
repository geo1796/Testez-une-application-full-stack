package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TeacherControllerTest {
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private TeacherService teacherService;

    @Test
    public void testFindByIdOk() {
        Long id = 1L;
        String firstname = "hello";
        String lastname = "world";
        Teacher teacher = Teacher.builder().id(id).firstName(firstname).lastName(lastname).build();
        TeacherDto dto = new TeacherDto();
        dto.setId(id);
        dto.setFirstName(firstname);
        dto.setLastName(lastname);

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById(""+id);
        TeacherDto responseBody = (TeacherDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, responseBody);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;

        when(teacherService.findById(id)).thenReturn(null);

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById(""+id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByIdBadRequest() {
        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById("abc");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAll() {
        List<Teacher> entities = new ArrayList<>();
        entities.add(Teacher.builder().id(1L).build());
        entities.add(Teacher.builder().id(2L).build());

        List<TeacherDto> dtos = new ArrayList<>();
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dtos.add(dto1);
        dtos.add(dto2);

        when(teacherService.findAll()).thenReturn(entities);
        when(teacherMapper.toDto(entities)).thenReturn(dtos);

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findAll();
        List<TeacherDto> responseBody = (List<TeacherDto>) response.getBody();
        assertEquals(dtos, responseBody);
    }
}