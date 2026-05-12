package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.dto.GetProfileResponseDTO;
import com.tcyao.studybuddy.identity.dto.UpdateProfileRequestDTO;
import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.exceptions.InvalidProfileUpdateException;
import com.tcyao.studybuddy.identity.exceptions.UserNotFoundException;
import com.tcyao.studybuddy.identity.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final String displayName = "Tester";
    private final int age = 25;
    @Mock
    private UserRepository repo;
    @InjectMocks
    private UserService service;

    @Test
    void registerUser_ShouldSaveUser() {
        User user = new User();
        user.setDisplayName(displayName);
        user.setAge(age);

        when(repo.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = service.registerUser(displayName, age);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(repo).save(captor.capture());

        User captured = captor.getValue();

        assertEquals(displayName, captured.getDisplayName());
        assertEquals(age, captured.getAge());
        assertEquals(displayName, registeredUser.getDisplayName());
        assertEquals(age, registeredUser.getAge());
    }

    @Test
    void getUserProfile_WhenUserExists() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setDisplayName(displayName);
        user.setAge(age);

        when(repo.findById(id)).thenReturn(Optional.of(user));

        GetProfileResponseDTO result = service.getUserProfile(id);

        assertEquals(displayName, result.getDisplayName());
        assertEquals(age, result.getAge());
    }

    @Test
    void getUserProfile_NoUserExists() {
        UUID id = UUID.randomUUID();
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserProfile(id));
    }

    @Test
    void updateProfile_ShouldUpdateDisplayName() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setDisplayName("OldName");
        user.setAge(25);
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        ReflectionTestUtils.setField(dto, "displayName", "NewName");
        ReflectionTestUtils.setField(dto, "age", 25);

        when(repo.findById(id)).thenReturn(Optional.of(user));
        when(repo.save(user)).thenReturn(user);

        service.getUserProfile(id, dto);

        assertEquals("NewName", user.getDisplayName());
        assertEquals(25, user.getAge());
        verify(repo).save(user);
    }

    @Test
    void updateProfile_ShouldUpdateAge() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setDisplayName("Tester");
        user.setAge(25);
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        ReflectionTestUtils.setField(dto, "age", 30);

        when(repo.findById(id)).thenReturn(Optional.of(user));
        when(repo.save(user)).thenReturn(user);

        service.getUserProfile(id, dto);

        assertEquals("Tester", user.getDisplayName());
        assertEquals(30, user.getAge());
        verify(repo).save(user);
    }

    @Test
    void updateProfile_ShouldUpdateBothFields() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setDisplayName("OldName");
        user.setAge(25);
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        ReflectionTestUtils.setField(dto, "displayName", "NewName");
        ReflectionTestUtils.setField(dto, "age", 30);

        when(repo.findById(id)).thenReturn(Optional.of(user));
        when(repo.save(user)).thenReturn(user);

        service.getUserProfile(id, dto);

        assertEquals("NewName", user.getDisplayName());
        assertEquals(30, user.getAge());
        verify(repo).save(user);
    }

    @Test
    void updateProfile_ShouldThrow_WhenBlankDisplayName() {
        UUID id = UUID.randomUUID();
        User user = new User();
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        ReflectionTestUtils.setField(dto, "displayName", "   ");

        when(repo.findById(id)).thenReturn(Optional.of(user));

        assertThrows(InvalidProfileUpdateException.class,
                () -> service.getUserProfile(id, dto));

        verify(repo, never()).save(any());
    }

    @Test
    void updateProfile_ShouldThrow_WhenAgeIsZero() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setDisplayName("Tester");
        user.setAge(25);
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();

        when(repo.findById(id)).thenReturn(Optional.of(user));

        assertThrows(InvalidProfileUpdateException.class,
                () -> service.getUserProfile(id, dto));

        verify(repo, never()).save(any());
    }

    @Test
    void updateProfile_ShouldThrow_WhenAgeExceedsMax() {
        UUID id = UUID.randomUUID();
        User user = new User();
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        ReflectionTestUtils.setField(dto, "age", 151);

        when(repo.findById(id)).thenReturn(Optional.of(user));

        assertThrows(InvalidProfileUpdateException.class,
                () -> service.getUserProfile(id, dto));

        verify(repo, never()).save(any());
    }

    @Test
    void updateProfile_ShouldThrow_WhenUserNotFound() {
        UUID id = UUID.randomUUID();
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getUserProfile(id, dto));

        verify(repo, never()).save(any());
    }
}
