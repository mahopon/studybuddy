package com.tcyao.studybuddy.identity.controllers;

import com.tcyao.studybuddy.identity.dto.ChangePasswordRequestDTO;
import com.tcyao.studybuddy.identity.dto.LoginRequestDTO;
import com.tcyao.studybuddy.identity.dto.RegisterRequestDTO;
import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.services.AuthService;
import com.tcyao.studybuddy.identity.services.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RegisterService registerService;
    private final AuthenticationManager authManager;

    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody RegisterRequestDTO req) {
        registerService.registerEmail(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<Void> postLogin(@RequestBody LoginRequestDTO loginReq, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);


        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> patchChangePassword(@RequestBody ChangePasswordRequestDTO changePWReq) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Auth user = (Auth) auth.getPrincipal();

        UUID id = user.getId();
        authService.changePassword(id, changePWReq.getOldPassword(), changePWReq.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
