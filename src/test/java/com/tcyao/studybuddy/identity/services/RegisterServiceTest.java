package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.dto.RegisterRequestDTO;
import com.tcyao.studybuddy.identity.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterService service;

    @Test
    void registerEmail_ShouldCreateUserAndAuth() {
        String displayName = "Tester";
        int age = 25;
        String email = "test@example.com";
        String password = "password123";

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setDisplayName(displayName);
        dto.setAge(age);
        dto.setEmail(email);
        dto.setPassword(password);

        User savedUser = new User(displayName, age);
        when(userService.registerUser(displayName, age)).thenReturn(savedUser);

        service.registerEmail(dto);

        verify(userService).registerUser(displayName, age);
        verify(authService).registerAuth(savedUser, email, password, "EMAIL");
    }
}
