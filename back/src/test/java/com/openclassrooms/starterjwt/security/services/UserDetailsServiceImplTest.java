package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadByUsername() {
        User user = User.builder()
                .email("test@email.com")
                .firstName("hello")
                .lastName("world")
                .id(1L)
                .password("password123").build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getFirstName(), userDetails.getFirstName());
        assertEquals(user.getLastName(), userDetails.getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testLoadByUsernameNotFound() {
        String username = "test";

        when(userRepository.findByEmail(username)).thenReturn(Optional.ofNullable(null));

        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

}