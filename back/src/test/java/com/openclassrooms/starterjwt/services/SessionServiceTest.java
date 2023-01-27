package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testParticipate() {
        Long id = 1L;
        Session session = Session.builder()
                .name("hello world")
                .id(id)
                .users(new ArrayList<>()).build();
        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@email.com")
                .lastName("hello")
                .firstName("world")
                .password("password123").build();

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(sessionRepository.save(session)).thenReturn(session);

        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        sessionService.participate(id, userId);

        assertTrue(session.getUsers().contains(user));
    }

    @Test
    public void testParticipateSessionNotFound() {
        Long id = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(new User()));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(NotFoundException.class, () -> sessionService.participate(id, userId));
    }

    @Test
    public void testParticipateUserNotFound() {
        Long id = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(new Session()));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(NotFoundException.class, () -> sessionService.participate(id, userId));
    }

    @Test
    public void testAlreadyParticipate() {
        Long id = 1L;
        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@email.com")
                .lastName("hello")
                .firstName("world")
                .password("password123").build();
        List<User> sessionUsers = new ArrayList<>();
        sessionUsers.add(user);
        Session session = Session.builder()
                .name("hello world")
                .id(id)
                .users(sessionUsers).build();

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(BadRequestException.class, () -> sessionService.participate(id, userId));
    }

    @Test
    public void testNoLongerParticipate() {
        Long id = 1L;
        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@email.com")
                .lastName("hello")
                .firstName("world")
                .password("password123").build();
        List<User> sessionUsers = new ArrayList<>();
        sessionUsers.add(user);
        Session session = Session.builder()
                .name("hello world")
                .id(id)
                .users(sessionUsers).build();

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(session));
        when(sessionRepository.save(session)).thenReturn(session);

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        sessionService.noLongerParticipate(id, userId);

        assertFalse(session.getUsers().contains(user));
    }

    @Test
    public void testNotAlreadyParticipate() {
        Long id = 1L;
        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@email.com")
                .lastName("hello")
                .firstName("world")
                .password("password123").build();
        Session session = Session.builder()
                .name("hello world")
                .id(id)
                .users(new ArrayList<>()).build();

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(session));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(id, userId));
    }

    @Test
    public void testNoLongerParticipateNotFound() {
        Long id = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(id, userId));
    }
}