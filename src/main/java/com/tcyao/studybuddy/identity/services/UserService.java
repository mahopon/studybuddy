package com.tcyao.studybuddy.identity.services;

import com.tcyao.studybuddy.identity.entities.User;
import com.tcyao.studybuddy.identity.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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


}
