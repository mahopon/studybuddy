package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.dto.GetProfileResponseDTO;
import com.tcyao.studybuddy.identity.dto.UpdateProfileRequestDTO;
import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.exceptions.InvalidProfileUpdateException;
import com.tcyao.studybuddy.identity.exceptions.UserNotFoundException;
import com.tcyao.studybuddy.identity.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Transactional
@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repo;

    public User registerUser(String displayName, int age) {
        User user = new User();
        user.setDisplayName(displayName);
        user.setAge(age);
        return repo.save(user);
    }

    @Transactional(readOnly = true)
    public GetProfileResponseDTO getUserProfile(UUID id) {
        return repo.findById(id)
                .map(u -> {
                    GetProfileResponseDTO dto = new GetProfileResponseDTO();
                    dto.setDisplayName(u.getDisplayName());
                    dto.setAge(u.getAge());
                    return dto;
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void getUserProfile(UUID id, UpdateProfileRequestDTO updatedProfile) {
        User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        AtomicBoolean updated = new AtomicBoolean(false);
        updatedProfile.getDisplayName().ifPresent(name -> {
            if (name.isBlank()) throw new InvalidProfileUpdateException("Display name must not be blank");
            user.setDisplayName(name);
            updated.set(true);
        });

        updatedProfile.getAge().ifPresent(age -> {
            if (age <= 0 || age > 150) throw new InvalidProfileUpdateException("Age must be between 1 and 150");
            user.setAge(age);
            updated.set(true);
        });

        if (updated.get()) {
            repo.save(user);
        }
    }
}
