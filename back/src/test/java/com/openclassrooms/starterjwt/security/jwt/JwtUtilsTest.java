package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testGenerateAndValidateJwt() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@email.com").build();
        String jwt = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetails, null));
        assertTrue(jwtUtils.validateJwtToken(jwt));
    }

    @Test
    public void testInvalidateJwt() {
        assertFalse(jwtUtils.validateJwtToken("not-a-valid-jwt"));
    }

}