package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testAuthenticateUserOk() {
        Long id = 1L;
        String email = "test@email.com";
        String password = "password123";
        String firstname = "hello";
        String lastname = "world";

        UserDetailsImpl userDetails = UserDetailsImpl
                .builder()
                .username(email)
                .firstName(firstname)
                .lastName(lastname)
                .id(id)
                .password(password)
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
        when(userRepository.findByEmail(email)).thenReturn(
                Optional.of(User
                        .builder()
                        .id(id)
                        .email(email)
                        .password(password)
                        .firstName(firstname)
                        .lastName(lastname)
                        .build()));

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.authenticateUser(new LoginRequest(email, password));
        JwtResponse responseBody = (JwtResponse) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, responseBody.getUsername());
        assertEquals(firstname, responseBody.getFirstName());
        assertEquals(lastname, responseBody.getLastName());
        assertEquals(id, responseBody.getId());
    }

    @Test
    public void testRegisterUserOk() {
        String email = "test@email.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.registerUser(new SignupRequest(email, "", "", password));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterUserEmailAlreadyTaken() {
        String email = "test@email.com";
        String password = "password123";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.registerUser(new SignupRequest(email, "", "", password));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}