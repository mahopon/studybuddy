package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.dto.RegisterDTO;
import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.entities.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    private final AuthService authService;
    private final UserService userService;

    public void registerEmail(RegisterDTO registrationDetails) {
        User user = userService.registerUser(registrationDetails.getDisplayName(), registrationDetails.getAge());
        Auth auth = authService.registerAuth(user, registrationDetails.getEmail(), registrationDetails.getPassword(), "EMAIL");

    }
}
