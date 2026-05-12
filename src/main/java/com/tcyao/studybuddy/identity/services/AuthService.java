package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.exceptions.EmailInUseException;
import com.tcyao.studybuddy.identity.exceptions.UserNotFoundException;
import com.tcyao.studybuddy.identity.exceptions.WrongPasswordException;
import com.tcyao.studybuddy.identity.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository repo;

    public Auth registerAuth(User user, String email, String password, String authType) {
        if (repo.findByEmail(email).isPresent()) {
            throw new EmailInUseException();
        }
        Auth auth = new Auth();
        auth.setUser(user);
        auth.setEmail(email);
        auth.setHashedPassword(passwordEncoder.encode(password));
        auth.setAuthType(authType);
        return repo.save(auth);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Auth with email not found: " + email));

//        return org.springframework.security.core.userdetails.User
//                .withUsername(auth.getEmail())
//                .password(auth.getHashedPassword())
//                .build();
    }

    public void changePassword(UUID id, String oldPassword, String newPassword) {
        Auth auth = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!passwordEncoder.matches(oldPassword, auth.getHashedPassword())) {
            throw new WrongPasswordException();
        }
        String newHashedPassword = passwordEncoder.encode(newPassword);
        auth.setHashedPassword(newHashedPassword);
        repo.save(auth);
    }
}
