package com.tcyao.studybuddy.identity.controllers;

import com.tcyao.studybuddy.identity.dto.GetProfileResponseDTO;
import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<GetProfileResponseDTO> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Auth user = (Auth) auth.getPrincipal();

        UUID id = user.getId();
        GetProfileResponseDTO profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }
}
