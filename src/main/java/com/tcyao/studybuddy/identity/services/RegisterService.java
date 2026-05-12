package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.dto.RegisterRequestDTO;
import com.tcyao.studybuddy.identity.entities.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    private final AuthService authService;
    private final UserService userService;

    @Transactional
    public void registerEmail(RegisterRequestDTO registrationDetails) {
        User user = userService.registerUser(registrationDetails.getDisplayName(), registrationDetails.getAge());
        authService.registerAuth(user, registrationDetails.getEmail(), registrationDetails.getPassword(), "EMAIL");
    }
}
