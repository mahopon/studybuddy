package com.tcyao.studybuddy.identity.repositories;

import com.tcyao.studybuddy.identity.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    Optional<Auth> findByEmail(String email);
}
