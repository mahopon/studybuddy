package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.repositories.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository repo;

    @Transactional
    public Auth registerAuth(User user, String email, String password, String authType) {
//        if (repo.findByEmail(email).isPresent()) {
//
//        }
        Auth auth = new Auth();
        auth.setUser(user);
        auth.setEmail(email);
        auth.setHashedPassword(passwordEncoder.encode(password));
        auth.setAuthType(authType);
        return repo.save(auth);
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Auth with email not found: " + email));

//        return org.springframework.security.core.userdetails.User
//                .withUsername(auth.getEmail())
//                .password(auth.getHashedPassword())
//                .build();
    }
}
