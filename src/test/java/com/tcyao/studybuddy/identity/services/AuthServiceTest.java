package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.entities.Auth;
import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.exceptions.EmailInUseException;
import com.tcyao.studybuddy.identity.exceptions.UserNotFoundException;
import com.tcyao.studybuddy.identity.exceptions.WrongPasswordException;
import com.tcyao.studybuddy.identity.repositories.AuthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService service;

    private final String email = "test@example.com";
    private final String password = "password123";
    private final String authType = "EMAIL";

    @Test
    void registerAuth_ShouldCreateAuth_WhenEmailNotInUse() {
        User user = new User();
        String hashedPassword = "hashedValue";

        when(repo.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);
        when(repo.save(any(Auth.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Auth result = service.registerAuth(user, email, password, authType);

        ArgumentCaptor<Auth> captor = ArgumentCaptor.forClass(Auth.class);
        verify(repo).save(captor.capture());
        Auth saved = captor.getValue();

        assertSame(user, saved.getUser());
        assertEquals(email, saved.getEmail());
        assertEquals(hashedPassword, saved.getHashedPassword());
        assertEquals(authType, saved.getAuthType());
        assertSame(user, result.getUser());
    }

    @Test
    void registerAuth_ShouldThrow_WhenEmailInUse() {
        when(repo.findByEmail(email)).thenReturn(Optional.of(new Auth()));

        assertThrows(EmailInUseException.class,
                () -> service.registerAuth(new User(), email, password, authType));

        verify(repo, never()).save(any());
    }

    @Test
    void loadUserByUsername_ShouldReturnAuth_WhenFound() {
        Auth auth = new Auth();
        auth.setEmail(email);
        when(repo.findByEmail(email)).thenReturn(Optional.of(auth));

        UserDetails result = service.loadUserByUsername(email);

        assertSame(auth, result);
        assertEquals(email, result.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrow_WhenNotFound() {
        when(repo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(email));
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenOldPasswordMatches() {
        UUID id = UUID.randomUUID();
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        String oldHash = "oldHash";
        String newHash = "newHash";
        Auth auth = new Auth();
        auth.setHashedPassword(oldHash);

        when(repo.findById(id)).thenReturn(Optional.of(auth));
        when(passwordEncoder.matches(oldPassword, oldHash)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newHash);

        service.changePassword(id, oldPassword, newPassword);

        assertEquals(newHash, auth.getHashedPassword());
        verify(repo).save(auth);
    }

    @Test
    void changePassword_ShouldThrow_WhenWrongOldPassword() {
        UUID id = UUID.randomUUID();
        String oldPassword = "wrongPass";
        String newPassword = "newPass";
        String oldHash = "oldHash";
        Auth auth = new Auth();
        auth.setHashedPassword(oldHash);

        when(repo.findById(id)).thenReturn(Optional.of(auth));
        when(passwordEncoder.matches(oldPassword, oldHash)).thenReturn(false);

        assertThrows(WrongPasswordException.class,
                () -> service.changePassword(id, oldPassword, newPassword));

        verify(repo, never()).save(any());
    }

    @Test
    void changePassword_ShouldThrow_WhenAuthNotFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.changePassword(id, "old", "new"));

        verify(repo, never()).save(any());
    }
}
