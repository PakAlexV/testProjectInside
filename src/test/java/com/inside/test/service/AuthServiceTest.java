package com.inside.test.service;

import com.inside.test.model.JwtRequest;
import com.inside.test.model.JwtResponse;
import com.inside.test.model.User;
import com.inside.test.model.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Array;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    private final String name = "test";
    private final String password = "123";

    @Test
    @DisplayName("Should get access token by login")
    void shouldLogin() {
        assertThrows(NullPointerException.class, ()->authService.login(null));

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setName(name);
        jwtRequest.setPassword(null);
        assertThrows(NullPointerException.class, ()->authService.login(jwtRequest));

        jwtRequest.setName(null);
        jwtRequest.setPassword(password);
        assertThrows(NullPointerException.class, ()->authService.login(jwtRequest));

        jwtRequest.setName(name);
        jwtRequest.setPassword(password);
        assertThrows(NullPointerException.class, ()->authService.login(jwtRequest));

        User mockUser = userService.createUser(name, "wrong");
        when(userRepository.findByName(name)).thenReturn(mockUser);

        assertThrows(SecurityException.class, ()->authService.login(jwtRequest));

        mockUser.setPassword(password);
        when(userRepository.findByName(name)).thenReturn(mockUser);

        JwtResponse token = authService.login(jwtRequest);

        assertEquals("1", token.getToken());
    }
}