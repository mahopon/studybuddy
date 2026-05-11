package com.tcyao.studybuddy.identity.repositories;

import com.tcyao.studybuddy.identity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    //public Optional<User> findById(UUID id);
}
